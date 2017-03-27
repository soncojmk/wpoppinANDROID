package com.wpoppin.whatspoppin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by joseph on 3/26/2017.
 */

public class RecommendedFollowListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> Items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Bitmap bitmap;
    private User user;

    public RecommendedFollowListAdapter(Activity activity, List<User> Items) {
        this.activity = activity;
        this.Items = Items;
    }



    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int location) {
        return Items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.recommended_follow_list, null);


        final ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView about = (TextView) convertView.findViewById(R.id.about);
        TextView college = (TextView) convertView.findViewById(R.id.college);
        final Button follow = (Button) convertView.findViewById(R.id.follow);

        // getting movie data for the row
        final User account = Items.get(position);

        user = PrefUtils.getCurrentUser(activity);
        Log.i("useradapter", user.getUrl());

        username.setText(account.user.getUsername());
        about.setText(account.getBio());
        college.setText(Integer.toString(account.getCollege()));

        follow.setText("Accept Request");
        follow.setBackgroundColor(convertView.getResources().getColor(R.color.white));
        follow.setTextColor(convertView.getResources().getColor(R.color.colorPrimary));
        follow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = account.getUrl() + "follow/";
                Log.i("account", url);
                PostDataToServer.follow(Request.Method.POST, user.getToken(), url);
                follow.setText("Requested");
                follow.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                follow.setTextColor(activity.getResources().getColor(R.color.white));
            }

        });

        return convertView;
    }

}
