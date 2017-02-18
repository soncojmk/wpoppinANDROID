package com.wpoppin.whatspoppin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by joseph on 2/17/2017.
 */

public class SelectInterests extends AppCompatActivity {

    private TextView music;
    private TextView dance;
    private TextView performing_arts;
    private TextView films;
    private TextView sports;
    private TextView poetry;
    private TextView art;
    private TextView debates;
    private TextView comedy;
    private TextView philanthropy;
    private TextView professional;
    private TextView lectures;
    private TextView health;
    private TextView gaming;
    private TextView politics;

    private Button submit;
    private int[] interests;
    private int i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectinterests);

        /*
        music = (TextView) findViewById(R.id.music);
        dance = (TextView) findViewById(R.id.dance);
        performing_arts = (TextView) findViewById(R.id.performing_arts);
        films = (TextView) findViewById(R.id.films);
        sports = (TextView) findViewById(R.id.sports);
        poetry = (TextView) findViewById(R.id.poetry);
        art = (TextView) findViewById(R.id.art);
        debates = (TextView) findViewById(R.id.debates);
        comedy = (TextView) findViewById(R.id.comedy);
        philanthropy = (TextView) findViewById(R.id.philanthropy);
        professional = (TextView) findViewById(R.id.professional);
        lectures = (TextView) findViewById(R.id.lectures);
        health = (TextView) findViewById(R.id.health);
        gaming = (TextView) findViewById(R.id.gaming);
        politics = (TextView) findViewById(R.id.politics);

        submit = (Button) findViewById(R.id.submit);

        interests = new int[20];
        i = 0;


        music.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) music.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 1;
                i++;
                music.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });


        dance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) dance.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 4;
                i++;
                dance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        performing_arts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) performing_arts.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 6;
                i++;
                performing_arts.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });


        films.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) films.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 12;
                i++;
                films.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) sports.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 2;
                i++;
                sports.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        poetry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) poetry.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 14;
                i++;
                poetry.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        art.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) art.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 8;
                i++;
                art.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        debates.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) debates.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 18;
                i++;
                debates.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        comedy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) comedy.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 13;
                i++;
                comedy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        philanthropy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) philanthropy.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 3;
                i++;
                philanthropy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });


        professional.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) professional.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 11;
                i++;
                professional.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        lectures.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) lectures.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 17;
                i++;
                lectures.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        health.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) health.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 19;
                i++;
                health.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        gaming.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) gaming.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 20;
                i++;
                gaming.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        politics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) politics.getBackground();
                int colorId = buttonColor.getColor();

                interests[i] = 21;
                i++;
                politics.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        */
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User c = PrefUtils.getCurrentUser(SelectInterests.this);
                c.addInterests(interests, i);
                PrefUtils.setCurrentUser(c, SelectInterests.this);
                Intent intent = new Intent(SelectInterests.this, Main.class);
                startActivity(intent);
                finish();
            }
        });


    }


    public void UpdateArray(View v)
    {
        ColorDrawable buttonColor = (ColorDrawable) v.getBackground();
        int colorId = buttonColor.getColor();

        interests[i] = Integer.parseInt(v.getTag().toString());
        i++;
        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }



}
