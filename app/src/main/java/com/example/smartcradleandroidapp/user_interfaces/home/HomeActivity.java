package com.example.smartcradleandroidapp.user_interfaces.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.user_interfaces.alert.AlertIndicatorActivity;
import com.example.smartcradleandroidapp.user_interfaces.assistent.VoiceAssistantActivity;
import com.example.smartcradleandroidapp.user_interfaces.live_streaming.LiveSteamingActivity;
import com.example.smartcradleandroidapp.user_interfaces.settings.SettingsPreferenceActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    /**
     * TODO : Turn on fan (not yet) (simple)
     * TODO : Temperate api (not yet) --> dynamic rendering (thread...) (intermediate)
     * TODO : Life streaming not add here (not yet) (simple)
     * <p>
     * TODO : Uploading images from mobile app of the authorized people. (we have to send to the server-flask)(not recommended)
     * (retrofit / volley are the rest api libraries) // (firebase)
     * TODO : (not yet) // else we have to capture from the camera, (recommended) (from the mobile phone trigger the frame.)
     * TODO : Voice assistant -- (turn on fan, how is my baby (it is sleeping , temperature okay) )
     * <p>
     * TODO : Analytical part (show pie chart or line graph about their activity - we need posture data, cry data ) (not yet)
     * TODO : TURN THE FAN OR CAMERA ANGLE SHOULD BE DONE FROM THE MOBILE APP. (NICE THING) (POSSIBLE) (RESET IT)
     * static -- nothing, shaking then say shaking (no need to explore different movements.)
     * <p>
     * TODO : Trigger notification. (making calling done partially -- main requirement) (cry , bad posture) -- receiver mother.
     * TODO : Gallery view  , the media server config has been done on flask so, we need to do the galary view on the app by taking image url in pagination.
     */


    private static final String TAG = "HomeActivity";
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );


        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        startHomeFragmentFirst(savedInstanceState);
    }


    private void startHomeFragmentFirst(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_fragment_container, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressLint({"NonConstantResourceId", "ResourceType"})
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.v(TAG, "On Navigation Click" + item.getTitle());
        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_fragment_container, new HomeFragment())
                        .commit();
                navigationView.setCheckedItem(R.id.nav_home);
                break;

            case R.id.nav_live_streaming:
                Intent liveStreamingIntent = new Intent(this, LiveSteamingActivity.class);
                startActivity(liveStreamingIntent);
                navigationView.setCheckedItem(R.id.nav_live_streaming);
                break;


            case R.id.nav_electronics:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_fragment_container, new HomeApplianceFragment())
                        .commit();
                navigationView.setCheckedItem(R.id.nav_electronics);
                break;

            case R.id.nav_ambient:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_fragment_container, new AmbientFragment())
                        .commit();
                navigationView.setCheckedItem(R.id.nav_ambient);
                break;

            case R.id.nav_voice_assistant:
                Intent intentVoiceAssistant = new Intent(this, VoiceAssistantActivity.class);
                startActivity(intentVoiceAssistant);
                navigationView.setCheckedItem(R.id.nav_voice_assistant);
                break;

            case R.id.nav_setting:
                Intent intentSettings = new Intent(this, SettingsPreferenceActivity.class);
                startActivity(intentSettings);
                navigationView.setCheckedItem(R.id.nav_setting);
                break;

            case R.id.nav_test:
                Intent intentTest = new Intent(this, AlertIndicatorActivity.class);
                startActivity(intentTest);
                navigationView.setCheckedItem(R.id.nav_test);
                break;

            case R.id.nav_shut_down:
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
                materialAlertDialogBuilder
                        .setTitle("Do you want to turn off")
                        .setMessage("Turning of this will shutdown the system fully")
                        .setNeutralButton("Cancel", (dialog, which) -> {
                            //TODO : DO NOTHING AND CLOSES THE DIALOG
                        }).setPositiveButton("Turn Off", (dialog, which) -> {
                    // TODO : SHUTDOWN THE SERVER BY RUNNING sudo shutdown;
                }).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}