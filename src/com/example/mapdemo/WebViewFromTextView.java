package com.example.mapdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFromTextView extends Activity {

    @SuppressLint("SetJavaScriptEnabled")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.web_view);
        if( savedInstanceState == null ) {
        	// RstDetail.javaからurlを受け取っている
            String url =
                getIntent().getDataString().replace("myscheme://", "http://");
            Log.e("WebViewFromTextView:", url);
            
            // WebViewの設定
        	WebView webView = (WebView)findViewById(R.id.webView);        	
        	webView.requestFocus();
        	webView.getSettings().setJavaScriptEnabled(true);
        	webView.getSettings().setBuiltInZoomControls(true);
        	webView.setWebViewClient(new WebViewClient());
        	// ここで読み込み
    		webView.loadUrl(url);

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// webviewでbackボタンが押下された時、履歴を戻れるなら戻り、戻れないなら前のActivityに戻る
        WebView webview = (WebView)findViewById(R.id.webView);
    	if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
    		webview.goBack();
    		return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
