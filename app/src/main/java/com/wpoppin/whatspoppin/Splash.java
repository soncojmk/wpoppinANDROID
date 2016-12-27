package com.wpoppin.whatspoppin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static android.R.attr.key;

/**
 * Created by joseph on 12/24/2016.
 */

public class Splash extends AppCompatActivity {


    private String usersname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String restoredText = prefs.getString("username", null);

        if(PrefUtils.getCurrentUser(Splash.this) == null){

            Intent homeIntent = new Intent(Splash.this, login.class);

            startActivity(homeIntent);

            finish();
        }


        else {
            Intent intent = new Intent(this, JSON.class);
            startActivity(intent);
            finish();
        }
    }

}

