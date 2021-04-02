package com.example.smartcradleandroidapp.user_interfaces.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.smartcradleandroidapp.R;

public class MainSettingsFragment extends PreferenceFragmentCompat {

    EditTextPreference serverIpAddressPreference;
    SwitchPreferenceCompat themeSwitchPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main_settings, rootKey);

        initiatePreferencesByKey();

        getServerIpAddressFromStored();

        storeServerIpAddress();
        getThemeFromStored();
        storeThemeConfiguration();

    }


    void initiatePreferencesByKey() {
        this.serverIpAddressPreference = findPreference("server_ip_address");
        this.themeSwitchPreference = findPreference(getResources().getString(R.string.pref_key_theme));
    }


    void storeServerIpAddress() {
        this.serverIpAddressPreference.setOnPreferenceChangeListener((preference, value) -> {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
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

    String getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "");
        System.out.println("saved ip address " + saved_ip_address);
        serverIpAddressPreference.setSummary(saved_ip_address);
        return saved_ip_address;
    }

    void getThemeFromStored() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedTheme = getResources().getString(R.string.saved_theme);
        savedTheme = sharedPref.getString(savedTheme, getResources().getString(R.string.saved_light_theme));
        System.out.println("saved theme on settings preferece " + savedTheme);
        if (savedTheme.equalsIgnoreCase(getResources().getString(R.string.saved_light_theme))) {
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            themeSwitchPreference.setChecked(false);
        } else {
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            themeSwitchPreference.setChecked(true);
        }
    }

}
