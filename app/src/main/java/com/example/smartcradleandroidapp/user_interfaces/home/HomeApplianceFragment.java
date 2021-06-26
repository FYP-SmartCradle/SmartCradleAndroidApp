package com.example.smartcradleandroidapp.user_interfaces.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcradleandroidapp.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class HomeApplianceFragment extends Fragment {

    private SwitchMaterial switchMaterial;
    private String savedServerIpAddress;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home_appliance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchMaterial = view.findViewById(R.id.switchMaterialLightControl);

    }

    @Override
    public void onStart() {
        super.onStart();
        getServerIpAddressFromStored();
        this.queue = Volley.newRequestQueue(requireContext());

        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            System.out.println(isChecked);
            if (isChecked) {
                turnOnLight("on");
            } else {
                turnOnLight("off");
            }
        });
    }


    private void turnOnLight(String status) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder
                .append("http://")
                .append(savedServerIpAddress)
                .append(":5000/api/arduino/led/").append(status);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(),
                response -> {}, error -> Log.d("", error.toString()));

        queue.add(stringRequest);
    }


    private void getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "0.0.0.0");
        savedServerIpAddress = saved_ip_address;
    }
}
