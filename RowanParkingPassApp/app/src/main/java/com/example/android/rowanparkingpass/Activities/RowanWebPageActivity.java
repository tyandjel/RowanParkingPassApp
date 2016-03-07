package com.example.android.rowanparkingpass.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.android.rowanparkingpass.R;

public class RowanWebPageActivity extends BaseActivity {

    private WebView webView;

    private static final String forgot_password_url = "https://id.rowan.edu/";
    private static final String change_password_url = "https://id.rowan.edu/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent currentIntent = getIntent();
        currentMode = currentIntent.getStringExtra(MODE);

        setContentView(R.layout.activity_webpage);

        webView = (WebView) findViewById(R.id.webView);

        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (currentMode.equals(mode.FORGOT_PASSWORD.name())) {
            webView.loadUrl(forgot_password_url);
        } else if (currentMode.equals(mode.CHANGE_PASSWORD.name())) {
            webView.loadUrl(change_password_url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent myIntent;

        switch (item.getItemId()) {
            // action with ID action_home was selected
            case R.id.action_home:
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                if (currentMode.equals(mode.CHANGE_PASSWORD.name())) {
                    myIntent = new Intent(this, SettingActivity.class);
                } else {
                    myIntent = new Intent(this, LoginPageActivity.class);
                }
                startActivity(myIntent);
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        // Check fi the key event was the Forward button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_FORWARD) && webView.canGoForward()) {
            webView.goForward();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
            return true;
        }
    }
}

