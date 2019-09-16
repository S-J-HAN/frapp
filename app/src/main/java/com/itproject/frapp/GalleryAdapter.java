package com.itproject.frapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<Artifact> artifacts;
    private Context context;

    public GalleryAdapter(Context context, ArrayList<Artifact> artifacts) {
        this.context = context;
        this.artifacts = artifacts;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Glide.with(context)
                .load(artifacts.get(i).getUrl())
                .centerCrop()
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return artifacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.imageView);
        }
    }
}
