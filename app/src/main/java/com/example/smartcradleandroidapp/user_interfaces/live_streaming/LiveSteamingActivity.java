package com.example.smartcradleandroidapp.user_interfaces.live_streaming;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcradleandroidapp.R;
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


        myWebView = findViewById(R.id.webview);
        buttonCaptureLiveStreaming = findViewById(R.id.btn_live_stream_capture);

        getServerIpAddressFromStored();
        loadWebViewContent();
        listenButtonCaptureLiveStreaming();

    }

    private void listenButtonCaptureLiveStreaming() {
        buttonCaptureLiveStreaming.setOnClickListener(click -> {
            System.out.println(click.toString());
            //TODO : CAPTURE THE CLIP..
        });
    }

    public void reloadWebView(View view) {
        myWebView.loadUrl("http://192.168.1.100:5000/video");
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
                String url = "http://" + savedServerIpAddress + "/video";
                myWebView.loadUrl(url);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void captureView(View view) {
        captureImage();
    }

    private void captureImage() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + savedServerIpAddress + "/captureImage";

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    System.out::println,
                    error -> System.out.println(error.toString()));
            queue.add(stringRequest);
        } catch (Exception e) {
            Log.v(TAG, "Error on requesting url" + url);
        }
    }


}