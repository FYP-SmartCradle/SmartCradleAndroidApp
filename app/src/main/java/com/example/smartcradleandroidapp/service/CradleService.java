package com.example.smartcradleandroidapp.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.smartcradleandroidapp.user_interfaces.home.HomeActivity;
import com.example.smartcradleandroidapp.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CradleService extends Service {

    private static final String TAG = "CradleService";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    @Override
    public void onCreate() {
        //only one time. when there service starts first time. only once in the lifecycle
        this.myRefOnClickListener();
        super.onCreate();
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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Firebase value Value is: " + value);

                Log.d(TAG, "This is called at service level");

                // TODO : i have to enable this for open alert activity and play ringtone
                if(value.equalsIgnoreCase("baby crying")){
                    playRingtoneSound();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void playRingtoneSound() {
        Context context = this;
        new Thread(() -> {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
            ringtone.play();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ringtone.stop();
        }).start();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
}
