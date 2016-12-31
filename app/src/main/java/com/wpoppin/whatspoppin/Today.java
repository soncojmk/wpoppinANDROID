package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joseph on 12/30/2016.
 */

public class Today extends AppCompatActivity {

    private static final String ENDPOINT = "http://www.wpoppin.com/api/events_today.json";

    private RequestQueue requestQueue;
    private Gson gson;

    String data = " ";
    private TextView author;
    private TextView title;
    private TextView price;
    private TextView description;
    private TextView date;
    private ProgressDialog pDialog;
    private List<Post> eventList = new ArrayList<Post>();
    private ListView listView;
    private View sports;
    private Toolbar toolbar;
    private CustomListAdapter adapter;
    private DrawerLayout drawerLayout;
    private View profile;
    private BottomBar bottomBar;

/*    private Button all_events;
    private Button today;
    private Button tomorrow;
    private Button this_week;
    private Button this_month; */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        requestQueue = Volley.newRequestQueue(this);

        author = (TextView) findViewById(R.id.author);
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
        date = (TextView) findViewById(R.id.date);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomListAdapter(this, eventList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        View all_events = (View) findViewById(R.id.day).findViewById(R.id.all_events);
        View today = (View) findViewById(R.id.day).findViewById(R.id.today);
        View tomorrow = (View) findViewById(R.id.day).findViewById(R.id.tomorrow);
        View this_week = (View) findViewById(R.id.day).findViewById(R.id.this_week);
        View this_month = (View) findViewById(R.id.day).findViewById(R.id.this_month);

        all_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Today.this, JSON.class));
                overridePendingTransition(0,0);
            }
        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Today.this, Today.class));
                overridePendingTransition(0,0);
            }
        });

        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Today.this, Tomorrow.class));
                overridePendingTransition(0,0);
            }
        });

        this_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Today.this, ThisWeek.class));
                overridePendingTransition(0,0);
            }
        });

        this_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Today.this, ThisMonth.class));
                overridePendingTransition(0,0);
            }
        });

        //toolbar = (Toolbar) findViewById(R.id.main_menu); // Attaching the layout to the toolbar object
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                                startActivity(new Intent(Today.this, JSON.class));
                                overridePendingTransition(0,0);
                            }
                        });
                        break;
                    case R.id.location_item:
                        profile = (View) findViewById(R.id.location_item);

                        profile.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Today.this, Explore.class));
                                overridePendingTransition(0,0);
                            }
                        });
                        break;
                    case R.id.favorite_item:
                        profile = (View) findViewById(R.id.favorite_item);

                        profile.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Today.this, sports.class));
                                overridePendingTransition(0,0);
                            }
                        });
                        break;
                }
            }
        });

        fetchPosts();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }






    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);

        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));
            Log.i("JSON", posts.size() + " posts loaded.");

            hidePDialog();



            try {
                for (Post post : posts) {
                    data += post.author + ": " + post.title + ": " + post.image +"\n";
                    Post event = new Post();

                    event.setCategory(post.category);
                    event.setAuthor(post.author);
                    event.setTitle(post.title);
                    event.setPrice(post.price);
                    event.setThumbnailUrl(post.image);
                    event.setDescription(post.description);
                    event.setDate(post.date);
                    event.setTime(post.time);
                    event.setTicket_link(post.ticket_link);


                    Log.i("JSON", post.author + ": " + post.title);
                    eventList.add(event);


                }
                //output.setText(data);


            }

            catch (Exception e) {
                e.printStackTrace();

            }

            adapter.notifyDataSetChanged();

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("JS" +
                    "ON", error.toString());
            data = error.toString();
            //output.setText(data);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();


            pDialog = null;
        }
    }

}
