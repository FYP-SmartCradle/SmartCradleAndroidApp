package com.example.smartcradleandroidapp.user_interfaces.alert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.smartcradleandroidapp.R;

public class AlertIndicatorActivity extends AppCompatActivity {
    private final static String TAG = AlertIndicatorActivity.class.getCanonicalName();
    ImageView imageView;
    String savedServerIpAddress;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_indicator);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        imageView = findViewById(R.id.imageViewBellView);
        webView = findViewById(R.id.webViewLiveStreamingAlertActivity);

        getServerIpAddressFromStored();
    }


    public void reloadWebView(View view) {
        webView.setVisibility(View.VISIBLE);
        try {
            if (savedServerIpAddress != null) {
                String url = "http://" + savedServerIpAddress + ":5000/video";
                webView.loadUrl(url);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    void getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "0.0.0.0");
        savedServerIpAddress = saved_ip_address;
        Log.d(TAG, savedServerIpAddress);
    }
}