package com.example.smartcradleandroidapp;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class NetworkSettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_network_settings, rootKey);
    }
}
