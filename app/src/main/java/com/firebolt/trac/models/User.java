package com.firebolt.trac.models;

/**
 * Created by onkar on 22/8/16.
 */
public class User {

    private String displayName;
    private String Email;
    private String imageUrl;

    public User(String displayName, String email, String imageUrl) {
        this.displayName = displayName;
        Email = email;
        this.imageUrl = imageUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
