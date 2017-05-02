package com.ai.base;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.ryg.dynamicload.DLBasePluginActivity;

/**
 * Created by wuyoujian on 17/3/30.
 */

public class AIWebViewBasePlugin {

    private DLBasePluginActivity mActivity;

    public AIWebViewBasePlugin(){}
    public AIWebViewBasePlugin(DLBasePluginActivity activity) {
        this.mActivity = activity;
    }

    public DLBasePluginActivity getActivity() {
        return mActivity;
    }
    public void setActivity(DLBasePluginActivity activity) {
        this.mActivity = activity;
    }

    @android.webkit.JavascriptInterface
    public void test(){
        Toast.makeText(mActivity.that,"调用NA接口",Toast.LENGTH_SHORT).show();
    }
}

