package com.jaess.winterland;

public class MainModel {

    String Name,Description,ImageURL,Email;

    MainModel(){


    }

    public MainModel(String name, String description, String imageURL, String email) {
        Name = name;
        Description = description;
        ImageURL = imageURL;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public String getEmail() {
        return Email;
    }
}
