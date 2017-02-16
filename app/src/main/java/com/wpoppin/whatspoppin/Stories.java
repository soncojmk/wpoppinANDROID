package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joseph on 1/14/2017.
 */

public class Stories extends Fragment {

    private static final String ENDPOINT = "http://www.wpoppin.com/api/blog.json";

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
    private StoriesListAdapter adapter;
    private DrawerLayout drawerLayout;
    private View profile;
    private BottomBar bottomBar;

    private Button all_events;
    private Button today;
    private Button tomorrow;
    private Button this_week;
    private Button this_month;

    private View view;

    public Stories() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_stories, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(getActivity());

        //author = (TextView) view.findViewById(R.id.author);
        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new StoriesListAdapter(getActivity(), eventList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        toolbar = (Toolbar) view.findViewById(R.id.main_menu); // Attaching the layout to the toolbar object
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((TextView) view.findViewById(R.id.main_toolbar_title)).setText("What'sPoppin Stories");

        fetchPosts();
        super.onActivityCreated(savedInstanceState);
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
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);

        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));
           // Log.i("JSON", posts.size() + " posts loaded.");

            hidePDialog();


            try {
                for (Post post : posts) {
                    data += post.author + ": " + post.title + ": " + post.image + "\n";
                    Post event = new Post();

                    event.setUrl(post.url);
                    event.setAuthor(post.author);
                    event.setTitle(post.title);
                    event.setThumbnailUrl(post.image);
                    event.setDescription(post.description);


                  //  Log.i("JSON", post.author + ": " + post.title);
                    eventList.add(event);


                }
                //output.setText(data);


            } catch (Exception e) {
                e.printStackTrace();

            }

            adapter.notifyDataSetChanged();

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           // Log.e("JS" +
             //       "ON", error.toString());
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
