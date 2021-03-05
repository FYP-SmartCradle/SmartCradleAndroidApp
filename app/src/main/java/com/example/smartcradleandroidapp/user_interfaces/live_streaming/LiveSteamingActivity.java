package com.example.smartcradleandroidapp.user_interfaces.live_streaming;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcradleandroidapp.R;

public class LiveSteamingActivity extends AppCompatActivity {
    final static String TAG = "LiveSteamingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_steaming);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        try {
            myWebView.loadUrl("https://www.google.com/");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}