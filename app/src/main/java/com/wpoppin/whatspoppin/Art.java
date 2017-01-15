package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Art extends Fragment {

    private static final String ENDPOINT = "http://www.wpoppin.com/api/art.json";

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
    private CustomListAdapter adapter;
    private BottomBar bottomBar;
    private View profile;

    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }


    private View view;

    public Art(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_arts, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        requestQueue = Volley.newRequestQueue(getActivity());

        author = (TextView) view.findViewById(R.id.author);
        title = (TextView) view.findViewById(R.id.title);
        price = (TextView) view.findViewById(R.id.price);
        description = (TextView) view.findViewById(R.id.description);
        date = (TextView) view.findViewById(R.id.date);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        sportsView = (ListView) view.findViewById(R.id.listView);
        adapter = new CustomListAdapter(getActivity(), eventList);
        sportsView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        toolbar = (Toolbar) view.findViewById(R.id.main_menu); // Attaching the layout to the toolbar object
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Art Events");

        fetchPosts();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            Fragment fragment = new Explore();
            replaceFragment(fragment);
            // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
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

                    event.setUrl(post.url);
                    event.setCategory(post.category);
                    event.setAuthor(post.author);
                    event.setTitle(post.title);
                    event.setPrice(post.price);
                    event.setThumbnailUrl(post.image);
                    event.setDescription(post.description);
                    event.setDate(post.date);
                    event.setTime(post.time);
                    event.setTicket_link(post.ticket_link);
                    event.setAddress(post.street_address);
                    event.setCity(post.city);
                    event.setZipcode(post.zipcode);
                    event.setState(post.state);


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