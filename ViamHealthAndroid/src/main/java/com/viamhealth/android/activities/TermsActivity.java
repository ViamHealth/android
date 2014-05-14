package com.viamhealth.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by kunal on 11/2/14.
 */
public class TermsActivity extends BaseActivity {

    private WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        Toast.makeText(activity, "Loading...", Toast.LENGTH_SHORT).show();

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            public void onPageFinished(WebView view, String url) {
                //Inject javascript code to the url given
                //Not display the element
                view.loadUrl("javascript:(function(){" + "document.getElementById('masthead').style.display ='none';" + "})()");
            }
        });

        webview.loadUrl("http://viamhealth.com/terms");

        setContentView(webview);
    }

}
