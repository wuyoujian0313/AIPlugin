package com.ai.AIBase;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.ai.AIBase.config.GlobalCfg;
import com.ai.AIBase.config.PluginCfg;
import com.ai.AIBase.util.BeanInvoker;
import com.ryg.dynamicload.DLBasePluginActivity;

import java.io.InputStream;

/**
 * Created by wuyoujian on 17/3/30.
 */

public class AIWebViewPluginEngine {

    private WebView mWebView;
    private DLBasePluginActivity mActivity;
    private String  mPluginCfgFile = "plugin.xml";

    private Handler mHandler = new Handler();

    private static AIWebViewPluginEngine instance;
    public static AIWebViewPluginEngine getInstance() {
        if (instance == null) {
            synchronized (AIWebViewPluginEngine.class) {
                instance = new AIWebViewPluginEngine();
            }
        }
        return instance;
    }

    public AIWebViewPluginEngine() {
        //
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void registerPlugins(DLBasePluginActivity activity, WebView webView,String configFileName) {

        this.mActivity = activity;
        this.mWebView = webView;
        this.mPluginCfgFile = configFileName;
        try {
            InputStream is = mActivity.getResources().getAssets().open(mPluginCfgFile);
            PluginCfg plugincfg = PluginCfg.getInstance(is);
            String[] names = plugincfg.getNames();
            if (names.length > 0) mWebView.getSettings().setJavaScriptEnabled(true);

            for (String name : names) {
                String className = plugincfg.attr(name, PluginCfg.CONFIG_ATTR_CLASS);
                AIWebViewBasePlugin plugin = (AIWebViewBasePlugin) BeanInvoker.instance(className,DLBasePluginActivity.class,mActivity,false);
                mWebView.addJavascriptInterface(plugin, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excuteJavascript(String js, final ValueCallback<String> callback) {
        if (mWebView != null) {

            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
                final String javascript = js;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("javascript:" + javascript);
                        callback.onReceiveValue("success");
                    }
                });
            } else {
                mWebView.evaluateJavascript(js,callback);
            }
        }
    }
}
