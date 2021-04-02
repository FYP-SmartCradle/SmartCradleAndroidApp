package com.example.smartcradleandroidapp.user_interfaces.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.smartcradleandroidapp.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    void getThemeFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String savedTheme = getResources().getString(R.string.saved_theme);
        savedTheme = sharedPref.getString(savedTheme, getResources().getString(R.string.saved_light_theme));
        System.out.println("saved theme in home fragement :: " + savedTheme);
        if (savedTheme.equalsIgnoreCase(getResources().getString(R.string.saved_light_theme))) {
            System.out.println("saved theme in home true case :: " + savedTheme);
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                System.out.println("Yes it is light theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        } if (savedTheme.equalsIgnoreCase(getResources().getString(R.string.saved_dark_theme))) {
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
                System.out.println("Yes it is dark theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
    }
}