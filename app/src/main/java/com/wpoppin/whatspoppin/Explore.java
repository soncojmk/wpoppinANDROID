package com.wpoppin.whatspoppin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;


import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

/**
 * Created by joseph on 12/25/2016.
 * This class allows users to explore the different categories of events
 * in a gridView fed from ExploreGridAdapter.java
 *
 */

public class Explore extends Fragment {
    GridView gv;
    Context context;
    ArrayList prgmName;
    private View sports;
    private View profile;
    private BottomBar bottomBar;
    private View view;
    private Fragment fragment;

    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }

    public static String [] prgmNameList={"MUSIC","PERFORMING ARTS","ART","SPORTS","FUNDRAISERS","HEALTH / WELL-BEING", "GAMING", "FILMS", "THINK"};
    public static int [] prgmImages={R.drawable.music,R.drawable.arts,R.drawable.gallery, 0,R.drawable.fundraisers, 0, 0, 0, 0};
    public static String selection;

    public Explore(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.explore, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        gv=(GridView) view.findViewById(R.id.gridView1);
        gv.setAdapter(new ExploreGridAdapter(this, prgmNameList,prgmImages));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // start-media code could go in fragment or adapter
                switch(position){
                    case 0:
                        selection = "Music";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/music.json");
                        ///fragment = new Music();
                        ///replaceFragment(fragment);
                        break;

                    case 1:
                        selection = "Performing Arts";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/performing_arts.json");
                        //fragment = new PerformingArts();
                        //replaceFragment(fragment);
                        break;

                    case 2:
                        selection = "Art";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/art.json");
                        //fragment = new Art();
                        //replaceFragment(fragment);
                        break;

                    case 3:
                        selection = "Sports";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/sports.json");
                        //fragment = new Comedy();
                        //replaceFragment(fragment);
                        break;

                    case 4:
                        selection = "Fundraisers";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/charity.json");
                        //fragment = new Poetry();
                        //replaceFragment(fragment);
                        break;

                    case 5:
                        selection = "Health and Well-Being";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/events/?format=json");
                        //fragment = new Fundraisers();
                        //replaceFragment(fragment);
                        break;

                    case 6:
                        selection = "Gaming";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/events/?format=json");
                        //fragment = new Fundraisers();
                        //replaceFragment(fragment);
                        break;

                    case 7:
                        selection = "Films";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/events/?format=json");
                        //fragment = new Fundraisers();
                        //replaceFragment(fragment);
                        break;

                    case 8:
                        selection = "Think";
                        ExploreSelectionHandler.SetEndpoint("http://www.wpoppin.com/api/think/?format=json");
                        //fragment = new Fundraisers();
                        //replaceFragment(fragment);
                        break;

                }
                fragment = new ExploreSelectionHandler();
                replaceFragment(fragment);

            }
        });


        super.onActivityCreated(savedInstanceState);


    }

    public static String getSelection()
    {
        return selection;
    }

    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



}
