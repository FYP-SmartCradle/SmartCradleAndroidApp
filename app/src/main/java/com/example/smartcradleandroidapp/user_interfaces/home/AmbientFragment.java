package com.example.smartcradleandroidapp.user_interfaces.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.example.smartcradleandroidapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AmbientFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ambient, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}