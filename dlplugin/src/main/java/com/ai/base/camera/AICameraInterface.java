package com.ai.base.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.view.SurfaceHolder;


import com.ai.base.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wuyoujian on 2017/5/3.
 */

public class AICameraInterface {

    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean mIsPreviewing = false;
    private float mPreviwRate = -1f;
    private Size mDSTSize;
    private String mImageName;

    private static AICameraInterface instance;

    public interface CamOpenOverCallback {
        public void cameraHasOpened();
    }

    public static AICameraInterface getInstance() {
        if (instance == null) {
            synchronized (AICameraInterface.class) {
                instance = new AICameraInterface();
            }
        }
        return instance;
    }

    /**
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback) {
        mCamera = Camera.open();
        callback.cameraHasOpened();
    }


    /**
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        if (mIsPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initCamera(previewRate);
        }
    }


    /**
     * @param surface
     * @param previewRate
     */
    public void doStartPreview(SurfaceTexture surface, float previewRate) {
        if (mIsPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initCamera(previewRate);
        }

    }

    /**
     *
     */
    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mIsPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     *
     */
    public void doTakePicture() {
        if (mIsPreviewing && (mCamera != null)) {
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }


    public void doTakePicture(int w, int h) {
        if (mIsPreviewing && (mCamera != null)) {

            mDSTSize.width = w;
            mDSTSize.height = h;
            mCamera.takePicture(mShutterCallback, null, mRectJpegPictureCallback);
        }
    }

    public Size doGetPrictureSize() {
        return mCamera.getParameters().getPictureSize();
    }

    private String saveImagePath() {
        File parentPath = Environment.getExternalStorageDirectory();
        String storagePath = parentPath.getAbsolutePath() + "/" + "CameraImage";
        File folder = new File(storagePath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String path = storagePath + "/" + mImageName;
        return path;
    }


    private void initCamera(float previewRate) {
        if (mCamera != null) {

            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);

            CamParaUtil camParaUtil = new CamParaUtil();

            Camera.Size pictureSize = camParaUtil.getPropPictureSize(
                    mParams.getSupportedPictureSizes(), previewRate, 800);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Camera.Size previewSize = camParaUtil.getPropPreviewSize(
                    mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);
            mCamera.setDisplayOrientation(90);

            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);
            mCamera.startPreview();

            mIsPreviewing = true;
            mPreviwRate = previewRate;

            mParams = mCamera.getParameters();
        }
    }

    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
        }
    };
    Camera.PictureCallback mRawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
        }
    };
    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);
                mCamera.stopPreview();
                mIsPreviewing = false;

                if (null != b) {
                    Bitmap rotaBitmap = Utility.getRotateBitmap(b, 90.0f);
                    //
                    Utility.saveBitmap(rotaBitmap, saveImagePath());
                }
                mCamera.startPreview();
                mIsPreviewing = true;
            }
        }
    };

    Camera.PictureCallback mRectJpegPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);
                mCamera.stopPreview();
                mIsPreviewing = false;
            }

            if (null != b) {

                Bitmap rotaBitmap = Utility.getRotateBitmap(b, 90.0f);
                int x = rotaBitmap.getWidth() / 2 - mDSTSize.width / 2;
                int y = rotaBitmap.getHeight() / 2 - mDSTSize.height / 2;

                Bitmap rectBitmap = Bitmap.createBitmap(rotaBitmap, x, y, mDSTSize.width, mDSTSize.height);
                Utility.saveBitmap(rectBitmap, saveImagePath());
                if (rotaBitmap.isRecycled()) {
                    rotaBitmap.recycle();
                    rotaBitmap = null;
                }
                if (rectBitmap.isRecycled()) {
                    rectBitmap.recycle();
                    rectBitmap = null;
                }
            }

            mCamera.startPreview();
            mIsPreviewing = true;
            if (!b.isRecycled()) {
                b.recycle();
                b = null;
            }

        }
    };

    public class CamParaUtil {
        private CameraSizeComparator sizeComparator = new CameraSizeComparator();
        public Camera.Size getPropPreviewSize(List<Camera.Size> list, float th, int minHeight){
            Collections.sort(list, sizeComparator);

            int i = 0;
            for(Camera.Size s:list){
                if((s.height >= minHeight) && equalRate(s, th)){
                    break;
                }
                i++;
            }
            if(i == list.size()){
                i = 0;
            }
            return list.get(i);
        }
        public Camera.Size getPropPictureSize(List<Camera.Size> list, float th, int minHeight){
            Collections.sort(list, sizeComparator);

            int i = 0;
            for(Camera.Size s:list){
                if((s.height >= minHeight) && equalRate(s, th)){
                    break;
                }
                i++;
            }
            if(i == list.size()){
                i = 0;
            }
            return list.get(i);
        }

        public boolean equalRate(Camera.Size s, float rate){
            float r = (float)(s.width)/(float)(s.height);
            if(Math.abs(r - rate) <= 0.03)
            {
                return true;
            }
            else{
                return false;
            }
        }

        public  class CameraSizeComparator implements Comparator<Camera.Size>{
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if(lhs.width == rhs.width){
                    return 0;
                }
                else if(lhs.width > rhs.width){
                    return 1;
                }
                else{
                    return -1;
                }
            }
        }
    }
}
