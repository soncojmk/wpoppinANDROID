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
import android.util.Log;
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

        final User currentUser = PrefUtils.getCurrentUser(SelectInterests.this);
        int[] temp = currentUser.getInterests();
        setOriginalInterests(temp);

        originalColor = Color.WHITE;

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(interest.size() >= 6) {
                    int[] interests = new int[20];
                    for (int i = 0; i < interest.size(); i++) {
                        interests[i] = interest.get(i);
                    }
                    currentUser.addInterests(interests, interest.size());
                    PrefUtils.setCurrentUser(currentUser, SelectInterests.this);

                    Intent intent = new Intent(SelectInterests.this, Main.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        if(interest.size() >= 6){
            submit.setBackgroundColor(getResources().getColor(R.color.orange));
            submit.setTextColor(originalColor);
        }

    }

    public void setOriginalInterests(int[] temp)
    {
        for(int num : temp) {
            if (num != 0) {
                interest.add(num);
                try {
                    (findViewById(R.id.interest_view)).findViewWithTag(num + "").setBackgroundColor(getResources().getColor(R.color.wallet_holo_blue_light));
                    ((TextView)((findViewById(R.id.interest_view)).findViewWithTag(num + ""))).setTextColor(Color.WHITE);
                }
                catch (Exception e)
                {
                    Log.e("ERROR", num + "");
                }
            }
        }
    }


    public void UpdateArray(View v)
    {
        if (!interest.contains(Integer.parseInt(v.getTag().toString()))) {
            interest.add(Integer.parseInt(v.getTag().toString()));
            v.setBackgroundColor(getResources().getColor(R.color.wallet_holo_blue_light));
            ((TextView)v).setTextColor(originalColor);
            if(interest.size() >= 6){
                submit.setBackgroundColor(getResources().getColor(R.color.orange));
                submit.setTextColor(originalColor);
            }


        }
        else
        {
            interest.remove(interest.indexOf(Integer.parseInt(v.getTag().toString())));
            v.setBackgroundColor(originalColor);
            ((TextView)v).setTextColor(getResources().getColor(R.color.black));
            if(interest.size() < 6){
                submit.setBackgroundColor(originalColor);
                submit.setTextColor(getResources().getColor(R.color.black));
            }
        }

    }




}
