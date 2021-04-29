package com.example.smartcradleandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.smartcradleandroidapp.user_interfaces.home.HomeActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        getThemeFromStored();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }

    void getThemeFromStored() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String savedTheme = getResources().getString(R.string.saved_theme);
        savedTheme = sharedPref.getString(savedTheme, getResources().getString(R.string.saved_light_theme));
        if (savedTheme.equalsIgnoreCase(getResources().getString(R.string.saved_light_theme))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}