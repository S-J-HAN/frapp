package com.itproject.frapp;

import java.util.ArrayList;
import java.util.HashMap;

public class Artifact {

    private String date;
    private String description;
    private String location;
    private String op; // UserID of the original poster
    private String tags;
    private String title;
    private String url;
    private String id;
    private ArrayList<Comment> comments = new ArrayList<>();

    public Artifact() {
    }

    public Artifact(String date, String description, String location, String op, String tags, String title, ArrayList<Comment> comments) {
        this.date = date;
        this.description = description;
        this.location = location;
        this.op = op;
        this.tags = tags;
        this.title = title;
        this.comments = comments;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setID(String id) { this.id = id; }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setComments(HashMap<String, HashMap<String, String>> comments) {
        ArrayList<Comment> newComments = new ArrayList<>();
        for (String key : comments.keySet()) {
            newComments.add(new Comment(comments.get(key).get("op"),
                                        comments.get(key).get("dateTime"),
                                        comments.get(key).get("text")));
        }
        this.comments = newComments;
    }

    public String getDate() {
        return this.date;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public String getOp() {
        return this.op;
    }

    public String getTags() {
        return this.tags;
    }

    public String getTitle() {
        return this.title;
    }

    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    public String getID() { return this.id; }

}
