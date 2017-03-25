package com.wpoppin.whatspoppin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by joseph on 3/24/2017.
 */

public class FollowRequestsListAdapater extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> Items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Bitmap bitmap;

    public FollowRequestsListAdapater(Activity activity, List<User> Items) {
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
            convertView = inflater.inflate(R.layout.follow_request_list, null);


        final ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        TextView about = (TextView) convertView.findViewById(R.id.about);
        TextView college = (TextView) convertView.findViewById(R.id.college);
        Button follow = (Button) convertView.findViewById(R.id.follow);

        // getting movie data for the row
        final User account = Items.get(position);


        username.setText(account.user.getUsername());
        about.setText(account.getBio());
        college.setText(Integer.toString(account.getCollege()));






        return convertView;
    }




}
