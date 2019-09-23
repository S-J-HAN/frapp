package com.itproject.frapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;


public class ArtifactAdapter extends RecyclerView.Adapter<ArtifactAdapter.ViewHolder> {
    private Artifact artifact;
    private String artifactID;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView op, dateTime, text;
        public TextView date, description, tags;
        public EditText newComment;
        public Button postComment, back;
        public ImageView image;


        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == 0) {
                this.image = (ImageView) view.findViewById(R.id.imageView_artifact);
                this.date = (TextView) view.findViewById(R.id.textView_date);
                this.description = (TextView) view.findViewById(R.id.textView_description);
                this.tags = (TextView) view.findViewById(R.id.textView_tags);
                this.newComment = (EditText) view.findViewById(R.id.editText_newComment);
                this.postComment = (Button) view.findViewById(R.id.button_postComment);
                this.back = (Button) view.findViewById(R.id.button_back);
            } else {
                this.op = (TextView) view.findViewById(R.id.textView_op);
                this.dateTime = (TextView) view.findViewById(R.id.textView_dateTime);
                this.text = (TextView) view.findViewById(R.id.textView_text);
            }
        }
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return position;
        }
    }

    public ArtifactAdapter(Artifact artifact, String artifactID, FirebaseUser currentUser, DatabaseReference dbRef, Context context) {
        this.artifact = artifact;
        this.artifactID = artifactID;
        this.currentUser = currentUser;
        this.dbRef = dbRef;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.artifact, parent, false);
            return new ViewHolder(itemView, viewType);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
            return new ViewHolder(itemView, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == 0) {
            Glide.with(context)
                    .load(artifact.getUrl())
                    .centerCrop()
                    .into(holder.image);
            holder.date.setText(artifact.getDate());
            holder.description.setText(artifact.getDescription());
            holder.tags.setText(artifact.getTags());

            holder.postComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Date currentTime = Calendar.getInstance().getTime();
                    String dateText = currentTime.toString();
                    String newCommentText = holder.newComment.getText().toString();


                    Comment comment = new Comment();
                    comment.setOp(currentUser.getUid());
                    System.out.println(currentUser.getDisplayName() + "test");

                    comment.setDateTime(dateText);
                    comment.setText(newCommentText);
                    System.out.println(comment.toString() + "hello");
                    dbRef.child("artifacts").child(artifactID).child("comments").push().setValue(comment);
                }
            });


            holder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goBack();
                }
            });

        } else {
            Comment comment = artifact.getComments().get(position - 1);
            holder.dateTime.setText(comment.getDateTime());
            holder.op.setText(comment.getOp());
            holder.text.setText(comment.getText());

            dbRef.child("users").child(comment.getOp()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    holder.op.setText(user.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Something went wrong...
                    System.out.println("Error retrieving user data in comment");
                    holder.op.setText("Error");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return artifact.getComments().size() + 1;
    }

    public void goBack() {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);

//        ((Activity)context).onBackPressed();
//        ((Activity)context).finish();
//        ((Activity)context).finishActivity(0);
    }

}