package com.ai.base.gesture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.base.config.ActivityConfig;
import com.ai.base.AIBaseActivity;
import com.ai.base.config.GlobalCfg;
import com.ai.base.util.AESEncrypt;
import com.ai.base.util.LocalStorageManager;
import com.ai.base.util.Utility;

/**
 * Created by wuyoujian on 17/4/27.
 */

public class AILocGesturePasswordActivity extends AIBaseActivity {

    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private TextView mTextView;
    private AIGesturePasswordLayout mGesturePasswordLayout;

    /**
     * 发送密码错误结果广播
     */
    public static final String kPasswordErrorBroadcast = "com.ai.base.passwordError.LOCAL_BROADCAST";

    private String mAnswer;
    /**
     * 最大尝试次数
     */
    private int mTryTimes = 3;

    public void setTryTimes(int tryTimes) {
        mTryTimes = tryTimes;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("手势密码");

        try {
            String encryptKey = GlobalCfg.getInstance().attr(GlobalCfg.CONFIG_FIELD_ENCRYPTKEY);
            String publicKey = GlobalCfg.getInstance().attr(GlobalCfg.CONFIG_FIELD_PUBLICKEY);
            String key = AESEncrypt.decrypt(encryptKey,publicKey);
            LocalStorageManager.getInstance().setContext(this);
            LocalStorageManager.getInstance().setEncryptKey(key);

            String answer = LocalStorageManager.getInstance().getString("answer");
            this.mAnswer = answer;
        } catch (Exception e) {
        }

        initView();
    }

    private void initView() {

        mLinearLayout = new LinearLayout(this);
        mLinearLayout.setBackgroundColor(Color.WHITE);
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
        mTextView.setTextColor(Color.BLACK);
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
                    if (password.equalsIgnoreCase(mAnswer)) {
                        setCheckStatus(true);
                    } else {
                        setCheckStatus(false);
                    }
                }
            } else {
                unmatchedExceedBoundary();
            }
        }
    }

    private LocalBroadcastManager localBroadcastManager;
    private void sendPasswordErrorBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(kPasswordErrorBroadcast);
        localBroadcastManager.sendBroadcast(intent);
    }


    /**
     * 处理输错次数超限的情况
     */
    private void unmatchedExceedBoundary() {
        // 正常情况这里需要做处理（如退出或重登）
        ActivityConfig.getInstance().clearAlreadyGesturePassword();
        Toast.makeText(this, "错误次数太多，请重新用密码登录", Toast.LENGTH_SHORT).show();

        sendPasswordErrorBroadcast();
        finish();
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
