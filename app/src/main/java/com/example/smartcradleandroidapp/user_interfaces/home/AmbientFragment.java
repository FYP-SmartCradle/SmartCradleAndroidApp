package com.example.smartcradleandroidapp.user_interfaces.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcradleandroidapp.R;

import org.json.JSONException;
import org.json.JSONObject;


public class AmbientFragment extends Fragment {

    Thread thread;
    TextView textViewTemperature;
    TextView textViewWet;

    RequestQueue queue;

    String savedServerIpAddress;
    boolean temperatureListenStatus = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ambient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTemperature = view.findViewById(R.id.textViewTemperatureUpdate);
        textViewWet = view.findViewById(R.id.textViewWetUpdate);
    }

    @Override
    public void onStart() {
        super.onStart();
        getServerIpAddressFromStored();
        this.queue = Volley.newRequestQueue(requireContext());
        getTemperatureData();
    }

    @Override
    public void onResume() {
        super.onResume();
        temperatureListenStatus = true;
    }


    void getTemperatureData() {
        this.thread = new Thread(() -> {
            while (temperatureListenStatus) {

                //0.0.0.0
                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder
                        .append("http://")
                        .append(savedServerIpAddress)
                        .append(":5000/api/arduino/get_temperature");


                @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(),
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                double temperature = (double) jsonObject.get("temperature");
                                System.out.println(response);
                                textViewTemperature.setText(Math.round(temperature) + "°C");
                                textViewWet.setText(String.valueOf(Math.round(temperature) / 3));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> Log.d("", error.toString()));

                queue.add(stringRequest);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        this.thread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        temperatureListenStatus = false;
    }

    private void getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "0.0.0.0");
        savedServerIpAddress = saved_ip_address;
    }
}