package com.wpoppin.whatspoppin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by joseph on 2/17/2017.
 */

public class SelectInterests extends AppCompatActivity {

    private Button submit;
    private ArrayList<Integer> interest;
    int originalColor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectinterests);



        submit = (Button) findViewById(R.id.submit);
        interest = new ArrayList<Integer>();

        originalColor = Color.WHITE;

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(interest.size() >= 8) {
                    User c = PrefUtils.getCurrentUser(SelectInterests.this);
                    int[] interests = new int[20];
                    for (int i = 0; i < interest.size(); i++) {
                        interests[i] = interest.get(i);
                    }
                    c.addInterests(interests, interest.size());
                    PrefUtils.setCurrentUser(c, SelectInterests.this);

                    Intent intent = new Intent(SelectInterests.this, Main.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }


    public void UpdateArray(View v)
    {
        if (!interest.contains(Integer.parseInt(v.getTag().toString()))) {
            interest.add(Integer.parseInt(v.getTag().toString()));
            v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            interest.remove(interest.indexOf(Integer.parseInt(v.getTag().toString())));
            v.setBackgroundColor(originalColor);
        }

    }




}
