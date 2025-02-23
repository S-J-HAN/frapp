/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.Schema;

// Class for storing and retrieving a user's name
public class User {

    private String name;

    private String email;
    private String url;

    private String location;

    public User() {
    }

    public User(String email, String fullName, String name, String location) {
        this.email = email;
        this.name = name;
        this.location = location;
    }


    // SETTERS
    public void setUrl(String url) { this.url = url; }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    // GETTERS
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
    public String getEmail() {
        return email;
    }
    public String getUrl() {
        return url;
    }

}
