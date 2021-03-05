package com.example.smartcradleandroidapp.user_interfaces.settings;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.service.CradleService;
import com.google.android.material.switchmaterial.SwitchMaterial;


public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private SwitchMaterial switchTheme, serviceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        updateSwitchState();

//        Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create())
//                .build();
    }

    private void updateSwitchState() {
        serviceSwitch = findViewById(R.id.service_switch);
        switchTheme = findViewById(R.id.theme_switch);
        if (isDarkThemeActivated()) {
            switchTheme.setChecked(true);
        } else {
            switchTheme.setChecked(false);
        }

        if (isCradleServiceRunning(CradleService.class)) {
            serviceSwitch.setChecked(true);
        } else {
            serviceSwitch.setChecked(false);
        }

    }


    public void startCradleForegroundService() {
        String input = "This is Cradle Service integration";
        Log.v(TAG, "Service Started from home");

        Intent serviceIntent = new Intent(this, CradleService.class);
        serviceIntent.putExtra("inputExtra", input);
        ContextCompat.startForegroundService(this, serviceIntent);
        Log.v(TAG, "Service Started intentService");

    }

    public void stopCradleForegroundService() {
        Intent serviceIntent = new Intent(this, CradleService.class);
        stopService(serviceIntent);
        Log.v(TAG, "Service Stopped from home");
    }

    public void lookUpServices(View view) {
        if (serviceSwitch.isChecked()) {
            startCradleForegroundService();
        } else {
            stopCradleForegroundService();
        }
    }

    public void switchTheme(View view) {
        if (switchTheme.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    private boolean isCradleServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isDarkThemeActivated() {
        return AppCompatDelegate.MODE_NIGHT_YES == AppCompatDelegate.getDefaultNightMode();
    }

    public void ledOnOff(View view) {
        SwitchMaterial ledSwitch = findViewById(R.id.led_blink);
        if (ledSwitch.isChecked()) {
            requestForLed("on");
        } else {
            requestForLed("off");
        }
    }

    private void requestForLed(String status) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.100:5000/led/" + status;

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> System.out.println(response),
                    error -> System.out.println(error.toString()));

            queue.add(stringRequest);
        } catch (Exception e) {
            Log.v(TAG, "Error on requesting url" + url);
        }

    }
}