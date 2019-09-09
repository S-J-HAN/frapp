package com.itproject.frapp;

public class Artifact {

    private String date;
    private String description;
    private String location;
    private String op; // UserID of the original poster
    private String tags;
    private String title;

    public Artifact() {
    }

    public Artifact(String date, String description, String location, String op, String tags, String title) {
        this.date = date;
        this.description = description;
        this.location = location;
        this.op = op;
        this.tags = tags;
        this.title = title;
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

    public String getDate() {
        return this.date;
    }

}
