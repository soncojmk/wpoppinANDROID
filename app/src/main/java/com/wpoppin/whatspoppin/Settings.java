package com.wpoppin.whatspoppin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;

public class Settings extends AppCompatActivity {

    LinearLayout invite;
    LinearLayout privacy;
    LinearLayout btnLogout;
    LinearLayout interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Settings");

        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        invite = (LinearLayout) findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "I really like this events app called What'sPoppin. I think you'd like it too. Check it out: bit.ly/2jrcOhw";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey check this app out:");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
        privacy = (LinearLayout)findViewById(R.id.privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.wpoppin.com"));
                startActivity(browserIntent);
            }
        });
        btnLogout = (LinearLayout) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.clearCurrentUser(getBaseContext());
                SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("username");
                editor.apply();

                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();


                Intent i= new Intent(getBaseContext(), login.class);
                startActivity(i);
            }
        });
        interests = (LinearLayout) findViewById(R.id.interests);
        interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SelectInterests.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}
