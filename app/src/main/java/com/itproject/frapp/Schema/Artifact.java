/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.Schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Artifact implements Serializable {

    private String date;
    private String description;
    private String op; // UserID of the original poster
    private String tags;
    private String url;
    private String id;
    private String people = "photo";
    private String semanticTags = "photo";
    private ArrayList<Comment> comments = new ArrayList<>();

    public Artifact() {
    }

    public Artifact(String date, String description, String op,
                    String tags, ArrayList<Comment> comments) {
        this.date = date;
        this.description = description;
        this.op = op;
        this.tags = tags;
        this.comments = comments;
    }

    // SETTERS
    public void setDate(String date) {
        this.date = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setOp(String op) {
        this.op = op;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setID(String id) { this.id = id; }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setPeople(String people) { this.people = people; }
    public void setSemanticTags(String semanticTags) { this.semanticTags = semanticTags; }
    public void setComments(HashMap<String, HashMap<String, String>> comments) {
        ArrayList<Comment> newComments = new ArrayList<>();
        for (String key : comments.keySet()) {
            newComments.add(new Comment(comments.get(key).get("op"),
                                        comments.get(key).get("dateTime"),
                                        comments.get(key).get("text"),
                                        key));
        }

        // Sort the comments by date
        newComments.sort(Comparator.comparing(Comment::getDateTime));

        this.comments = newComments;
    }

    // GETTERS
    public String getDate() {
        return this.date;
    }

    public String getSortableDate() {
        ArrayList<String> vals = new ArrayList<String>(Arrays.asList(this.date.split(Character.toString('/'))));
        Collections.reverse(vals);
        return String.join("", vals);
    }

    public String getDescription() {
        return this.description;
    }
    public String getOp() {
        return this.op;
    }
    public String getTags() {
        return this.tags;
    }
    public ArrayList<Comment> getComments() {
        return this.comments;
    }
    public String getID() { return this.id; }
    public String getUrl() {
        return url;
    }
    public String getPeople() { return people; }
    public String getSemanticTags() { return semanticTags; }

}
