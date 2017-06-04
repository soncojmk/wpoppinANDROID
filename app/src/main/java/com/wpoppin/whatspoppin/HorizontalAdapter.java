package com.wpoppin.whatspoppin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 4/2/2017.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private List<String> horizontalList;
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> Items = new ArrayList<>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Bitmap bitmap;
    private User user = new User();
    private User account;


    public HorizontalAdapter(Activity activity, List<User> Items) {
        this.activity = activity;
        this.Items = Items;
        this.user = PrefUtils.getCurrentUser(activity);
        Log.i("consctructor", Items.toString());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView username;
        protected TextView about;
        protected TextView college;
        protected Button follow;
        protected ImageView avatar;


        public MyViewHolder(View view) {
            super(view);
            this.avatar = (ImageView) activity.findViewById(R.id.avatar);
            this.username = (TextView) activity.findViewById(R.id.username);
            this.about = (TextView) activity.findViewById(R.id.about);
            this.college = (TextView) activity.findViewById(R.id.college);
            this.follow = (Button) activity.findViewById(R.id.follow);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organizations_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        account = new User();
        account =  Items.get(position);

        Log.i("useradapter", "item" + user.getUsername());

//        holder.username.setText(user.getUsername());
       // holder.about.setText(Items.get(position).getBio());
        //holder.college.setText(Integer.toString(Items.get(position).getCollege()));


        holder.follow.setText("follow");
        holder.follow.setBackgroundColor(activity.getResources().getColor(R.color.white));
        holder.follow.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        holder.follow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = account.getUrl() + "follow/";
                Log.i("account", url);
                PostDataToServer.follow(Request.Method.POST, user.getToken(), url);
                holder.follow.setText("Requested");
                holder.follow.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                holder.follow.setTextColor(activity.getResources().getColor(R.color.white));
            }

        });

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}

