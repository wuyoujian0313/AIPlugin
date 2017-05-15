package com.ai.base.gesture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.base.AIBaseActivity;
import com.ai.base.config.ActivityConfig;
import com.ai.base.util.Utility;

/**
 * Created by wuyoujian on 17/4/27.
 */

public class AISetGesturePasswordActivity extends AIBaseActivity {

    /**
     * 发送密码设置结果广播
     */
    public static final String kPasswordCommitBroadcast = "com.ai.base.passwordCommit.LOCAL_BROADCAST";
    /**
     * 监听密码设置结果广播, 调用者发送这个广播,kCommitResultReceiverKey 数据类型是int 0标识密码通过
     */
    public static final String kCommitResultReceiver = "com.ai.base.commitResult.LOCAL_BROADCAST";

    /**
     * 从广播里获取密码的key,一般通过通知又带回出去
     */
    public static final String kGesturePasswordKey = "kGesturePassworkKey";

    public static final String kCommitResultReceiverKey = "kCommitResultReceiverKey";

    /**
     * 从广播里获取用户名的key,一般通过通知又带回出去
     */
    public static final String kUserNameKey = "kUserNameKey";

    /**
     * 从广播里获取扩张字段key,一般通过通知又带回出去
     */
    public static final String kPhoneNumber = "kPhoneNumber";

    /**
     * 从广播里获取扩张字段key,一般通过通知又带回出去
     */
    public static final String kExtandKey = "kExtandKey";

    private RelativeLayout mRelativeLayout;
    private TextView mTextView;
    private AIGesturePasswordLayout mGesturePasswordLayout;

    private String mFirstPassword = "";
    /**
     * 最大设置次数
     */
    private int mTryTimes = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置手势密码");
        this.initView();
    }

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private void sendPasswordCommitBroadcast(String password) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        Intent intent = new Intent(kPasswordCommitBroadcast);
        intent.putExtra(kGesturePasswordKey,password);
        String userName = getIntent().getStringExtra(kUserNameKey);
        String extendData = getIntent().getStringExtra(kExtandKey);
        String phoneNumber = getIntent().getStringExtra(kPhoneNumber);

        intent.putExtra(kUserNameKey,userName);
        intent.putExtra(kExtandKey,extendData);
        intent.putExtra(kPhoneNumber,phoneNumber);
        localBroadcastManager.sendBroadcast(intent);

        // 同时注册监听
        registerCommitResultReceier();
    }

    private void registerCommitResultReceier() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(kCommitResultReceiver);
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }

    private LocalReceiver localReceiver;
    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra(kCommitResultReceiverKey,0);

            if (code == 0) {
                mTextView.setText("密码设置成功");
                mGesturePasswordLayout.setViewColor(true);

                ActivityConfig.getInstance().setAlreadyGesturePassword();
                //
                finish();
            } else {
                mTextView.setText("密码设置失败,请重新设置");
                mGesturePasswordLayout.setViewColor(false);
                mFirstPassword = "";
                mTryTimes = 3;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localBroadcastManager != null && localReceiver != null) {
            localBroadcastManager.unregisterReceiver(localReceiver);
        }
    }


    private void initView() {

        mRelativeLayout = new RelativeLayout(this);
        mRelativeLayout.setBackgroundColor(Color.WHITE);
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
        mTextView.setTextColor(Color.BLACK);
        mTextView.setTextSize(16);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mRelativeLayout.addView(mTextView,tvParams);

        mGesturePasswordLayout = new AIGesturePasswordLayout(this);
        mGesturePasswordLayout.setGravity(Gravity.CENTER_VERTICAL);
        mGesturePasswordLayout.setBackgroundColor(0x00ffffff);
        mGesturePasswordLayout.setOnGestureLockViewListener(mListener);
        //
        tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRelativeLayout.addView(mGesturePasswordLayout,tvParams);
        setContentView(mRelativeLayout);
    }


    /**
     * 处理手势图案的输入结果
     * @param password
     */
    private void gestureEvent(String password) {

        if (password != null) {
            if (mTryTimes >= 1 && mTryTimes <=3 ) {
                if (password.length() < 4) {
                    mTextView.setText("需要四个点以上");
                    mGesturePasswordLayout.setViewColor(false);
                    return;
                } else {

                    if (mFirstPassword.contentEquals("") ) {
                        mFirstPassword = password;
                        mTextView.setText("请再次绘制密码");
                    } else {
                        if (password.contentEquals(mFirstPassword)) {
                            sendPasswordCommitBroadcast(password);
                        } else {
                            mTextView.setText("两次输入不一致");
                            mGesturePasswordLayout.setViewColor(false);
                            mTryTimes --;
                        }
                    }
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
        Toast.makeText(this, "错误次数太多，请重新绘制第一次密码", Toast.LENGTH_SHORT).show();

        mFirstPassword = "";
        mTryTimes = 3;

        mTextView.setText("请重新设置密码");
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
