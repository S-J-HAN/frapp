package com.itproject.frapp;

public class User {

    private String email;
    private String fullName; // Actual full name
    private String name; // First name only, used for login
    private String location;

    public User() {
    }

    public User(String email, String fullName, String name, String location) {
        this.email = email;
        this.fullName = fullName;
        this.name = name;
        this.location = location;
    }

}
