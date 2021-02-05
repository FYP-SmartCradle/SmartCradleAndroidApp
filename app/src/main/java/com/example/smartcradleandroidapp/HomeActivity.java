package com.example.smartcradleandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void startService(View view){
        System.out.println("start service button works fine.");
    }

    public void stopService(View view){
        System.out.println("start service button works fine.");
    }

}