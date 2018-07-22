package com.airg.android.facerecognition.model;

import android.content.Context;

import com.airg.android.facerecognition.entities.RecognitionResponse;
import com.airg.android.facerecognition.interfaces.home.IHomeModel;
import com.airg.android.facerecognition.service.RecognitionService;
import com.airg.android.facerecognition.utils.ApiUtils;
import com.airg.android.facerecognition.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phong Bui on 7/21/2018.
 */

public class HomeModel implements IHomeModel{

    private RecognitionService recognitionService;
    @Override
    public void recognize(final OnRecognizeFinishListener listener, Context context) {
        recognitionService = ApiUtils.getRecognitionService();
        recognitionService.regconize().enqueue(new Callback<RecognitionResponse>() {
            @Override
            public void onResponse(Call<RecognitionResponse> call, Response<RecognitionResponse> response) {
                listener.onRecognizeSuccess(response.body());
            }

            @Override
            public void onFailure(Call<RecognitionResponse> call, Throwable t) {
                listener.onRecognizeError("No image");
            }
        });
    }

    @Override
    public void saveImage(OnSaveImageFinishListener listener, byte[] data) {
        File pictureFile = FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            listener.onSaveImageError("pictureFile == null");
            return;
        }
        try {
            //write the file
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            listener.onSaveImageSuccess("success");
        } catch (FileNotFoundException e) {
            listener.onSaveImageError(e.getMessage());
        } catch (IOException e) {
            listener.onSaveImageError(e.getMessage());
        }
    }
}
