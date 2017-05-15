package com.ai.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.ai.base.config.ActivityConfig;
import com.ai.base.gesture.AIGesturePasswordActivity;

/**
 * Created by wuyoujian on 17/3/29.
 */

public abstract class AIBaseActivity extends AppCompatActivity {

    protected  boolean mEnbleGesturePwd = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AIStatusBarCompat.compat(this);
        AIStatusBarCompat.compat(this, 0xFF000000);
        AIActivityCollector.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityConfig.getInstance().isShowGesturePasswordActivity()
                && !this.getClass().getSimpleName().equalsIgnoreCase("AIGesturePasswordActivity")
                && mEnbleGesturePwd) {
            startActivity(new Intent(this, AIGesturePasswordActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityConfig.getInstance().saveLockTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AIActivityCollector.getInstance().removeActivity(this);
    }


}
