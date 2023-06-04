package com.danny.xcamera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.ZoomState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.danny.xcamera.databinding.ActivityCameraBinding;
import com.danny.xtool.LogTool;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = CameraActivity.class.getSimpleName();
    private ActivityCameraBinding cameraBinding;

    /**
     * 前后摄像头
     */
    private int lensFacing = CameraSelector.LENS_FACING_BACK;

    /**
     * 比例
     */
    private static final double RADIO_4_3 = 4.0 / 3.0;
    private static final double RADIO_16_9 = 16.0 / 9.0;

    private ProcessCameraProvider cameraProvider;

    /**
     * 拍摄
     */
    private ImageCapture imageCapture;

    private Camera camera;

    /**
     * 实时捕捉画面
     */
    private Preview preview;

    /**
     * 拍摄控制
     */
    private CameraControl cameraControl;

    /**
     * 照片信息
     */
    private CameraInfo cameraInfo;

    /**
     * 照相线程
     */
    private ExecutorService executors = Executors.newSingleThreadExecutor();

    private boolean isSaveSdcard = true;
    private boolean needLocation = false;

    private String wmTx;

    /**
     * 照相文件
     */
    private File photoFile;

    private CameraXLocation xLocation;

    private PreViewCallback callback = new PreViewCallback() {
        @Override
        public void zoom(float delta) {
            if (null != cameraInfo && null != cameraControl) {
                ZoomState zoomState = cameraInfo.getZoomState().getValue();
                if (null != zoomState) {
                    cameraControl.setZoomRatio(zoomState.getZoomRatio() * delta);
                    LogTool.INSTANCE.d("zoom");
                }
            }
        }


        @Override
        public void click(float x, float y) {
            if (null == cameraControl) {
                return;
            }
            cameraBinding.xCameraFocus.startFocus(x, y);
            MeteringPointFactory factory = cameraBinding.xCameraView.getMeteringPointFactory();
            MeteringPoint point = factory.createPoint(x, y);
            FocusMeteringAction action = new FocusMeteringAction
                    .Builder(point, FocusMeteringAction.FLAG_AF)
                    .setAutoCancelDuration(3, TimeUnit.SECONDS).build();
            ListenableFuture<FocusMeteringResult> future = cameraControl.startFocusAndMetering(action);
            future.addListener(() -> LogTool.INSTANCE.e(""), ContextCompat.getMainExecutor(CameraActivity.this));
        }

        @Override
        public void doubleClick(float x, float y) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        cameraBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        initConfigData();
        initListener();
        cameraBinding.xCameraView.post(this::setUpCamera);
    }

    // 初始化相机
    private void setUpCamera() {
        ListenableFuture<ProcessCameraProvider> providerListenableFuture = ProcessCameraProvider.getInstance(this);
        providerListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = providerListenableFuture.get();
                    // 判断摄像头,后置优先
                    if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                        lensFacing = CameraSelector.LENS_FACING_BACK;
                    } else if (cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                        lensFacing = CameraSelector.LENS_FACING_FRONT;
                    } else {
                        throw new IllegalStateException("摄像机不可用");
                    }
                    bindCameraUseCase();
                } catch (ExecutionException | InterruptedException | CameraInfoUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void initListener() {
        cameraBinding.xCameraKa.setOnClickListener(this::onClick);
    }

    private void initConfigData() {
        Intent intent = getIntent();
        if (null != intent) {
            isSaveSdcard = intent.getBooleanExtra(CameraXConstants.IS_SAVE_SD, true);
            needLocation = intent.getBooleanExtra(CameraXConstants.NEED_LOCATION, false);
            wmTx = intent.getStringExtra(CameraXConstants.ADD_WATER);
        }
        if (needLocation) {
            xLocation = new CameraXLocation();
        }
        initWaterMark();
    }

    private void initWaterMark() {

    }

    private void bindCameraUseCase() {
        DisplayMetrics dm = new DisplayMetrics();
        cameraBinding.xCameraView.getDisplay().getMetrics(dm);
        int screenAspectRatio = aspectRatio(dm.widthPixels, dm.heightPixels);
        int rotation = (int) cameraBinding.xCameraView.getRotation();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();
        preview = new Preview.Builder().setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation).build();
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation).build();

        cameraProvider.unbindAll();
        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        cameraInfo = camera.getCameraInfo();
        cameraControl = camera.getCameraControl();
        preview.setSurfaceProvider(cameraBinding.xCameraView.createSurfaceProvider());

        imageCapture.setFlashMode(ImageCapture.FLASH_MODE_AUTO);
    }

    private int aspectRatio(int width, int height) {
        double ratio = Math.max(width, height) / Math.min(width, height);
        if (Math.abs(ratio - RADIO_4_3) <= Math.abs(ratio - RADIO_16_9)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    @Override
    public void onClick(View v) {

    }

    private void takePhoto() {
        photoFile = CameraXUtil.getFile(this, isSaveSdcard);

        // 前置摄像头开启
        ImageCapture.Metadata metadata = new ImageCapture.Metadata();
        metadata.setReversedHorizontal(lensFacing == CameraSelector.LENS_FACING_FRONT);

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions
                .Builder(photoFile).setMetadata(metadata).build();
        imageCapture.takePicture(outputFileOptions, executors, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri uri = outputFileResults.getSavedUri();
                if (uri == null) {
                    uri = Uri.fromFile(photoFile);
                }
                cameraBinding.cameraPicture.setImageURI(uri);
                cameraBinding.cameraPictureLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {

            }
        });
    }

    private void savePhotoAndExit() {
        if (null == photoFile) {
            errorExit("take photo error");
            return;
        }
        String photoPath = null;
        try {
            photoPath = photoFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();

        }

        if (TextUtils.isEmpty(photoPath)) {
            errorExit("photoPath is null");
            return;
        }

        exit(photoPath);
    }

    private void errorExit(String msg) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("BACK_MSG", msg);
        bundle.putString("FILE_PATH", "");
        intent.putExtra("BACK_BUNDLE", bundle);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    private void exit(String photoPath) {
        if (isSaveSdcard) {
            CameraXUtil.mediaScan(this, photoFile);
        }

        Intent intent = new Intent();
        intent.putExtra("", photoPath);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 切换摄像头
     */
    private void switchFacing() {
        if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
            lensFacing = CameraSelector.LENS_FACING_BACK;
        } else {
            lensFacing = CameraSelector.LENS_FACING_FRONT;
        }
        // 重新设置用例
        bindCameraUseCase();
    }
}
