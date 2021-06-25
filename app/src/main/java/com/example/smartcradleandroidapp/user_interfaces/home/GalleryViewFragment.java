package com.example.smartcradleandroidapp.user_interfaces.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.model.ImageStore;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class GalleryViewFragment extends Fragment {

    String savedServerIpAddress;
    String imageStoredUrl;
    ExtendedFloatingActionButton floatingActionButton;
    List<ImageStore> imageFileNameList = new ArrayList<>();
    Calendar calendar;
    private RecyclerView mRecyclerView;
    private GalleryViewAdapter mAdapter;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view_gallery);
        floatingActionButton = view.findViewById(R.id.floatingButtonDateRangeGalleryView);
        this.view = view;

    }

    @Override
    public void onStart() {
        super.onStart();
        getServerIpAddressFromStored();
        generateImageStoredUrl();

        calendar = Calendar.getInstance(TimeZone.getDefault());
        getImageListFromServer(calendar);

        listenFloatingButton();
    }

    private void listenFloatingButton() {
        floatingActionButton.setOnClickListener(v -> {

            MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date").build();

            datePicker.addOnPositiveButtonClickListener(view_date -> {
                assert datePicker.getSelection() != null;
                calendar.setTimeInMillis((Long) datePicker.getSelection());
                getImageListFromServer(calendar);
            });

            datePicker.show(((HomeActivity) view.getContext()).getSupportFragmentManager(), "date_picker");

        });
    }


    private void getImageListFromServer(Calendar calendar) {
        Date date = new Date(calendar.getTimeInMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String dateStr = dateFormat.format(date);

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder
                .append("http://")
                .append(savedServerIpAddress)
                .append(":5000/api/get-image-list-by-date/")
                .append(dateStr);

        System.out.println(urlBuilder.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlBuilder.toString(),
                response -> {
                    try {

                        imageFileNameList = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            imageFileNameList.add(new ImageStore(o.getString("date_label"),
                                    o.getString("file_name"),o.getString("label_found")));
                        }

                        mAdapter = new GalleryViewAdapter(view.getContext(), imageFileNameList, imageStoredUrl);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    System.out.println(error.toString());
                });

        queue.add(stringRequest);
    }


    private void generateImageStoredUrl() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("http://")
                .append(savedServerIpAddress)
                .append(":5000/api/media-file/image/");

        imageStoredUrl = stringBuilder.toString();
    }

    private void getServerIpAddressFromStored() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        String saved_ip_address = getResources().getString(R.string.saved_server_ip_address);
        saved_ip_address = sharedPref.getString(saved_ip_address, "0.0.0.0");
        savedServerIpAddress = saved_ip_address;
    }
}