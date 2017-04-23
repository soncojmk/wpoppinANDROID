package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Abby on 2/21/2017.
 * This page handles when the user selects "For You", "Today", "This Week", or "This Month"
 * It loads the correct lists and updates the adapter to show the users the correct data
 */

public class TopNavigationHandler extends Fragment {

    private View currentView;

    private ImageButton for_you;
    private ImageButton featured;
    private ImageButton notifications;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        for_you = (ImageButton) currentView.findViewById(R.id.for_you);
        featured = (ImageButton) currentView.findViewById(R.id.featured);
        notifications = (ImageButton) currentView.findViewById(R.id.notification);

        //Initially on for you
        replaceFragment(new For_You());

        for_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for_you.setColorFilter(getResources().getColor(R.color.userPurple));
                featured.setColorFilter(getResources().getColor(R.color.black));
                notifications.setColorFilter(getResources().getColor(R.color.black));
                replaceFragment(new For_You());

            }
        });
        featured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                featured.setColorFilter(getResources().getColor(R.color.userPurple));
                for_you.setColorFilter(getResources().getColor(R.color.black));
                notifications.setColorFilter(getResources().getColor(R.color.black));
                replaceFragment(new Featured());
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifications.setColorFilter(getResources().getColor(R.color.userPurple));
                for_you.setColorFilter(getResources().getColor(R.color.black));
                featured.setColorFilter(getResources().getColor(R.color.black));
                // todo replaceFragment(new Featured());
            }
        });
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.activity_top_navigation, container, false);
        return currentView;
    }


    public void replaceFragment(Fragment fragment) {
        Log.e("HERE topnav", "Replace Frag");
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main, fragment);
        fragmentTransaction.commit();
    }


}
