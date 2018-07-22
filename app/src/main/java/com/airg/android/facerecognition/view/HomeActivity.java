package com.airg.android.facerecognition.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airg.android.facerecognition.R;
import com.airg.android.facerecognition.interfaces.home.IHomePresenter;
import com.airg.android.facerecognition.interfaces.home.IHomeView;
import com.airg.android.facerecognition.presenter.HomePresenter;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements IHomeView{

    private static final String TAG = "HomeActivity";
    private Button btnRecognize, btnSwitchCamera;
    private ProgressDialog dialog;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private LinearLayout lyCameraPreview;
    private ImageView imvCapture;
    private Toast toast;
    private boolean cameraFront = true;
    private int cameraId;

    private IHomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_detection);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initial();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        //check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                homePresenter.saveImage(data);
                //refresh camera to continue preview
                //mPreview.refreshCamera(mCamera, cameraId);
            }
        };
        return picture;
    }

    View.OnClickListener OnSwitchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //get the number of cameras
            int camerasNumber = Camera.getNumberOfCameras();
            if (camerasNumber > 1) {
                releaseCamera();
                chooseCamera();
            } else {
                showToast(getResources().getString(R.string.msg_only_one_camera));
            }
        }
    };

    private int findFrontFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void chooseCamera() {
        //if the camera preview is the front
        hideImage();
        if (cameraFront) {
            cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera, cameraId);
            }
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera, cameraId);
            }
        }
    }
    private View.OnClickListener OnCaptureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideImage();
            mCamera.takePicture(null, null, mPicture);
        }
    };

    private View.OnTouchListener OnImageCaptureTouchListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imvCapture.setVisibility(View.GONE);
                return false;
            }
        };
    }
    private void getViews(){
        imvCapture = findViewById(R.id.imvCapture);
        lyCameraPreview = findViewById(R.id.lyCameraPreview);
        mPreview = new CameraPreview(this, mCamera);
        btnRecognize = findViewById(R.id.btnRecognize);
        btnSwitchCamera = findViewById(R.id.btnSwitchCamera);
        lyCameraPreview.addView(mPreview);
    }

    private void setListeners(){
        btnRecognize.setOnClickListener(OnCaptureListener);
        btnSwitchCamera.setOnClickListener(OnSwitchCameraListener);
        imvCapture.setOnTouchListener(OnImageCaptureTouchListener());
    }

    @Override
    public void initial() {
        getViews();
        setListeners();
        homePresenter = new HomePresenter(this);
    }

    @Override
    public void showCamera() {
        if (!hasCamera(this)) {
            showToast(getResources().getString(R.string.msg_no_camera));
            finish();
        }
        if (mCamera == null) {
            if (findFrontFacingCamera() < 0) {
                showToast(getResources().getString(R.string.msg_no_front_camera));
                btnSwitchCamera.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findFrontFacingCamera());
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera, cameraId);
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void showImage(String url) {
        imvCapture.setVisibility(View.VISIBLE);
        Picasso.with(this).load(R.mipmap.ic_launcher_round).into(imvCapture);
    }

    @Override
    public void hideImage() {
        imvCapture.setVisibility(View.GONE);
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    public void showDialog(String message) {
        if(dialog == null)
            dialog = new ProgressDialog(this);
        dialog.setMessage(message);
        dialog.show();
    }

    @Override
    public void closeDialog() {
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onSaveImageFinish(String result) {
        if("success".equalsIgnoreCase(result)){
            homePresenter.recognize(this);
        }
    }
}
