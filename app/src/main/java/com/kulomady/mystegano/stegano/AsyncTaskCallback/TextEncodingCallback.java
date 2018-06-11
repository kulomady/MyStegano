package com.kulomady.mystegano.stegano.AsyncTaskCallback;


import com.kulomady.mystegano.stegano.ImageSteganography;

/**
 * This the callback interface for TextEncoding AsyncTask.
 */

public interface TextEncodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(ImageSteganography result);

}
