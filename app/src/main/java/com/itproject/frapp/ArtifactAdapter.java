/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Adapter class for controlling the contents of the artifact page recycler view
public class ArtifactAdapter extends RecyclerView.Adapter<ArtifactAdapter.ViewHolder> {

    private Artifact artifact;
    private String artifactID;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;
    private Context context;

    private final static int artifactViewType = 0;

    // Class for each element of the recycler view (either the artifact + details or a comment)
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView op, dateTime, text;
        private TextView date, description, tags;
        private EditText newComment;
        private Button postComment, back;
        private ImageView image;


        ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == artifactViewType) {
                this.image = view.findViewById(R.id.imageView_artifact);
                this.date = view.findViewById(R.id.textView_dateTime);
                this.description = view.findViewById(R.id.textView_description);
                this.tags = view.findViewById(R.id.textView_tags);
                this.newComment = view.findViewById(R.id.editText_newComment);
                this.postComment = view.findViewById(R.id.button_postComment);
                this.back = view.findViewById(R.id.button_back);
            } else {
                this.op = view.findViewById(R.id.textView_op);
                this.dateTime = view.findViewById(R.id.textView_dateTime);
                this.text = view.findViewById(R.id.textView_text);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    ArtifactAdapter(Artifact artifact, String artifactID, FirebaseUser currentUser, DatabaseReference dbRef, Context context) {
        this.artifact = artifact;
        this.artifactID = artifactID;
        this.currentUser = currentUser;
        this.dbRef = dbRef;
        this.context = context;
    }

    // When a view holder is created, distinguish between the view for artifact details or for a comment
    // using its view type
    @Override @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == artifactViewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.artifact, parent, false);
            return new ViewHolder(itemView, viewType);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
            return new ViewHolder(itemView, viewType);
        }
    }

    // When a view holder is used, distinguish between the view for artifact details or for a comment
    // using its position (view type)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position == artifactViewType) {
            Glide.with(context)
                    .load(artifact.getUrl())
                    .fitCenter()
                    .into(holder.image);
            holder.date.setText(artifact.getDate());
            holder.description.setText(artifact.getDescription());
            holder.tags.setText(artifact.getTags());

            // Posting a new comment
            holder.postComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Date currentTime = Calendar.getInstance().getTime();

                    SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String dateText = newDateFormat.format(currentTime);

                    String newCommentText = holder.newComment.getText().toString();

                    Comment comment = new Comment();
                    comment.setOp(currentUser.getUid());

                    comment.setDateTime(dateText);
                    comment.setText(newCommentText);

                    // Push the new comment into the database
                    dbRef.child("artifacts").child(artifactID).child("comments").push().setValue(comment);
                }
            });

            // Pressing the back button to go back to the gallery
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
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        holder.op.setText(user.getName());
                    } else {
                        holder.op.setText(context.getResources().getString(R.string.error));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Something went wrong...
                    System.out.println("Error retrieving user data in comment");
                    holder.op.setText(context.getResources().getString(R.string.error));
                }
            });
        }
    }

    // Get the number of items that should be in the recycler view (number of comments + 1)
    @Override
    public int getItemCount() {
        return artifact.getComments().size() + 1;
    }

    // Go back to the gallery
    private void goBack() {
//        Intent intent = new Intent(context, HomeActivity.class);
//        context.startActivity(intent);
        ((Activity)context).finish();

//        ((Activity)context).onBackPressed();
//        ((Activity)context).finish();
//        ((Activity)context).finishActivity(0);
    }

}