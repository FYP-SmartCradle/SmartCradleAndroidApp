package com.example.smartcradleandroidapp.user_interfaces.settings;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.service.CradleService;

public class MainSettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "MainSettingsFragment";
    EditTextPreference serverIpAddressPreference;
    SwitchPreferenceCompat themeSwitchPreference;
    SwitchPreferenceCompat serviceSwitchPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main_settings, rootKey);

        initiatePreferencesByKey();

        //Flask Rest api service handler
        getServerIpAddressFromStored();
        storeServerIpAddress();

        //Handle Themes
        getThemeFromStored();
        storeThemeConfiguration();

        //Handle the background Cradle Service Manually.
        handleCradleService();
        setServicesStatus();

    }


    void initiatePreferencesByKey() {
        this.serverIpAddressPreference = findPreference("server_ip_address");
        this.themeSwitchPreference = findPreference(getResources().getString(R.string.pref_key_theme));
        this.serviceSwitchPreference = findPreference(getResources().getString(R.string.pref_key_services));
    }


    void storeServerIpAddress() {
        this.serverIpAddressPreference.setOnPreferenceChangeListener((preference, value) -> {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getResources().getString(R.string.saved_server_ip_address), value.toString());
            editor.apply();
            getServerIpAddressFromStored();
            return true;
        });
    }

    void storeThemeConfiguration() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        this.themeSwitchPreference.setOnPreferenceChangeListener((preference, value) -> {

            if (value.toString().equalsIgnoreCase("true")) {
                editor.putString(getResources().getString(R.string.saved_theme),
                        getResources().getString(R.string.saved_dark_theme));
                editor.apply();
                themeSwitchPreference.setIcon(R.drawable.ic_light_theme);
            } else {
                editor.putString(getResources().getString(R.string.saved_theme),
                        getResources().getString(R.string.saved_light_theme));
                editor.apply();
                themeSwitchPreference.setIcon(R.drawable.ic_dark_theme);
            }
            getThemeFromStored();
            return true;
        });
    }

    void getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "0.0.0.0");
        System.out.println("saved ip address " + saved_ip_address);
        serverIpAddressPreference.setSummary(saved_ip_address);
    }

    void getThemeFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String savedTheme = getResources().getString(R.string.saved_theme);
        savedTheme = sharedPref.getString(savedTheme, getResources().getString(R.string.saved_light_theme));
        System.out.println("saved theme on settings preference " + savedTheme);
        if (savedTheme.equalsIgnoreCase(getResources().getString(R.string.saved_light_theme))) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            themeSwitchPreference.setChecked(false);
        } else {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            themeSwitchPreference.setChecked(true);
        }
    }


    void setServicesStatus() {
        serviceSwitchPreference.setChecked(isCradleServiceRunning(CradleService.class));
    }

    private boolean isCradleServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (CradleService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    void handleCradleService() {
        serviceSwitchPreference.setOnPreferenceChangeListener(((preference, newValue) -> {
            if ((boolean) newValue) {
                startCradleForegroundService();
            } else {
                stopCradleForegroundService();
            }
            return true;
        }));
    }

    public void startCradleForegroundService() {
        String input = "This is Cradle Service integration";
        Log.v(TAG, "Service Started from home");

        Intent serviceIntent = new Intent(getContext(), CradleService.class);
        serviceIntent.putExtra("sendWithKey", input);
        ContextCompat.startForegroundService(getContext(), serviceIntent);
        Log.v(TAG, "Service Started intentService");

    }

    public void stopCradleForegroundService() {
        Intent serviceIntent = new Intent(getContext(), CradleService.class);
        getActivity().stopService(serviceIntent);
        Log.v(TAG, "Service Stopped from home");
    }


}
