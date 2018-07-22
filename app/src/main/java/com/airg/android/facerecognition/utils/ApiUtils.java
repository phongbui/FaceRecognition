package com.airg.android.facerecognition.utils;

import com.airg.android.facerecognition.service.RecognitionService;
import com.airg.android.facerecognition.service.RetrofitClient;

/**
 * Created by Phong Bui on 7/21/2018.
 */

public class ApiUtils {
    public static final String BASE_URL = "https://api.recognize";

    public static RecognitionService getRecognitionService() {
        return RetrofitClient.getOkClient(BASE_URL).create(RecognitionService.class);
    }
}
