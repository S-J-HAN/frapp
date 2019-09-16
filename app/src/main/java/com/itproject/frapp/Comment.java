package com.itproject.frapp;

public class Comment {

    private String op;
    private String datetime;
    private String text;

    public Comment() {
    }

    public Comment(String op, String datetime, String text) {
        this.op = op;
        this.datetime = datetime;
        this.text = text;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setDateTime(String datetime) {
        this.datetime = datetime;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOp() {
        return this.op;
    }

    public String getDateTime() {
        return this.datetime;
    }

    public String getText() {
        return this.text;
    }

}
