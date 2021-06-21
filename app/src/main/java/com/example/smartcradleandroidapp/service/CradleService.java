package com.example.smartcradleandroidapp.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.example.smartcradleandroidapp.MainActivity;
import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.user_interfaces.alert.AlertIndicatorActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class CradleService extends Service {

    private static final String TAG = "CradleService";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenceVoicePrediction = database.getReference("voice_prediction");

    private ServiceHandler serviceHandler;
    private TextToSpeech textToSpeech;

    @Override
    public void onCreate() {
        //only one time. when there service starts first time. only once in the lifecycle
        this.myRefOnClickListener();
        super.onCreate();

        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

        initiateTextToSpeech();
        // Get the HandlerThread's Looper and use it for our Handler
        Looper serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //will be triggered when we start service
        Log.v(TAG, "Service Started in onStartCommand");

        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, "ServiceNotification")
                .setContentTitle("Service")
                .setContentText("Foreground Service")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);


        return START_NOT_STICKY;
    }

    private void myRefOnClickListener() {
        // Read from the database
        referenceVoicePrediction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Firebase value Value is: " + value);

                assert value != null;
                if (value.equalsIgnoreCase("cry")) {

                    System.out.println("baby crying found");

                    Message msg = serviceHandler.obtainMessage();
                    msg.arg1 = 10041996;
                    msg.arg2 = 0;

                    serviceHandler.sendMessage(msg);

                    startAlertActivity();
                    //playRingtoneSound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void playRingtoneSound() {
        try {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
            ringtone.play();
            Thread.sleep(5000);
            ringtone.stop();
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    public void playAssistantSound(String msg) {

        String userName = getUsernameFromStored();
        String babyName = getBabyNameFromStored();

        StringBuilder speechBuilder = new StringBuilder();
        speechBuilder.append("Hey! ").append(userName).append(", ").append(babyName).append("is now ").append(msg);

        textToSpeech.speak(speechBuilder.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void startAlertActivity() {
        Intent intent = new Intent(this, AlertIndicatorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    private String getUsernameFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String saved_username = getResources().getString(R.string.saved_username);
        saved_username = sharedPref.getString(saved_username, "Man");
        return saved_username;
    }

    private String getBabyNameFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String saved_baby_name = getResources().getString(R.string.saved_baby_name);
        saved_baby_name = sharedPref.getString(saved_baby_name, "Baby");
        return saved_baby_name;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            int alertTrigger = msg.arg2;

            if (alertTrigger == 0) {
                playAssistantSound("cry");
            }
            if (alertTrigger == 1) {
                playRingtoneSound();
            }
            if (alertTrigger == 2) {
                playAssistantSound("Hello");
            }
            stopSelf(msg.arg1);
        }
    }
}
