package com.ai.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ai.base.gesture.AIGesturePasswordActivity;

/**
 * Created by wuyoujian on 17/3/29.
 */

public abstract class AIBaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AIActivityCollector.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (AIActivityConfig.getInstance().isShowGesturePasswordActivity()) {
            startActivity(new Intent(this, AIGesturePasswordActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AIActivityConfig.getInstance().saveLockTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AIActivityCollector.getInstance().removeActivity(this);
    }

}
