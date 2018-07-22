package com.airg.android.facerecognition.interfaces.home;

import android.content.Context;

import com.airg.android.facerecognition.entities.RecognitionResponse;

import java.util.List;

/**
 * Created by Phong Bui on 7/21/2018.
 */

public interface IHomeModel {

    interface OnRecognizeFinishListener{
        void onRecognizeError(String error);
        void onRecognizeSuccess(RecognitionResponse result);
    }

    interface OnSaveImageFinishListener{
        void onSaveImageError(String error);
        void onSaveImageSuccess(String message);
    }

    void recognize(OnRecognizeFinishListener listener, Context context);
    void saveImage(OnSaveImageFinishListener listener,byte[] data);
}
