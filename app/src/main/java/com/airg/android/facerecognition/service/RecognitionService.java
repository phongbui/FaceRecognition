package com.airg.android.facerecognition.service;

import com.airg.android.facerecognition.entities.RecognitionResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Phong Bui on 7/21/2018.
 */

public interface RecognitionService {
    @GET("/recognize")
    Call<RecognitionResponse> regconize();
}
