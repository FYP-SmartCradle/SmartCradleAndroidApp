package com.example.smartcradleandroidapp.user_interfaces.home;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcradleandroidapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    View view;
    String savedServerIpAddress;

    TextView textViewTemperatureStatus;
    TextView textViewVoiceStatus;
    TextView textViewLyingPostureStatus;
    TextView textViewWetStatus;

    Thread thread;
    boolean latestStateListenStatus = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        textViewVoiceStatus = view.findViewById(R.id.textViewVoiceStatus);
        textViewTemperatureStatus = view.findViewById(R.id.textViewTemperatureStatus);
        textViewLyingPostureStatus = view.findViewById(R.id.textViewLyingPostureStatus);
        textViewWetStatus = view.findViewById(R.id.textViewWetStatus);

        getServerIpAddressFromStored();


    }

    @Override
    public void onResume() {
        super.onResume();
        latestStateListenStatus = true;
        getLatestStateContinuously();
        getVoiceAnalysis();
        getLyingPostureAnalysis();
    }

    @Override
    public void onPause() {
        super.onPause();
        latestStateListenStatus = false;
    }

    private void getLatestStateContinuously() {
        this.thread = new Thread(() -> {
            while (latestStateListenStatus) {
                System.out.println("listening to latest state *****");
                getLatestState();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("listening to latest state stopped ^^^^^^^");
        });

        this.thread.start();
    }

    private void getLatestState() {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder
                .append("http://")
                .append(savedServerIpAddress)
                .append(":5000/api/get-latest-states");

        System.out.println(urlBuilder.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(),
                response -> {
                    try {
                        JSONObject o = new JSONObject(response);
                        textViewVoiceStatus.setText(o.getString("sound"));
                        textViewLyingPostureStatus.setText(o.getString("lyingPosture"));
                        textViewTemperatureStatus.setText(o.getDouble("temperature") + "°C");
                        textViewWetStatus.setText(String.valueOf(o.getDouble("wet")));
                    } catch (JSONException e) {
                        latestStateListenStatus = false;
                        e.printStackTrace();
                    }
                },
                error -> {
                    System.out.println(error.toString());
                });

        queue.add(stringRequest);
    }


    private void getVoiceAnalysis() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder
                .append("http://")
                .append(savedServerIpAddress)
                .append(":5000/api/database_analysis/get_one_day_summary_for_voice/")
                .append("2021-06-25");

        System.out.println(urlBuilder.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(),
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            entries.add(new PieEntry((float) o.getDouble("hour"), o.getString("label").toUpperCase()));
                        }

                        initializeChartVoiceChart(entries);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    System.out.println(error.toString());
                });

        queue.add(stringRequest);
    }


    private void getLyingPostureAnalysis() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder
                .append("http://")
                .append(savedServerIpAddress)
                .append(":5000/api/database_analysis/get_one_day_summary_for_lying_posture/")
                .append("2021-06-25");

        System.out.println(urlBuilder.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(),
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            entries.add(new PieEntry((float) o.getDouble("hour"), o.getString("label").toUpperCase()));
                        }

                        initializeChartLyingPostureChart(entries);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    System.out.println(error.toString());
                });

        queue.add(stringRequest);
    }

    void initializeChartVoiceChart(ArrayList<PieEntry> entries) {
        PieChart chart;

        chart = view.findViewById(R.id.chartVoice);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener


        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);





       /* entries.add(new PieEntry(10f, "done"));
        entries.add(new PieEntry(70f, "done"));
        entries.add(new PieEntry(20f, "done"));
*/
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        // colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setUsingSliceColorAsValueLineColor(true);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.setDrawHoleEnabled(true);
        chart.invalidate();

    }

    void initializeChartLyingPostureChart(ArrayList<PieEntry> entries) {
        PieChart chart;

        chart = view.findViewById(R.id.chartLyingPosture);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener


        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);





       /* entries.add(new PieEntry(10f, "done"));
        entries.add(new PieEntry(70f, "done"));
        entries.add(new PieEntry(20f, "done"));
*/
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        // colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setUsingSliceColorAsValueLineColor(true);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.setDrawHoleEnabled(true);
        chart.invalidate();

    }


    void getThemeFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String savedTheme = getResources().getString(R.string.saved_theme);
        savedTheme = sharedPref.getString(savedTheme, getResources().getString(R.string.saved_light_theme));
        System.out.println("saved theme in home fragement :: " + savedTheme);
        if (savedTheme.equalsIgnoreCase(getResources().getString(R.string.saved_light_theme))) {
            System.out.println("saved theme in home true case :: " + savedTheme);
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                System.out.println("Yes it is light theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        if (savedTheme.equalsIgnoreCase(getResources().getString(R.string.saved_dark_theme))) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                System.out.println("Yes it is dark theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
    }


    private void getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "0.0.0.0");
        savedServerIpAddress = saved_ip_address;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}