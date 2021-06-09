package com.example.smartcradleandroidapp.user_interfaces.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ambient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTemperature = view.findViewById(R.id.textViewTemperatureUpdate);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.queue = Volley.newRequestQueue(requireContext());
        getTemperatureData();
    }

    void getTemperatureData() {
        this.thread = new Thread(() -> {
            while (true) {
                String url = "http://192.168.1.100:5000/api/arduino/get_temperature";
                @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                double temperature = (double) jsonObject.get("temperature");
                                System.out.println(response);
                                textViewTemperature.setText(Math.round(temperature) +"°C");
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
}