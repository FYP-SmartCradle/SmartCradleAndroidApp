package com.example.smartcradleandroidapp.user_interfaces.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.smartcradleandroidapp.R;

public class ImageViewDialogFragment extends DialogFragment {

    Uri imageUri;
    View view;

    public ImageViewDialogFragment() {
    }

    public ImageViewDialogFragment(Uri uri) {
        this.imageUri = uri;
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
        System.out.println("$$$$$$$   -- " + imageUri);
        Glide
                .with(this.view)
                .load(imageUri)
                .into(imageView);

    }
}
