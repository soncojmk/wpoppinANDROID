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
import android.widget.GridView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.util.ArrayList;

/**
 * Created by joseph on 12/25/2016.
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
