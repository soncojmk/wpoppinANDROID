package com.wpoppin.whatspoppin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Abby on 2/21/2017.
 * This page handles when the user selects "For You", "Today", "This Week", or "This Month"
 * It loads the correct lists and updates the adapter to show the users the correct data
 */

public class TopNavigationHandler extends Fragment {

    private View currentView;

    private ImageButton for_you;
    private ImageButton explore;
    private ImageButton notifications;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        for_you = (ImageButton) currentView.findViewById(R.id.for_you);
        explore = (ImageButton) currentView.findViewById(R.id.explore);
        notifications = (ImageButton) currentView.findViewById(R.id.notification);

        for_you.setColorFilter(getResources().getColor(R.color.black));
        //Initially on for you
        replaceFragment(new For_You());

        for_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for_you.setColorFilter(getResources().getColor(R.color.black));
                explore.setColorFilter(getResources().getColor(R.color.dark_gray));
                notifications.setColorFilter(getResources().getColor(R.color.dark_gray));
                replaceFragment(new For_You());

            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explore.setColorFilter(getResources().getColor(R.color.black));
                for_you.setColorFilter(getResources().getColor(R.color.dark_gray));
                notifications.setColorFilter(getResources().getColor(R.color.dark_gray));
                replaceFragment(new Featured());
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifications.setColorFilter(getResources().getColor(R.color.black));
                for_you.setColorFilter(getResources().getColor(R.color.dark_gray));
                explore.setColorFilter(getResources().getColor(R.color.dark_gray));
                replaceFragment(new NotificationsFeedPage());
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
