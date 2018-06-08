package com.kulomady.mystegano;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final int SELECT_PICTURE = 100;

    @BindView(R.id.img_result)
    ImageView imgResult;

    @BindView(R.id.btn_choose_image)
    Button btnChooseImage;

    private Uri filepath;

    //Bitmaps
    private Bitmap original_image;
    private Bitmap encoded_image;



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

}
