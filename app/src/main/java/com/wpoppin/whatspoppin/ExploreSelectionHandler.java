package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Abby on 2/21/2017.
 * This page handles all of the events. The Explore page tells this one what event was selected
 * This page then loads the correct data per selection and throws it into the listview
 */

public class ExploreSelectionHandler extends Fragment {

    private static String endpoint = "";
    private View currentView;
    private ProgressDialog pDialog;
    private CustomListAdapter adapter;
    private Gson gson;
    private RequestQueue requestQueue;
    private List<Post> eventList = new ArrayList<Post>();
    private ListView listView;
    private String data;

    /**
     * Set the URL to load for this fragment
     * @param url
     */
    public static void SetEndpoint(String url)
    {
        endpoint = url;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        String selection = Explore.getSelection();

        requestQueue = Volley.newRequestQueue(getActivity());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        listView = (ListView) currentView.findViewById(R.id.listView);
        adapter = new CustomListAdapter(getActivity(), eventList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        Toolbar toolbar = (Toolbar) currentView.findViewById(R.id.main_menu); // Attaching the layout to the toolbar object
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(selection + " Events");

        fetchPosts();
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.activity_explore_selection, container, false);
        setHasOptionsMenu(true);
        return currentView;
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



    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }


    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);

        fragmentTransaction.commit();
    }





    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, endpoint, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));
            hidePDialog();

            for (Post post : posts) {
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

                eventList.add(event);
            }

            adapter.notifyDataSetChanged();
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            data = error.toString();
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
