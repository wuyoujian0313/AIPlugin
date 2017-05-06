package com.ai.base.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Size;
import android.widget.ImageView;

import com.ai.base.util.Utility;

/**
 * Created by wuyoujian on 2017/5/3.
 */

public class AIMaskView extends ImageView {

    private Paint mLinePaint;
    private Paint mAreaPaint;
    private Rect mCenterRect = null;
    private Context mContext;

    private int mWidthScreen;
    private int mHeightScreen;

    public AIMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        mContext = context;
        Size s	= Utility.getScreenMetrics(mContext);
        mWidthScreen = s.getWidth();
        mHeightScreen = s.getHeight();
    }

    private void initPaint(){
        //
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLUE);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(30);

        //
        mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAreaPaint.setColor(Color.GRAY);
        mAreaPaint.setStyle(Paint.Style.FILL);
        mAreaPaint.setAlpha(180);
    }


    public void setCenterRect(Rect r){
        this.mCenterRect = r;
        postInvalidate();
    }


    public void clearCenterRect(Rect r){
        this.mCenterRect = null;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if(mCenterRect == null)
            return;
        canvas.drawRect(0, 0, mWidthScreen, mCenterRect.top, mAreaPaint);
        canvas.drawRect(0, mCenterRect.bottom + 1, mWidthScreen, mHeightScreen, mAreaPaint);
        canvas.drawRect(0, mCenterRect.top, mCenterRect.left - 1, mCenterRect.bottom  + 1, mAreaPaint);
        canvas.drawRect(mCenterRect.right + 1, mCenterRect.top, mWidthScreen, mCenterRect.bottom + 1, mAreaPaint);

        canvas.drawRect(mCenterRect, mLinePaint);
        super.onDraw(canvas);
    }
}
