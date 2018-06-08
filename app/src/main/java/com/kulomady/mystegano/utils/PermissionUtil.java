package com.kulomady.mystegano.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 6/8/18.
 */

public class PermissionUtil {

    public interface PermissionCallback {
        void onPermissionCallbackResult(boolean success);
    }

    private static class PermissionRequest {
        final Activity activity;
        final String permission;
        final int requestCode;
        final CharSequence reqReason;
        final CharSequence rejectedMsg;
        final PermissionCallback callback;

        PermissionRequest(Activity activity,
                          String permission,
                          int requestCode,
                          CharSequence reqReason,
                          CharSequence rejectedMsg,
                          PermissionCallback callback) {
            this.activity = activity;
            this.permission = permission;
            this.requestCode = requestCode;
            this.reqReason = reqReason;
            this.rejectedMsg = rejectedMsg;
            this.callback = callback;
        }
    }

    private static List<PermissionRequest> permissionRequestList = new ArrayList<>();

    public static boolean hasPermission(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void checkPermission(Activity activity,
                                       String permission,
                                       int requestCode,
                                       CharSequence reqReason,
                                       CharSequence rejectedMsg,
                                       final PermissionCallback callback) {
        if (hasPermission(activity, permission)) {
            activity.getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    callback.onPermissionCallbackResult(true);
                }
            });
        } else {
            boolean shouldShowReqReason = ActivityCompat
                    .shouldShowRequestPermissionRationale(activity, permission);
            PermissionRequest request = new PermissionRequest(
                    activity, permission, requestCode, reqReason, rejectedMsg, callback);
            if (shouldShowReqReason) {
                showRequestReason(request);
            } else {
                requestPermission(request);
            }
        }
    }

    private static void showRequestReason(final PermissionRequest request) {
        new AlertDialog.Builder(request.activity)
                .setCancelable(false)
                .setMessage(request.reqReason)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission(request);
                    }
                })
                .show();
    }

    private static void requestPermission(PermissionRequest request) {
        permissionRequestList.add(request);
        ActivityCompat.requestPermissions(request.activity, new String[]{request.permission}, request.requestCode);
    }

    private static void showRejectedMessage(final PermissionRequest request) {
        new AlertDialog.Builder(request.activity)
                .setCancelable(false)
                .setMessage(request.rejectedMsg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.callback.onPermissionCallbackResult(false);
                        permissionRequestList.remove(request);
                    }
                })
                .setNegativeButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAppDetailSetting(request);
                    }
                })
                .show();
    }

    private static void openAppDetailSetting(PermissionRequest request) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", request.activity.getPackageName(), null);
        intent.setData(uri);
        request.activity.startActivityForResult(intent, request.requestCode);
    }

    public static void onRequestPermissionResult(Activity activity,
                                                 int requestCode,
                                                 String[] permissions,
                                                 int[] grantResults) {
        PermissionRequest targetRequest = null;
        for (PermissionRequest request : permissionRequestList) {
            if (request.activity.equals(activity)
                    && request.requestCode == requestCode
                    && request.permission.equals(permissions[0])) {
                targetRequest = request;
                break;
            }
        }
        if (targetRequest != null) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                targetRequest.callback.onPermissionCallbackResult(true);
                permissionRequestList.remove(targetRequest);
            } else {
                if (TextUtils.isEmpty(targetRequest.rejectedMsg)) {
                    targetRequest.callback.onPermissionCallbackResult(false);
                    permissionRequestList.remove(targetRequest);
                } else {
                    showRejectedMessage(targetRequest);
                }
            }
        }
    }

    public static void onActivityResult(Activity activity,
                                        int requestCode) {
        PermissionRequest targetRequest = null;
        for (PermissionRequest request : permissionRequestList) {
            if (request.activity.equals(activity)
                    && request.requestCode == requestCode) {
                targetRequest = request;
                break;
            }
        }
        if (targetRequest != null) {
            if (hasPermission(targetRequest.activity, targetRequest.permission)) {
                targetRequest.callback.onPermissionCallbackResult(true);
            } else {
                targetRequest.callback.onPermissionCallbackResult(false);
            }
            permissionRequestList.remove(targetRequest);
        }
    }
}
