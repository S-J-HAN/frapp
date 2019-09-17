package com.itproject.frapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;



import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;




public class ArtifactAdapter extends RecyclerView.Adapter<ArtifactAdapter.ViewHolder> {
    private Artifact artifact;
    private String artifactID;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView op, datetime, text;
        public TextView date, description, tags;
        public EditText newComment;
        public Button postComment;


        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == 0) {
                this.date = (TextView) view.findViewById(R.id.textView_date);
                this.description = (TextView) view.findViewById(R.id.textView_description);
                this.tags = (TextView) view.findViewById(R.id.textView_tags);
                this.newComment = (EditText) view.findViewById(R.id.editText_newComment);
                this.postComment = (Button) view.findViewById(R.id.button_postComment);
            } else {
                this.op = (TextView) view.findViewById(R.id.textView_op);
                this.datetime = (TextView) view.findViewById(R.id.textView_datetime);
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArtifactAdapter(Artifact artifact, String artifactID, FirebaseUser currentUser, DatabaseReference dbRef) {
        this.artifact = artifact;
        this.artifactID = artifactID;
        this.currentUser = currentUser;
        this.dbRef = dbRef;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.artifact, parent, false);
            return new ViewHolder(itemView, viewType);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
            return new ViewHolder(itemView, viewType);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (position == 0) {
            holder.date.setText(artifact.getDate());
            holder.description.setText(artifact.getDescription());
            holder.tags.setText(artifact.getTags());

            final String newCommentText = holder.newComment.getText().toString();

            holder.postComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Comment comment = new Comment();
                    comment.setOp(currentUser.getDisplayName());
                    System.out.println(currentUser.getDisplayName() + "test");
                    comment.setDateTime("date");
                    comment.setText(newCommentText);
                    System.out.println(comment.toString() + "hello");
                    dbRef.child("artifacts").child(artifactID).child("comments").push().setValue(comment);
                }
            });

        } else {
            Comment comment = artifact.getComments().get(position - 1);
            holder.datetime.setText(comment.getDateTime());
            holder.op.setText(comment.getOp());
            holder.text.setText(comment.getText());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artifact.getComments().size() + 1;
    }
}