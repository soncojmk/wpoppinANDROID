package com.wpoppin.whatspoppin;


import java.net.URL;

/**
 * Created by joseph on 12/24/2016.
 *
 * This is the User class that keeps track of who the user is in sharedPreferences
 */

public class User {

    public String username = null;

    public String email;

    public String facebookID = "1435767690067161";

    public String avatar;

    public String token;

    public int college;

    public String about;

    public int id;

    public int interests[] = new int[20];

    public boolean interestSet;

    public String url;

    public User user;


    public User getUser(){return user;}

    public void setUser(User user){this.user=user;}


    public String getUrl(){return url;}

    public void setUrl(String url){
        this.url = url;
    }

    public void addInterests(int interests[], int size){

        for(int i=0; i < size; i++){
            this.interests[i] = interests[i];
        }
        this.interestSet = true;
    }

    public int[] getInterests(){
        return interests;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCollege(){return college;}

    public void setCollege(int college){this.college = college;}

    public String getBio(){return about;}

    public void setBio(String bio){this.about = bio;}

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String profile_picture) {
        this.avatar = profile_picture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

