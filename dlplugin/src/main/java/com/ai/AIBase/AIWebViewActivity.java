package com.ai.AIBase;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.ai.AIBase.config.GlobalCfg;
import com.ryg.dynamicload.DLBasePluginActivity;

import java.io.InputStream;

/**
 * Created by wuyoujian on 17/4/5.
 */

public class AIWebViewActivity extends DLBasePluginActivity {
    private WebView webView;
    private LinearLayout mLinearLayout;

    private String glabalCfgFile = "global.properties";
    private String pluginCfgFile = "h5Plugin.xml";


    public AIWebViewActivity(String glabalCfgFile, String pluginCfgFile) {
        this.glabalCfgFile = glabalCfgFile;
        this.pluginCfgFile = pluginCfgFile;
    }

    public AIWebViewActivity() {
        this.pluginCfgFile = "h5Plugin.xml";
        this.glabalCfgFile = "global.properties";
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setPluginEngine();
    }

    private void setPluginEngine() {
        AIWebViewPluginEngine.getInstance().registerPlugins(this,webView,pluginCfgFile);
    }

    private void initView() {

        this.mLinearLayout = new LinearLayout(that);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        this.mLinearLayout.setLayoutParams(params);
        this.mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        this.webView = new WebView(that);
        this.webView.setLayoutParams(params);
        this.webView.setWebViewClient(new AIWebViewClient());
        //

        try {
            InputStream is = this.getResources().getAssets().open(glabalCfgFile);
            GlobalCfg globalCfg = GlobalCfg.getInstance();
            globalCfg.parseConfig(is);

            String url = GlobalCfg.getInstance().attr(GlobalCfg.CONFIG_FIELD_ONLINEADDR);
            this.webView.loadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        this.mLinearLayout.addView(this.webView,tvParams);
        that.setContentView(this.mLinearLayout);
    }
}
