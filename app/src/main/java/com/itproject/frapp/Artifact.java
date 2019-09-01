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

}
