package com.wpoppin.whatspoppin;

/**
 * Created by joseph on 12/24/2016.
 * This is the user profile page where the user can logout and see their info
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


public class Profile extends Fragment {

    private TextView btnLogout;
    private User user;
    private ImageView profileImage;
    Bitmap bitmap;
    private TextView name;
    private LinearLayout post;
    private AlertDialog pDialog;
    private LinearLayout invite;
    private LinearLayout privacy;

    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }
    private View view;

    public Profile(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        user=PrefUtils.getCurrentUser(getActivity());
        profileImage= (ImageView) view.findViewById(R.id.profileImage);
        name = (TextView) view.findViewById(R.id.username);
        post = (LinearLayout) view.findViewById(R.id.post);
        invite = (LinearLayout) view.findViewById(R.id.invite);
        privacy = (LinearLayout) view.findViewById(R.id.privacy);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new AlertDialog.Builder(getActivity()).create();
                // Showing progress dialog before making http request
                pDialog.setMessage("In order to limit spam, event hosts can only post events through our website at wpoppin.com");
                pDialog.show();
            }
        });



            name.setText(user.username);

        // fetching facebook's profile picture
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {

                    imageURL = new URL("https://graph.facebook.com/" + user.facebookID + "/picture?type=large");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                profileImage.setImageBitmap(bitmap);
            }
        }.execute();

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

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.wpoppin.com"));
                startActivity(browserIntent);
            }
        });


        btnLogout = (TextView) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.clearCurrentUser(getActivity());
                SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("username");
                editor.apply();


                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();


                Intent i= new Intent(getActivity(), login.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }


}