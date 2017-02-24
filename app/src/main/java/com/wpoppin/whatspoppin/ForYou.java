package com.wpoppin.whatspoppin;

/**
 * Created by joseph on 2/17/2017.
 *
 *
 * This file controls a users For You feed. Displays events based on a users saved interests
 */


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
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


public class ForYou extends Fragment {

    private static final String ENDPOINT = "http://www.wpoppin.com/api/events.json";

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

    private Button all_events;
    private Button for_you;
    private Button today;
    private Button tomorrow;
    private Button this_week;
    private Button this_month;

    private View view;

    private ArrayList<Integer> interest = new ArrayList<Integer>();

    public ForYou(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_json, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        int[] temp = PrefUtils.getCurrentUser(getActivity()).getInterests();
        for (int index = 0; index < temp.length; index++)
        {
            interest.add(temp[index]);
        }

        requestQueue = Volley.newRequestQueue(getActivity());

        author = (TextView) view.findViewById(R.id.author);
        title = (TextView) view.findViewById(R.id.title);
        price = (TextView) view.findViewById(R.id.price);
        description = (TextView) view.findViewById(R.id.description);
        date = (TextView) view.findViewById(R.id.date);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new CustomListAdapter(getActivity(), eventList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        for_you = (Button) view.findViewById(R.id.day).findViewById(R.id.for_you);
        for_you.setBackgroundColor(getResources().getColor(R.color.orange));
        for_you.setTextColor(getResources().getColor(R.color.white));

        //View all_events = (View) view.findViewById(R.id.day).findViewById(R.id.all_events);
        View today = (View) view.findViewById(R.id.day).findViewById(R.id.today);
        // View tomorrow = (View) view.findViewById(R.id.day).findViewById(R.id.tomorrow);
        View this_week = (View) view.findViewById(R.id.day).findViewById(R.id.this_week);
        View this_month = (View) view.findViewById(R.id.day).findViewById(R.id.this_month);

      /*  all_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment json = new AllEvents();
                replaceFragment(json);

            }
        }); */

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment json = new Today();
                replaceFragment(json);
            }
        });

        /*
        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment json = new Tomorrow();
                replaceFragment(json);
            }
        });
        */

        this_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment json = new ThisWeek();
                replaceFragment(json);
            }
        });

        this_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment json = new ThisMonth();
                replaceFragment(json);
            }
        });

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
            //  Log.i("JSON", posts.size() + " posts loaded.");

            hidePDialog();





            try {
                for (Post post : posts) {
                    data += post.author + ": " + post.title + ": " + post.image +"\n";
                    if(interest.contains(Integer.parseInt(post.getCategory()))) {
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


                        // Log.i("JSON", post.author + ": " + post.title);
                        eventList.add(event);
                    }

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
            //  Log.e("JS" +
            //          "ON", error.toString());
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