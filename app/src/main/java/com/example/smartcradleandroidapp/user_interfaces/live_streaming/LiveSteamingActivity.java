package com.example.smartcradleandroidapp.user_interfaces.live_streaming;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcradleandroidapp.R;

public class LiveSteamingActivity extends AppCompatActivity {
    final static String TAG = "LiveSteamingActivity";

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_steaming);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        myWebView = (WebView) findViewById(R.id.webview);
        try {
            myWebView.loadUrl("http://192.168.1.100:5000/video");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void reloadWebView(View view) {
        myWebView.loadUrl("http://192.168.1.100:5000/video");
    }
}