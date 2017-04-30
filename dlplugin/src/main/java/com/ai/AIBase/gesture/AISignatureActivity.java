package com.ai.AIBase.gesture;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.ai.AIBase.AIBaseActivity;
import com.ai.AIBase.util.Utility;

import java.io.File;
import java.io.IOException;


/**
 * Created by wuyoujian on 17/4/28.
 */

public class AISignatureActivity extends AIBaseActivity {

    private AISignatureView mSignatureView;
    private LinearLayout mLinearLayout;
    private LinearLayout mToolBarLayout;

    private String mSavePath;

    private Button mClearButton;
    private Button mSaveButton;
    private Button mFinishButton;
    private Button mCancelButton;

    private OnSignatureLister mOnSignatureListner;

    public void setmOnSignatureListner(OnSignatureLister mOnSignatureListner) {
        this.mOnSignatureListner = mOnSignatureListner;
    }

    public interface OnSignatureLister {
        /**
         *
         * @param savePath 全路径,包含文件名,保存的图片是PNG类型
         */
        public void saveSignatureImage(String savePath);

        /**
         * 确定
         */
        public void finishSignture();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("手写签名");
        String filePath = getCacheDir().getAbsolutePath()+ "/images";
        File file = new File(filePath);
        file.mkdir();
        mSavePath = filePath + "/signature.png";
        initView();
    }

    private void initView() {

        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setLayoutParams(params);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        tvParams.setLayoutDirection(LinearLayout.VERTICAL);
        tvParams.weight = 1;

        mSignatureView = new AISignatureView(this);

        mLinearLayout.addView(mSignatureView,tvParams);


        mToolBarLayout = new LinearLayout(this);
        mToolBarLayout.setOrientation(LinearLayout.HORIZONTAL);
        mToolBarLayout.setBackgroundColor(0xFF8fc320);

        mClearButton = new Button(this);
        mClearButton.setBackgroundColor(Color.TRANSPARENT);
        mClearButton.setText("清除");
        mClearButton.setTextColor(0xFFFFFFFF);
        mClearButton.setLinkTextColor(0xFFf3b100);
        mClearButton.setTextSize(16);
        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(Utility.dip2px(this,10),0,
                Utility.dip2px(this,10),0);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignatureView.clear();
            }
        });
        mToolBarLayout.addView(mClearButton,tvParams);

        mSaveButton = new Button(this);
        mSaveButton.setBackgroundColor(Color.TRANSPARENT);
        mSaveButton.setText("保存");
        mSaveButton.setTextColor(0xFFFFFFFF);
        mSaveButton.setLinkTextColor(0xFFf3b100);
        mSaveButton.setTextSize(16);
        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(Utility.dip2px(this,10),0,
                Utility.dip2px(this,10),0);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSignatureView.getmIsTouched()) {
                    try {

                        mSignatureView.save(mSavePath,true,0);
                        if (mOnSignatureListner != null) {
                            mOnSignatureListner.saveSignatureImage(mSavePath);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mToolBarLayout.addView(mSaveButton,tvParams);

        mFinishButton = new Button(this);
        mFinishButton.setBackgroundColor(Color.TRANSPARENT);
        mFinishButton.setText("完成");
        mFinishButton.setTextColor(0xFFFFFFFF);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSignatureView.getmIsTouched()) {

                    try {
                        mSignatureView.save(mSavePath);
                        if (mOnSignatureListner != null) {
                            mOnSignatureListner.finishSignture();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                finish();
            }
        });

        mFinishButton.setTextSize(16);
        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(Utility.dip2px(this,10),0,
                Utility.dip2px(this,10),0);
        mToolBarLayout.addView(mFinishButton,tvParams);

        mCancelButton = new Button(this);
        mCancelButton.setBackgroundColor(Color.TRANSPARENT);
        mCancelButton.setText("取消");
        mCancelButton.setTextColor(0xFFFFFFFF);
        mCancelButton.setLinkTextColor(0xFFf3b100);
        mCancelButton.setTextSize(16);
        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(Utility.dip2px(this,10),0,
                Utility.dip2px(this,10),0);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignatureView.delete(mSavePath);
                finish();
            }
        });
        mToolBarLayout.addView(mCancelButton,tvParams);

        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mToolBarLayout.setLayoutParams(tvParams);
        mLinearLayout.addView(mToolBarLayout,tvParams);

        setContentView(mLinearLayout);
    }
}
