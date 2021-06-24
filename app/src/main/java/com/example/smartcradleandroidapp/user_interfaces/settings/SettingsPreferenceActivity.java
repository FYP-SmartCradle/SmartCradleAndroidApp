package com.example.smartcradleandroidapp.user_interfaces.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.user_interfaces.home.HomeActivity;


public class SettingsPreferenceActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings_preference);

        Toolbar toolbar = findViewById(R.id.appBarSettings);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intentMainActivity = new Intent(this, HomeActivity.class);
            startActivity(intentMainActivity);
            finish();
        });


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_container,
                        new MainSettingsFragment()).commit();
    }


    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }
}