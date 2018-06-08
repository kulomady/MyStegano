package com.kulomady.mystegano;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.kulomady.mystegano.Text.AsyncTaskCallback.TextDecodingCallback;
import com.kulomady.mystegano.Text.ImageSteganography;
import com.kulomady.mystegano.Text.TextDecoding;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements TextDecodingCallback {

    private static final String TAG = "LoginActivity";

    private static final int SELECT_PICTURE = 100;

    @BindView(R.id.img_result)
    ImageView imgResult;

    @BindView(R.id.btn_choose_image)
    Button btnChooseImage;

    @BindView(R.id.input_username)
    EditText edtUsername;

    @BindView(R.id.wrapper_layout)
    LinearLayout wrapperLayout;


    private Uri filepath;
    private Bitmap original_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.link_signup) void btnSignupClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.btn_login) void btnLoginClicked() {
        Toast.makeText(this,"test", Toast.LENGTH_LONG).show();
        doDecode();
    }

    @OnClick(R.id.btn_choose_image) void onButtonChooseImageClicked(){
        showImageChooser();
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

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        if (result != null){
            if (!result.isDecoded())
                Snackbar.make(wrapperLayout,"No Message Found", Snackbar.LENGTH_LONG).show();
            else{
                if (!result.isSecretKeyWrong()){
                    Snackbar.make(wrapperLayout,
                            "Decoded and message is: " + result.getMessage(),
                            Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(wrapperLayout,"Wrong secret key", Snackbar.LENGTH_LONG).show();
                }
            }
        }
        else {
            Snackbar.make(wrapperLayout,"Select Image First", Snackbar.LENGTH_LONG).show();
        }
    }

    private void doDecode(){
        if (filepath != null){

            //Making the ImageSteganography object
            ImageSteganography imageSteganography = new ImageSteganography(edtUsername.getText().toString(),
                    original_image);

            //Making the TextDecoding object
            TextDecoding textDecoding = new TextDecoding(LoginActivity.this, this);

            //Execute Task
            textDecoding.execute(imageSteganography);
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

    void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }
}
