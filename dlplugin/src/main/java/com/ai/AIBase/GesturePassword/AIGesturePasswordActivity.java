package com.ai.AIBase.GesturePassword;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.AIBase.AIBaseActivity;
import com.ai.AIBase.util.Utility;

/**
 * Created by wuyoujian on 17/4/27.
 */

public class AIGesturePasswordActivity extends AIBaseActivity {

    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private TextView mTextView;
    private AIGesturePasswordLayout mGesturePasswordLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("手势密码");
        initView();
    }

    private void initView() {

        this.mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        this.mLinearLayout.setPadding(Utility.dip2px(this,16),Utility.dip2px(this,16),
                Utility.dip2px(this,16),Utility.dip2px(this,16));

        this.mLinearLayout.setLayoutParams(params);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        tvParams.setMargins(0,Utility.dip2px(this,16),0,0);
        tvParams.gravity = Gravity.CENTER;
        this.mImageView = new ImageView(this);
        //设置头像

        this.mImageView.setContentDescription("头像");
        this.mLinearLayout.addView(this.mImageView,tvParams);

        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        this.mTextView = new TextView(this);
        this.mTextView.setText("请绘制手势密码");
        this.mTextView.setTextSize(16);
        this.mTextView.setGravity(Gravity.CENTER);
        this.mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.mLinearLayout.addView(this.mTextView,tvParams);

        this.mGesturePasswordLayout = new AIGesturePasswordLayout(this);
        this.mGesturePasswordLayout.setGravity(Gravity.CENTER_VERTICAL);
        this.mGesturePasswordLayout.setBackgroundColor(0x00ffffff);
        this.mGesturePasswordLayout.setTryTimes(4);

        // 设置初始密码,可以根据本地存储的密码
        this.mGesturePasswordLayout.setAnswer("14789");

        this.mGesturePasswordLayout.setShowPath(true);
        this.mGesturePasswordLayout.isFirstSet(false);
        this.mGesturePasswordLayout.setOnGestureLockViewListener(mListener);
        //
        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        this.mLinearLayout.addView(this.mGesturePasswordLayout,tvParams);
        this.setContentView(this.mLinearLayout);
    }

    @Override
    public void onBackPressed() {
        // 阻止Lock页面的返回事件
        moveTaskToBack(true);
    }


    /**
     * 处理手势图案的输入结果
     * @param matched
     */
    private void gestureEvent(boolean matched) {
        if (matched) {
            mTextView.setText("输入正确");
            //finish();
        } else {
            mTextView.setText("手势错误，还剩"+ mGesturePasswordLayout.getTryTimes() + "次");
        }
    }

    private void firstSetPattern(boolean patternOk) {
        if (patternOk) {
            mTextView.setText("请再次输入以确认");
        } else {
            mTextView.setText("需要四个点以上");
        }
    }

    /**
     * 处理输错次数超限的情况
     */
    private void unmatchedExceedBoundary() {
        // 正常情况这里需要做处理（如退出或重登）
        Toast.makeText(this, "错误次数太多，请重新登录", Toast.LENGTH_SHORT).show();
    }

    // 手势操作的回调监听
    private AIGesturePasswordLayout.OnGesturePasswordViewListener mListener = new
            AIGesturePasswordLayout.OnGesturePasswordViewListener() {
                @Override
                public void onGestureEvent(boolean matched) {
                    gestureEvent(matched);
                }

                @Override
                public void onUnmatchedExceedBoundary() {
                    unmatchedExceedBoundary();
                }

                @Override
                public void onFirstSetPattern(boolean patternOk) {
                }
            };


}