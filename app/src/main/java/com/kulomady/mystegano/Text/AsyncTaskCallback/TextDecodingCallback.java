package com.kulomady.mystegano.Text.AsyncTaskCallback;


import com.kulomady.mystegano.Text.ImageSteganography;

/**
 * This the callback interface for TextDecoding AsyncTask.
 */

public interface TextDecodingCallback {

    public void onStartTextEncoding();

    public void onCompleteTextEncoding(ImageSteganography result);

}
