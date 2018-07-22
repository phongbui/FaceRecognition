package com.airg.android.facerecognition.interfaces.home;

import android.content.Context;

/**
 * Created by Phong Bui on 7/21/2018.
 */

public interface IHomeView {

    void initial();
    void showCamera();
    void showImage(String url);
    void hideImage();
    void showToast(String message);
    void showDialog(String message);
    void closeDialog();
    void onSaveImageFinish(String result);
}
