package com.kulomady.mystegano.Text.AsyncTaskCallback;


import com.kulomady.mystegano.Text.ImageSteganography;

/**
 * This the callback interface for TextEncoding AsyncTask.
 */

public interface TextEncodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(ImageSteganography result);

}
