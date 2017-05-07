package com.ai.base;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.ai.base.config.WebViewPluginCfg;
import com.ai.base.util.BeanInvoker;
import com.ryg.dynamicload.DLBasePluginActivity;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;

/**
 * Created by wuyoujian on 17/3/30.
 */

public class AIWebViewPluginEngine {

    private WebView mWebView;
    private AIBaseActivity mActivity;
    private String  mPluginCfgFile = "h5Plugin.xml";

    private DLBasePluginActivity mDLActivity;
    protected DexClassLoader classLoader = null;
    private String mApkPath;

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

    public void setApkPath(String apkPath) {
        this.mApkPath = apkPath;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void registerPlugins(DLBasePluginActivity activity, WebView webView,String configFileName) {

        this.mDLActivity = activity;
        this.mWebView = webView;
        this.mPluginCfgFile = configFileName;
        try {
            InputStream is = mDLActivity.that.getResources().getAssets().open(mPluginCfgFile);
            WebViewPluginCfg plugincfg = WebViewPluginCfg.getInstance();
            plugincfg.parseConfig(is);

            String[] names = plugincfg.getNames();
            if (names.length > 0) mWebView.getSettings().setJavaScriptEnabled(true);

            File dexPath = mDLActivity.that.getDir("dex", 0);

            for (String name : names) {
                String className = plugincfg.attr(name, WebViewPluginCfg.CONFIG_ATTR_CLASS);
                if (mApkPath != null) {
                    classLoader = new DexClassLoader(mApkPath, dexPath.getAbsolutePath(), null, mDLActivity.that.getClassLoader());

                    Class  mLoadClassDynamic = classLoader.loadClass(className);

                    Constructor<?> constructor = mLoadClassDynamic.getConstructor(DLBasePluginActivity.class);
                    AIWebViewBasePlugin plugin = (AIWebViewBasePlugin)constructor.newInstance(mDLActivity);
                    mWebView.addJavascriptInterface(plugin, name);
                } else {
                    AIWebViewBasePlugin plugin = (AIWebViewBasePlugin) BeanInvoker.instance(className,DLBasePluginActivity.class, mDLActivity,false);
                    mWebView.addJavascriptInterface(plugin, name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void registerPlugins(AIBaseActivity activity, WebView webView,String configFileName) {

        this.mActivity = activity;
        this.mWebView = webView;
        this.mPluginCfgFile = configFileName;
        try {
            InputStream is = mActivity.getResources().getAssets().open(mPluginCfgFile);
            WebViewPluginCfg plugincfg = WebViewPluginCfg.getInstance();
            plugincfg.parseConfig(is);

            String[] names = plugincfg.getNames();
            if (names.length > 0) mWebView.getSettings().setJavaScriptEnabled(true);
            for (String name : names) {
                String className = plugincfg.attr(name, WebViewPluginCfg.CONFIG_ATTR_CLASS);
                AIWebViewBasePlugin plugin = (AIWebViewBasePlugin) BeanInvoker.instance(className,AIBaseActivity.class, mActivity,false);
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
