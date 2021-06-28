package com.example.smartcradleandroidapp.user_interfaces.alert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.user_interfaces.assistent.VoiceAssistantActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AlertIndicatorActivity extends AppCompatActivity {
    private final static String TAG = AlertIndicatorActivity.class.getCanonicalName();
    ImageView imageView;
    String savedServerIpAddress;
    WebView webView;
    MaterialButton btnWebView;
    MaterialButton btnVoiceAssistant;

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;

    private Intent intent;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_indicator);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        imageView = findViewById(R.id.imageViewBellView);
        webView = findViewById(R.id.webViewLiveStreamingAlertActivity);
        btnWebView  = findViewById(R.id.btn_reload_live_streaming);
        btnVoiceAssistant  = findViewById(R.id.btn_voice_assistant_alert);

        getServerIpAddressFromStored();
        requestForPermissions();
        initiateSpeechRecognizer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.queue = Volley.newRequestQueue(this);
    }

    public void reloadWebView(View view) {
        if (webView.getVisibility() == View.VISIBLE) {
            webView.setVisibility(View.INVISIBLE);
            btnWebView.setIconResource(R.drawable.ic_remove_red_eye);
        } else {
            webView.setVisibility(View.VISIBLE);
            btnWebView.setIconResource(R.drawable.ic_visibility_off);
            try {
                if (savedServerIpAddress != null) {
                    String url = "http://" + savedServerIpAddress + ":5000/video";
                    webView.loadUrl(url);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

    }


    private void getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "0.0.0.0");
        savedServerIpAddress = saved_ip_address;
        Log.d(TAG, savedServerIpAddress);
    }


    public void turnVoiceRecognitionOn(View view) {
        speechRecognizer.startListening(intent);
    }

    private void requestForPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
    }


    private void initiateSpeechRecognizer() {
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 12);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.e("SR :: ", "On Ready for Speech");

                // speechRecognizer.startListening(intent);
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string;
                Log.e("TTS :: ", "start recognizing top");
                if (matches != null) {
                    string = matches.get(0);
                    // Toast.makeText(AlertIndicatorActivity.this, string, Toast.LENGTH_LONG).show();
                    System.out.println(matches.get(0));

                    if (string.equals("can you turn on the light")) {
                        turnOnLight("on");

                    }

                    if (string.equals("can you turn off the light")) {
                        turnOnLight("off");

                    }
                } else {
                    Log.e("TTS :: ", "start recognizing end");

                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


    }


    private void turnOnLight(String status) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder
                .append("http://")
                .append(savedServerIpAddress)
                .append(":5000/api/arduino/led/").append(status);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(),
                response -> {}, error -> Log.d("", error.toString()));

        queue.add(stringRequest);
    }
}