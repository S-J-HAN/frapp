/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.ArtifactPages;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.itproject.frapp.MainGallery.HomeActivity;
import com.itproject.frapp.R;
import com.itproject.frapp.Schema.Artifact;
import com.itproject.frapp.Schema.Comment;
import com.itproject.frapp.Schema.User;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

// Adapter class for controlling the contents of the artifact page recycler view
public class ArtifactAdapter extends RecyclerView.Adapter<ArtifactAdapter.ViewHolder> {

    private Artifact artifact;
    private String artifactID;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;
    private Context context;
    private Activity activity;

    private final static int artifactViewType = 0;

    // Class for each element of the recycler view (either the artifact + details or a comment)
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView op, dateTime, text;
        private TextView date, description, tags;
        private EditText newComment;
        private ImageView image;
        private ImageButton deleteComment, deleteArtifact, postComment;
        private LinearLayout back;
        private CircleImageView userProfile;

        ViewHolder(View view, int viewType) {
            super(view);
            // If this is the artifact details view
            if (viewType == artifactViewType) {
                this.image = view.findViewById(R.id.imageView_artifact);
                this.date = view.findViewById(R.id.textView_dateTime);
                this.description = view.findViewById(R.id.textView_description);
                this.tags = view.findViewById(R.id.textView_tags);
                this.newComment = view.findViewById(R.id.editText_newComment);
                this.postComment = view.findViewById(R.id.imageButton_postComment);
                this.back = view.findViewById(R.id.linearLayout_back);
                this.deleteArtifact = view.findViewById(R.id.imageButton_deleteArtifact);
            // If this is the comment view
            } else {
                this.op = view.findViewById(R.id.textView_op);
                this.dateTime = view.findViewById(R.id.textView_dateTime);
                this.text = view.findViewById(R.id.textView_text);
                this.deleteComment = view.findViewById(R.id.imageButton_deleteComment);
                this.userProfile = view.findViewById(R.id.circleImage_profilePicture);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    ArtifactAdapter(Artifact artifact, String artifactID, FirebaseUser currentUser, DatabaseReference dbRef, Context context, Activity activity) {
        this.artifact = artifact;
        this.artifactID = artifactID;
        this.currentUser = currentUser;
        this.dbRef = dbRef;
        this.context = context;
        this.activity = activity;
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
            holder.postComment.setOnClickListener(view -> {

                // Only post a comment if it contains something
                if (holder.newComment.getText().length() > 0) {

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
            holder.back.setOnClickListener(view -> goBack(Activity.RESULT_CANCELED, null));

            // Clicking on an image opens it in full screen mode using Stfalcon library
            holder.image.setOnClickListener(view -> {

                String[] images = { artifact.getUrl() };

                new StfalconImageViewer.Builder<>(context, images, (imageView, imageUrl) ->
                        Glide.with(context).load(imageUrl).into(imageView))
                        .withTransitionFrom(holder.image)
                        .show();
            });

            // Show the delete button on an artifact if this is the user who uploaded it
            System.out.println(artifact.getOp());
            System.out.println(currentUser.getUid());
            if (artifact.getOp().equals(currentUser.getUid())) {
                holder.deleteArtifact.setVisibility(View.VISIBLE);
                holder.deleteArtifact.setOnClickListener(view -> {
                    deleteArtifactConfirmation();
                });
            }

        } else {
            Comment comment = artifact.getComments().get(position - 1);
            holder.dateTime.setText(comment.getDateTime());
            holder.op.setText(comment.getOp());
            holder.text.setText(comment.getText());

            // Show the delete button on a comment if this is the user who posted it
            if (comment.getOp().equals(currentUser.getUid())) {
                holder.deleteComment.setVisibility(View.VISIBLE);
                holder.deleteComment.setOnClickListener(view -> {
                    deleteCommentConfirmation(comment);
                });
            }

            dbRef.child("users").child(comment.getOp()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        holder.op.setText(user.getName());
                        if (user.getUrl() != null) {
                            Glide.with(context).load(user.getUrl()).into(holder.userProfile);
                        }
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
        if (artifact != null) {
            return artifact.getComments().size() + 1;
        } else {
            return 0;
        }
    }

    // Go back to the gallery
    private void goBack(int result, String artifactToDelete) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra("Delete", artifactToDelete);
        activity.setResult(result, intent);
        ((Activity)context).finish();
    }

    // Show a confirmation dialog box before deleting a comment
    private void deleteCommentConfirmation(Comment comment) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete_comment)
                .setPositiveButton(R.string.delete, (dialog, which) ->
                        dbRef.child("artifacts").child(artifactID).child("comments").child(comment.getId()).removeValue())
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                      // Do nothing
                })
                .show();
    }

    // Show a confirmation dialog box before deleting a comment
    private void deleteArtifactConfirmation() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete_artifact)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goBack(Activity.RESULT_OK, artifactID);
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    // Do nothing
                })
                .show();
    }

}