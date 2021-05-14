package com.example.smartcradleandroidapp.user_interfaces.assistent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartcradleandroidapp.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class VoiceAssistantActivity extends AppCompatActivity {

    MaterialTextView voiceAssistantStateIndicatorTextView;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_assistant);
        voiceAssistantStateIndicatorTextView = findViewById(R.id.voice_assistant_state);
        requestForPermissions();
        initiateSpeechRecognizer();
    }

    void initiateTextToSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS :: ", "Language not supported");
                } else {
                    Log.d("TTS :: ", "initialisation Success.");
                }
            } else {
                Log.e("TTS :: ", "initialisation failed.");
            }
        });
    }

    private void startSpeechRecognition() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak("Hi Senduran , how can i help you?", TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak("please tell me , how can i help you?", TextToSpeech.QUEUE_FLUSH, null);
        }

        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {
        }

        speechRecognizer.startListening(intent);

    }


    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    public void turnVoiceRecognitionOn(View view) {
        startSpeechRecognition();
    }

    private void requestForPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
    }

    private void initiateSpeechRecognizer() {
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 6);
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
                    Toast.makeText(VoiceAssistantActivity.this, string, Toast.LENGTH_LONG).show();
                    System.out.println(matches.get(0));

                    if (string.equals("Hi Hello")) {
                        Log.e("TTS :: ", "start recognizing middle");

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

        initiateTextToSpeech();
    }
}