package com.kulomady.mystegano;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kulomady.mystegano.repo.ApiClient;
import com.kulomady.mystegano.repo.User;
import com.kulomady.mystegano.stegano.AsyncTaskCallback.TextEncodingCallback;
import com.kulomady.mystegano.stegano.ImageSteganography;
import com.kulomady.mystegano.stegano.TextEncoding;
import com.kulomady.mystegano.utils.CheckPermissionUtil;
import com.kulomady.mystegano.utils.PermissionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class RegisterActivity extends AppCompatActivity
        implements TextEncodingCallback, PermissionUtil.PermissionCallback {

    private static final String TAG = "RegisterActivity";
    private static final int SELECT_PICTURE = 100;

    @BindView(R.id.img_result)
    ImageView imgResult;

    @BindView(R.id.btn_choose_image)
    Button btnChooseImage;

    @BindView(R.id.input_username)
    EditText edtUsername;

    @BindView(R.id.input_password)
    EditText edtPassword;

    @BindView(R.id.input_email)
    EditText edtEmail;

    @BindView(R.id.wrapper_layout)
    LinearLayout wrapperLayout;

    private Uri filepath;

    private Bitmap original_image;
    private Bitmap encoded_image;
    private String secreetKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.link_login) void onButtonLoginClicked(){
        this.onBackPressed();
    }

    @OnClick(R.id.btn_choose_image) void onButtonChooseImageClicked(){
        showImageChooser();
    }

    @OnClick(R.id.btn_signup) void onButtonSignupClicked(){
        CheckPermissionUtil.checkWriteSdCard(this, this);
    }

    void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){

            displaySelectedImage(data);
        }

    }

    private void displaySelectedImage(Intent data) {
        filepath = data.getData();
        try{
            original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
            imgResult.setVisibility(View.VISIBLE);
            imgResult.setImageBitmap(original_image);
            btnChooseImage.setVisibility(View.GONE);
        }
        catch (IOException e){
            Log.d(TAG, "Error : " + e);
        }
    }

    private void doEncode(){

        if (filepath != null){
            if (edtPassword.getText() != null ){
                String password = edtPassword.getText().toString();
                String username = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();

                secreetKey = username;
                String message = username + "-" + password + "-" + email;
                ImageSteganography imageSteganography =
                        new ImageSteganography(message,
                        secreetKey,
                        original_image);
                TextEncoding textEncoding =
                        new TextEncoding(RegisterActivity.this,
                                RegisterActivity.this);

                textEncoding.execute(imageSteganography);

            }
        }
    }

    private void doSaveImage() {
        String name = UUID.randomUUID().toString();

        ProgressDialog progressDialog = createProgressDialog();
        progressDialog.show();
        if (encoded_image != null){
            saveEncodedImage(name);
        }
        progressDialog.dismiss();
    }

    private void saveEncodedImage(String name) {
        String name_image = name + "_encoded" + ".png";
        File encoded_file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), name_image);

        try {
            encoded_file.createNewFile();
            FileOutputStream fout_encoded_image = new FileOutputStream(encoded_file);
            encoded_image.compress(Bitmap.CompressFormat.PNG, 100, fout_encoded_image);
            fout_encoded_image.flush();
            fout_encoded_image.close();

            doRegister(encoded_file,secreetKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doRegister(File file, String secreetKey) {
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);

        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), edtUsername.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), edtPassword.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), edtEmail.getText().toString());
        RequestBody secreetKeyParam = RequestBody.create(MediaType.parse("text/plain"), secreetKey);

        Call<User> request = ApiClient.getIntance().service().register(image,username,password,email,secreetKeyParam);

        request.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, @NonNull Response<User> response) {
                Log.d(TAG, "onResponse: " + response.body());

                if(response.isSuccessful()){
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }else {
                    Log.d(TAG, "onResponse: " + response.body());
                }
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Snackbar.make(wrapperLayout,"User Already Exist",Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private ProgressDialog createProgressDialog(){
        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Saving Image");
        progressDialog.setMessage("Loading Please Wait...");

        return progressDialog;
    }

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        if (result != null && result.isEncoded()){
            encoded_image = result.getEncoded_image();
            Toast.makeText(this, "Successfully encode", Toast.LENGTH_SHORT).show();
            imgResult.setImageBitmap(encoded_image);
            doSaveImage();
        }
    }

    @Override
    public void onPermissionCallbackResult(boolean success) {
        doEncode();
    }
}
