package com.yikang.app.yikangserver.ui;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yikang.app.yikangserver.R;

public class WebviewActivivty extends BaseActivity {

    private WebView webView;
    private String url;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_activivty);
        url = getIntent().getStringExtra("bannerUrl");
        title = getIntent().getStringExtra("bannerName");
        initTitleBar(title);
        init();

    }


    private void init() {
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        //WebView加载web资源
        showWaitingUI();
        webView.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                hideWaitingUI();
                super.onPageFinished(view, url);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);

                return true;
            }
        });
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected void getData() {

    }

    @Override
    protected void initViewContent() {

    }
}
