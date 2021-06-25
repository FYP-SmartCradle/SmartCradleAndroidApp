package com.example.smartcradleandroidapp.user_interfaces.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.model.ImageStore;

public class ImageViewDialogFragment extends DialogFragment {

    Uri imageUri;
    View view;
    ImageStore imageStore;

    public ImageViewDialogFragment() {
    }

    public ImageViewDialogFragment(Uri uri, ImageStore imageStore) {
        this.imageUri = uri;
        this.imageStore = imageStore;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_image_view_dialog, container, false);
        return this.view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView imageView = this.view.findViewById(R.id.image_view_single_image_dialog);
        TextView textViewImageTakenDate = this.view.findViewById(R.id.textViewImageTakenDate);
        TextView textViewImageLabel = this.view.findViewById(R.id.textViewImageLabel);

        Glide
                .with(this.view)
                .load(imageUri)
                .into(imageView);

        textViewImageTakenDate.setText(imageStore.getDateLabel());
        textViewImageLabel.setText(imageStore.getLabelFound());

    }
}
