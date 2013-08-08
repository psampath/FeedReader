package com.solstice.feedreader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Display the content of the feed in a webview
 * 
 * @author sampathpasupunuri
 */
public class WebActivity extends Activity {
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.weblayout);

        WebView wv = (WebView) findViewById(R.id.webview);        

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = getIntent().getStringExtra("content");

        wv.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }
}
