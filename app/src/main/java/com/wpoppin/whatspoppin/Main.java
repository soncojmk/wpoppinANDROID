package com.wpoppin.whatspoppin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class Main extends AppCompatActivity {

    static ArrayList<Integer> backbutton = new ArrayList<>();
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);

        backbutton.add(0);
        Fragment fragment = new TopNavigationHandler();
        replaceFragment(fragment);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    backbutton.add(0);
                    Fragment json = new TopNavigationHandler();
                    replaceFragment(json);
                } else if (item.getItemId() == R.id.explore) {
                    backbutton.add(1);
                    Fragment json = new Explore();
                    replaceFragment(json);
                } else if (item.getItemId() == R.id.stories) {
                    backbutton.add(2);
                    Fragment json = new Stories();
                    replaceFragment(json);
                } else if (item.getItemId() == R.id.profile) {
                    backbutton.add(3);
                    Fragment json = new Profile();
                    replaceFragment(json);
                }

                updateNavigationBarState(item.getItemId());
                return true;
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (backbutton.size() <= 1) {
            finish();
            return;
        }
        int back = backbutton.get(backbutton.size() - 2);
        Log.e("Back", back + "");
        backbutton.remove(backbutton.size() - 1);
        Fragment json;
        switch (back) {
            case 0:
                json = new TopNavigationHandler();
                updateNavigationBarState(R.id.home);
                break;
            case 1:
                json = new Explore();
                updateNavigationBarState(R.id.explore);
                break;
            case 2:
                json = new Stories();
                updateNavigationBarState(R.id.stories);
                break;
            case 3:
                json = new Profile();
                updateNavigationBarState(R.id.profile);
                break;
            default:
                json = new TopNavigationHandler();
                updateNavigationBarState(R.id.home);
                break;
        }
        replaceFragment(json);
    }

    private void updateNavigationBarState(int actionId) {
        Menu menu = bottomNavigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == actionId);
        }
    }
}

