package com.ai.base.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * Created by wuyoujian on 2017/5/3.
 */

public class AICameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    AICameraInterface mCameraInterface;
    Context mContext;
    SurfaceHolder mSurfaceHolder;


    public AICameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        AICameraInterface.getInstance().doStopCamera();
    }
    public SurfaceHolder getSurfaceHolder(){
        return mSurfaceHolder;
    }

}
