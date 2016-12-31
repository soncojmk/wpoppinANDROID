package com.wpoppin.whatspoppin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;


import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.util.ArrayList;

/**
 * Created by joseph on 12/25/2016.
 * This class allows users to explore the different categories of events
 * in a gridView fed from ExploreGridAdapter.java
 *
 */

public class Explore extends Activity {
    GridView gv;
    Context context;
    ArrayList prgmName;
    private View sports;
    private View profile;
    private BottomBar bottomBar;


    public static String [] prgmNameList={"MUSIC","PERFORMING ARTS","ART","COMEDY","POETRY","FUNDRAISERS"};
    public static int [] prgmImages={R.drawable.music,R.drawable.arts,R.drawable.gallery,R.drawable.comedy,R.drawable.poetry,R.drawable.fundraisers};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);
        gv=(GridView) findViewById(R.id.gridView1);
        gv.setAdapter(new ExploreGridAdapter(this, prgmNameList,prgmImages));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // start-media code could go in fragment or adapter
                switch(position){
                    case 0:
                        startActivity(new Intent(Explore.this, Music.class));
                        overridePendingTransition(0,0);
                        break;

                    case 1:
                        startActivity(new Intent(Explore.this, PerformingArts.class));
                        overridePendingTransition(0,0);
                        break;

                    case 2:
                        startActivity(new Intent(Explore.this, Art.class));
                        overridePendingTransition(0,0);
                        break;

                    case 3:
                        startActivity(new Intent(Explore.this, Comedy.class));
                        overridePendingTransition(0,0);
                        break;

                    case 4:
                        startActivity(new Intent(Explore.this, Poetry.class));
                        overridePendingTransition(0,0);
                        break;

                    case 5:
                        startActivity(new Intent(Explore.this, Fundraisers.class));
                        overridePendingTransition(0,0);
                        break;

                }
            }
        });

        bottomBar = BottomBar.attach(this, savedInstanceState);
        //bottomBar.setActiveTabColor("#FFA500");
        bottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.recent_item:
                        sports = (View) findViewById(R.id.recent_item);

                        sports.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Explore.this, JSON.class));
                                overridePendingTransition(0,0);
                            }
                        });
                        break;
                    case R.id.location_item:
                        View profile = (View) findViewById(R.id.location_item);

                        profile.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Explore.this, Explore.class));
                                overridePendingTransition(0,0);
                            }
                        });
                        break;
                    case R.id.favorite_item:
                        profile = (View) findViewById(R.id.favorite_item);

                        profile.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Explore.this, Profile.class));
                                overridePendingTransition(0,0);
                            }
                        });
                        break;
                }
            }

        });

        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorPrimary));
        bottomBar.mapColorForTab(1, 0xFF5D4037);
        bottomBar.mapColorForTab(2, "#7B1FA2");

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }

}
