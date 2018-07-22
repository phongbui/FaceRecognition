package com.airg.android.facerecognition.entities;

/**
 * Created by Phong Bui on 7/21/2018.
 */

public class RecognitionResponse {

    private String message;
    private String imgUrl = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
