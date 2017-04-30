package com.ai.AIBase.gesture;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.AIBase.AIBaseActivity;
import com.ai.AIBase.util.Utility;

/**
 * Created by wuyoujian on 17/4/27.
 */

public class AISetGesturePasswordActivity extends AIBaseActivity {

    private RelativeLayout mRelativeLayout;
    private TextView mTextView;
    private AIGesturePasswordLayout mGesturePasswordLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("设置手势密码");
        this.initView();
    }


    private void initView() {

        mRelativeLayout = new RelativeLayout(this);
        mRelativeLayout.setPadding(Utility.dip2px(this,16),Utility.dip2px(this,16),
                Utility.dip2px(this,16),Utility.dip2px(this,16));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        mRelativeLayout.setLayoutParams(params);


        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(0,Utility.dip2px(this,16),0,0);

        mTextView = new TextView(this);
        mTextView.setText("请绘制手势密码");
        mTextView.setTextSize(16);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mRelativeLayout.addView(mTextView,tvParams);

        mGesturePasswordLayout = new AIGesturePasswordLayout(this);
        mGesturePasswordLayout.setGravity(Gravity.CENTER_VERTICAL);
        mGesturePasswordLayout.setBackgroundColor(0x00ffffff);
        mGesturePasswordLayout.setUnMatchExceedBoundary(10000);
        mGesturePasswordLayout.isFirstSet(true);
        mGesturePasswordLayout.setOnGestureLockViewListener(mListener);
        //
        tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRelativeLayout.addView(mGesturePasswordLayout,tvParams);
        setContentView(mRelativeLayout);
    }


    private void gestureEvent(boolean matched) {
        if (matched) {
            mTextView.setText("设置成功");

            String password = this.mGesturePasswordLayout.getChooseStr();
            // 保存

            Toast.makeText(this,"设置密码为: " + password,Toast.LENGTH_SHORT).show();

            //finish();
        } else {
            mTextView.setText("手势不一致，请重试");
        }
    }

    private void firstSetPattern(boolean patternOk) {
        if (patternOk) {
            mTextView.setText("请再次输入以确认");
        } else {
            mTextView.setText("需要四个点以上");
        }
    }

    // 回调监听
    private AIGesturePasswordLayout.OnGesturePasswordViewListener mListener = new
            AIGesturePasswordLayout.OnGesturePasswordViewListener() {
                @Override
                public void onGestureEvent(boolean matched) {
                    gestureEvent(matched);
                }

                @Override
                public void onUnmatchedExceedBoundary() {
                }

                @Override
                public void onFirstSetPattern(boolean patternOk) {
                    firstSetPattern(patternOk);
                }
            };
}
