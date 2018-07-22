package com.airg.android.facerecognition.interfaces.home;

import android.content.Context;

/**
 * Created by Phong Bui on 7/21/2018.
 */

public interface IHomePresenter {

    void recognize(Context context);
    void saveImage(byte[] data);
}
