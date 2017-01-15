package com.wpoppin.whatspoppin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;


/**
 * Created by joseph on 1/13/2017.
 */

public class Main extends AppCompatActivity {

    private int current;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);

        Fragment fragment = new JSON();
        replaceFragment(fragment);

        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    Fragment json = new JSON();
                    replaceFragment(json);
                } else if (item.getItemId() == R.id.explore) {
                    Fragment json = new Explore();
                    replaceFragment(json);
                }  else if (item.getItemId() == R.id.stories) {
                    Fragment json = new Stories();
                    replaceFragment(json);
                } else if (item.getItemId() == R.id.profile) {
                    Fragment json = new Profile();
                    replaceFragment(json);
                }
                return true;

            }
        });
        }

        public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}

