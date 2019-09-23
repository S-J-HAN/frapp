package com.itproject.frapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("WAKAKAKAKA" + Integer.toString(i));
                goToArtifact(artifacts.get(i).getID());
            }
        });
        Glide.with(context)
                .load(artifacts.get(i).getUrl())
                .centerCrop()
                .into(viewHolder.galleryButton);
    }

    @Override
    public int getItemCount() {
        return artifacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        ImageView image;
//        Button galleryButton;
        ImageButton galleryButton;

        public ViewHolder(View view) {
            super(view);
//            image = view.findViewById(R.id.imageView);
            galleryButton = view.findViewById(R.id.galleryButton);
        }
    }

    public void goToArtifact(String id) {
        Intent intent = new Intent(context, ArtifactActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ARTIFACT_ID", id);
        context.startActivity(intent);
    }
}
