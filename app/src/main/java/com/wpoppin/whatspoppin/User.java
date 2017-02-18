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

    public URL profile_picture;

    public String token;

    public int interests[] = new int[20];

    public boolean interestSet;

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

    public URL getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(URL profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

