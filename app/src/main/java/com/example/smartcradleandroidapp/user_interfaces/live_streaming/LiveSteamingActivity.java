package com.example.smartcradleandroidapp.user_interfaces.live_streaming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.user_interfaces.home.HomeActivity;
import com.google.android.material.button.MaterialButton;

public class LiveSteamingActivity extends AppCompatActivity {
    final static String TAG = "LiveSteamingActivity";

    WebView myWebView;
    MaterialButton buttonCaptureLiveStreaming;
    String savedServerIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_steaming);

        /*ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Live Streaming");*/

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intentMainActivity = new Intent(this, HomeActivity.class);
            startActivity(intentMainActivity);
            finish();
        });


        myWebView = findViewById(R.id.webview);
        buttonCaptureLiveStreaming = findViewById(R.id.btn_live_stream_capture);

        getServerIpAddressFromStored();
        loadWebViewContent();
        listenButtonCaptureLiveStreaming();


    }

    private void listenButtonCaptureLiveStreaming() {
        buttonCaptureLiveStreaming.setOnClickListener(click -> {
            captureImage();
        });
    }

    public void reloadWebView(View view) {
        String url = "http://" + savedServerIpAddress + ":5000/video";
        myWebView.loadUrl(url);
    }


    void getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "0.0.0.0");
        savedServerIpAddress = saved_ip_address;
        Log.d(TAG, savedServerIpAddress);
    }

    void loadWebViewContent() {
        try {
            if (savedServerIpAddress != null) {
                String url = "http://" + savedServerIpAddress + ":5000/video";
                myWebView.loadUrl(url);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    private void captureImage() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + savedServerIpAddress + ":5000/api/save-image";
        System.out.println(url);
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    },
                    error -> System.out.println(error.toString()));
            queue.add(stringRequest);
        } catch (Exception e) {
            Log.v(TAG, "Error on requesting url" + url);
        }
    }


}