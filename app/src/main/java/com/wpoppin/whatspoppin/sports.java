package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class sports extends ActionBarActivity {

    private static final String ENDPOINT = "http://www.wpoppin.com/api/sports.json";

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
    private ListView sportsView;
    private View sports;
    private Toolbar toolbar;
    private SportsListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

        requestQueue = Volley.newRequestQueue(this);

        author = (TextView) findViewById(R.id.author);
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
        date = (TextView) findViewById(R.id.date);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        sportsView = (ListView) findViewById(R.id.sportsView);
        adapter = new SportsListAdapter(this, eventList);
        sportsView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        toolbar = (Toolbar) findViewById(R.id.main_menu); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        fetchPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            sports = (View) findViewById(R.id.action_search);

            sports.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(sports.this, JSON.class));
                }
            });
        }

        return super.onOptionsItemSelected(item);
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
            Log.e("JSON", error.toString());
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