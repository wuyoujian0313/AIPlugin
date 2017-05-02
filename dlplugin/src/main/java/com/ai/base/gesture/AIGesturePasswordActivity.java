package com.ai.base.gesture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.base.AIActivityConfig;
import com.ai.base.AIBaseActivity;
import com.ai.base.util.Utility;

/**
 * Created by wuyoujian on 17/4/27.
 */

public class AIGesturePasswordActivity extends AIBaseActivity {

    /**
     * 发送密码校验结果广播
      */

    public static final String kPasswordCheckBroadcast = "com.ai.base.passwordCheck.LOCAL_BROADCAST";
    public static final String kCheckBroadcastIntentKey = "kCheckBroadcastIntentKey";

    /**
     * 监听密码校验结果广播, 调用者发送这个广播,kCheckResultReceiverKey 数据类型是int 0标识密码通过
     */
    public static final String kCheckResultReceiver = "com.ai.base.checkResult.LOCAL_BROADCAST";
    public static final String kCheckResultReceiverKey = "kCheckResultReceiverKey";

    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private TextView mTextView;
    private AIGesturePasswordLayout mGesturePasswordLayout;

    /**
     * 最大尝试次数
     */
    private int mTryTimes = 3;

    public void setTryTimes(int tryTimes) {
        mTryTimes = tryTimes;
    }


    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private void sendPasswordCheckBroadcast(String password) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        Intent intent = new Intent(kPasswordCheckBroadcast);
        intent.putExtra(kCheckBroadcastIntentKey,password);
        localBroadcastManager.sendBroadcast(intent);

        // 同时注册监听
        registerCheckResultReceier();
    }


    private LocalReceiver localReceiver;
    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra(kCheckResultReceiverKey,0);
            setCheckStatus(code == 0);
        }
    }
    private void registerCheckResultReceier() {

        intentFilter = new IntentFilter();
        intentFilter.addAction(kCheckResultReceiver);
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("手势密码");
        initView();
    }

    private void initView() {

        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setPadding(Utility.dip2px(this,16),Utility.dip2px(this,16),
                Utility.dip2px(this,16),Utility.dip2px(this,16));

        mLinearLayout.setLayoutParams(params);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        tvParams.setMargins(0,Utility.dip2px(this,16),0,0);
        tvParams.gravity = Gravity.CENTER;
        mImageView = new ImageView(this);
        //设置头像

        mImageView.setContentDescription("头像");
        mLinearLayout.addView(this.mImageView,tvParams);

        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mTextView = new TextView(this);
        mTextView.setText("请绘制手势密码");
        mTextView.setTextSize(16);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mLinearLayout.addView(mTextView,tvParams);

        mGesturePasswordLayout = new AIGesturePasswordLayout(this);
        mGesturePasswordLayout.setGravity(Gravity.CENTER_VERTICAL);
        mGesturePasswordLayout.setBackgroundColor(0x00ffffff);

        mGesturePasswordLayout.setShowPath(true);
        mGesturePasswordLayout.setOnGestureLockViewListener(mListener);

        //
        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.addView(mGesturePasswordLayout,tvParams);
        setContentView(mLinearLayout);
    }

    @Override
    public void onBackPressed() {
        // 阻止Lock页面的返回事件
        moveTaskToBack(true);
    }


    private void setCheckStatus(Boolean bSuc) {
        if(bSuc) {
            mTextView.setText("输入正确");
            mGesturePasswordLayout.setViewColor(true);
            finish();
        } else {

            mTextView.setText("输入不正确");
            mGesturePasswordLayout.setViewColor(false);
            mTryTimes --;
        }
    }

    /**
     * 处理手势图案的输入结果
     * @param password
     */
    private void gestureEvent(String password) {

        if (password != null) {
            if (mTryTimes >= 0 && mTryTimes <=3 ) {
                if (password.length() < 4) {
                    mTextView.setText("需要四个点以上");
                    mGesturePasswordLayout.setViewColor(false);
                    return;
                } else {
                    sendPasswordCheckBroadcast(password);
                }
            } else {
                unmatchedExceedBoundary();
            }
        }
    }
    /**
     * 处理输错次数超限的情况
     */
    private void unmatchedExceedBoundary() {
        // 正常情况这里需要做处理（如退出或重登）

        AIActivityConfig.getInstance().clearAlreadyGesturePassword();
        Toast.makeText(this, "错误次数太多，请重新用密码登录", Toast.LENGTH_SHORT).show();
    }

    // 手势操作的回调监听
    private AIGesturePasswordLayout.OnGesturePasswordViewListener mListener = new
            AIGesturePasswordLayout.OnGesturePasswordViewListener() {
                @Override
                public void onGestureEvent(String password) {
                    gestureEvent(password);
                }
            };


}