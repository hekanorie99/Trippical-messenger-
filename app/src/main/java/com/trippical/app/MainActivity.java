package com.trippical.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.*;

public class MainActivity extends Activity {
    WebView web;
    ValueCallback<Uri[]> picker;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);
        web = new WebView(this);
        web.setBackgroundColor(Color.BLACK);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView w, ValueCallback<Uri[]> cb, FileChooserParams p) {
                if (picker != null) picker.onReceiveValue(null);
                picker = cb;
                try { startActivityForResult(p.createIntent(), 1); }
                catch (Exception e) { picker = null; return false; }
                return true;
            }
        });
        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    @Override protected void onActivityResult(int req, int res, Intent data) {
        if (req == 1 && picker != null) {
            picker.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res, data));
            picker = null;
        }
    }

    @Override public void onBackPressed() {
        web.evaluateJavascript("window.goBack&&window.goBack()", r -> {
            if (!"true".equals(r)) finish();
        });
    }
  }
