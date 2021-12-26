package com.jaess.winterland;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Gift implements Serializable {

    public String Description;
    public String Name;
    public String Email;
    public String ImageURL;

    public Gift() {
// Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Gift(String description, String name, String email, String imageUrl) {
        this.Description = description;
        this.Name = name;
        this.Email = email;
        this.ImageURL = imageUrl;
    }
}
