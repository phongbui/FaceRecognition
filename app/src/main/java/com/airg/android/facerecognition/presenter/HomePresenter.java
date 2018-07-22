package com.airg.android.facerecognition.presenter;

import android.content.Context;

import com.airg.android.facerecognition.entities.RecognitionResponse;
import com.airg.android.facerecognition.interfaces.home.IHomeModel;
import com.airg.android.facerecognition.interfaces.home.IHomePresenter;
import com.airg.android.facerecognition.interfaces.home.IHomeView;
import com.airg.android.facerecognition.model.HomeModel;

/**
 * Created by Phong Bui on 7/21/2018.
 */

public class HomePresenter implements IHomePresenter, IHomeModel.OnRecognizeFinishListener, IHomeModel.OnSaveImageFinishListener{

    private IHomeView iHomeView;
    private IHomeModel iHomeModel;

    public HomePresenter(IHomeView iHomeView){
        this.iHomeView = iHomeView;
        this.iHomeModel = new HomeModel();
    }

    @Override
    public void recognize(Context context) {
        if(iHomeView != null){
            iHomeView.showDialog("Recognizing...");
        }
        iHomeModel.recognize(this, context);
    }

    @Override
    public void saveImage(byte[] data) {
        iHomeModel.saveImage(this, data);
    }

    @Override
    public void onRecognizeError(String error) {
        if(iHomeView != null){
            iHomeView.closeDialog();
            iHomeView.showImage(error);
        }
    }

    @Override
    public void onRecognizeSuccess(RecognitionResponse result) {
        if(iHomeView != null){
            iHomeView.closeDialog();
            iHomeView.showImage(result.getImgUrl());
        }
    }

    @Override
    public void onSaveImageError(String error) {
        if(iHomeView != null){
            iHomeView.onSaveImageFinish(error);
        }
    }

    @Override
    public void onSaveImageSuccess(String message) {
        if(iHomeView != null){
            iHomeView.onSaveImageFinish(message);
        }
    }
}
