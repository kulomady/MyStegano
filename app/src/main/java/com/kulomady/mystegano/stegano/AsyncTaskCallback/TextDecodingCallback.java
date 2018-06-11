package com.kulomady.mystegano.stegano.AsyncTaskCallback;


import com.kulomady.mystegano.stegano.ImageSteganography;

/**
 * This the callback interface for TextDecoding AsyncTask.
 */

public interface TextDecodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(ImageSteganography result);

}
