package com.itproject.frapp.MainGallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itproject.frapp.ArtifactPages.ArtifactActivity;
import com.itproject.frapp.R;
import com.itproject.frapp.Schema.Artifact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    public static final int REQUEST_DELETE = 0;

    private ArrayList<Artifact> artifacts;
    private Context context;
    private Activity activity;

    public GalleryAdapter(Context context, ArrayList<Artifact> artifacts, Activity activity) {
        this.context = context;
        this.artifacts = artifacts;
        this.activity = activity;

        // Sort artifacts based on date
        this.artifacts.sort(Comparator.comparing(Artifact::getSortableDate));
        Collections.reverse(this.artifacts);
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
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ARTIFACT_ID", id);
        activity.startActivityForResult(intent, REQUEST_DELETE);
//        context.startActivity(intent);

    }
}
