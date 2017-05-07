package com.ai.base;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.ryg.dynamicload.DLBasePluginActivity;

/**
 * Created by wuyoujian on 17/3/30.
 */

public class AIWebViewBasePlugin {

    // 不采用动态插件
    private AIBaseActivity mActivity;
    public AIWebViewBasePlugin(AIBaseActivity activity) {
        this.mActivity = activity;
    }
    public AIBaseActivity getActivity() {
        return mActivity;
    }
    public void setActivity(AIBaseActivity activity) {
        this.mActivity = activity;
    }

    // 不采用动态插件
    private DLBasePluginActivity mDLActivity;
    public AIWebViewBasePlugin(DLBasePluginActivity activity) {
        this.mDLActivity = activity;
    }
    public DLBasePluginActivity getDLActivity() {
        return mDLActivity;
    }
    public void setDLActivity(DLBasePluginActivity activity) {
        this.mDLActivity = activity;
    }

    @android.webkit.JavascriptInterface
    public void test(){
    }
}

