package com.example.smartcradleandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.smartcradleandroidapp.service.CradleService;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void startService(View view){
        String input = "This is Cradle Service integration";
        Log.v(TAG,"Service Started from home");

        Intent serviceIntent = new Intent(this, CradleService.class);
        serviceIntent.putExtra("inputExtra",input);
        ContextCompat.startForegroundService(this, serviceIntent);
        Log.v(TAG,"Service Started intentService");

    }

    public void stopService(View view){
        Intent serviceIntent = new Intent(this, CradleService.class);
        stopService(serviceIntent);
        Log.v(TAG,"Service Stopped from home");
    }

}