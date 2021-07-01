package com.example.smartcradleandroidapp.user_interfaces.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartcradleandroidapp.R;
import com.example.smartcradleandroidapp.model.ImageStore;

import java.util.List;

public class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewAdapter.ViewHolder> {

    List<ImageStore> imageFileNameList;
    String imageStoredUrl;
    View view;
    Context context;


    public GalleryViewAdapter(Context context, List<ImageStore> imageFileNameList, String imageStoredUrl) {
        this.context = context;
        this.imageFileNameList = imageFileNameList;
        this.imageStoredUrl = imageStoredUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_single_image_item, parent, false);
        return new ViewHolder(this.view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = Uri.parse(imageStoredUrl + imageFileNameList.get(position).getFileName());
        Glide.with(view.getContext()).load(uri).into(holder.imageView);

        holder.textViewImageLabel.setText(imageFileNameList.get(position).getLabelFound());

        holder.itemView.setOnClickListener(v -> {
            DialogFragment newFragment = new ImageViewDialogFragment(uri, imageFileNameList.get(position));
            FragmentManager fm = ((HomeActivity) context).getSupportFragmentManager();
            newFragment.show(fm, "");
        });
    }

    @Override
    public int getItemCount() {
        return imageFileNameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View itemView;
        TextView textViewImageLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.imageViewGalleryViewImage);
            textViewImageLabel = itemView.findViewById(R.id.textViewImageLabel);

        }

    }
}
