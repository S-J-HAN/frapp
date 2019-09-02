package com.itproject.frapp;

public class User {

    private String email;
    private String name;
    private String location;

    public User() {
    }

    public User(String email, String fullName, String name, String location) {
        this.email = email;
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
