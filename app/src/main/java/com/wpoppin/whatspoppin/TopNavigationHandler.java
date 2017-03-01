package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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
 * This page handles when the user selects "For You", "Today", "This Week", or "This Month"
 * It loads the correct lists and updates the adapter to show the users the correct data
 */

public class TopNavigationHandler extends Fragment {

    private String endpoint = "http://www.wpoppin.com/api/events.json"; //initially for you
    private View currentView;
    private ProgressDialog pDialog;
    private CustomListAdapter adapter;
    private Gson gson;
    private RequestQueue requestQueue;
    private List<Post> eventList = new ArrayList<>();
    private ListView listView;
    private ArrayList<Integer> interest = new ArrayList<>();

    private Button today;
    private Button this_week;
    private Button this_month;
    private Button for_you;
    private Button btnLoadMore;

    private View view;

    SQLSaveEvents db = new SQLSaveEvents(getContext());

    private ArrayList<Button> topBottons = new ArrayList<Button>();

    private int response = 0;


    /**
     * Set the URL to get from the main page
     * @param url
     */
    public void SetURL(String url)
    {
        this.endpoint = url;
    }

    /**
     * Get list based off of what tab is selected
     * @param posts loaded from website
     */
    public void customResponsePerPage(List<Post> posts) {

        for (Post post : posts) {
            switch (response) {
                case 0: //FOR YOU
                    if (interest.contains(Integer.parseInt(post.getCategory()))) {
                        addEvent(post);
                    }
                    break;
                default: //TODAY, THIS WEEK, THIS MONTH
                    addEvent(post);
                    break;
            }
        }
    }

    public void noInternetSQL()
    {
        String saved;
        if(view != null)
            saved = db.getRequest(view.getTag().toString());
        else
            saved = db.getRequest("foryou");
        List<Post> posts = null;
        if(saved != null && !saved.equals("")) {
            try {
                posts = Arrays.asList(gson.fromJson(saved, Post[].class));
            } catch (Exception e)
            {
                if(view == null)
                    db.deleteRequest("foryou");
                else
                    db.deleteRequest(view.getTag().toString());

                posts = null;
            }
        }
        hidePDialog();
        if(posts != null &&posts.size() != 0) {
            Toast.makeText(getContext(), "Connect to the internet for updated information", Toast.LENGTH_LONG).show();
            customResponsePerPage(posts);
        }
        else
            Toast.makeText(getContext(), "No data loaded, please check internet connection", Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
    }

    public void addEvent(Post post)
    {
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        db = new SQLSaveEvents(getContext());
        requestQueue = Volley.newRequestQueue(getActivity());

        //GET INTERESTS
        int[] temp = PrefUtils.getCurrentUser(getActivity()).getInterests();
        for (int index = 0; index < temp.length; index++)
        {
            interest.add(temp[index]);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        listView = (ListView) currentView.findViewById(R.id.listView);
        adapter = new CustomListAdapter(getActivity(), eventList);
        listView.setAdapter(adapter);


        // LoadMore button
        btnLoadMore = new Button(getContext());
        btnLoadMore.setText("Add More Interests");
        btnLoadMore.setBackgroundColor(Color.WHITE);
        btnLoadMore.setBackgroundColor(getResources().getColor(R.color.orange));
        btnLoadMore.setTextColor(Color.WHITE);

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectInterests.class);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new TopNavigationHandler());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                startActivity(intent);
            }
        });
        listView.addFooterView(btnLoadMore);



        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        //get all of the buttons
        today = (Button) currentView.findViewById(R.id.day).findViewById(R.id.today);
        topBottons.add(today);
        this_week = (Button) currentView.findViewById(R.id.day).findViewById(R.id.this_week);
        topBottons.add(this_week);
        this_month = (Button) currentView.findViewById(R.id.day).findViewById(R.id.this_month);
        topBottons.add(this_month);
        for_you = (Button) currentView.findViewById(R.id.day).findViewById(R.id.for_you);
        topBottons.add(for_you);

        //Initially on for you
        for_you.setBackgroundColor(getResources().getColor(R.color.orange));
        for_you.setTextColor(getResources().getColor(R.color.white));


        //set the onclick listener
        for( Button b: topBottons)
        {
            b.setOnClickListener(buttonNavigation());
        }

        ConnectivityManager connectivityManager
                = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
            fetchPosts();
        else
            noInternetSQL();
        super.onActivityCreated(savedInstanceState);
    }

    //If one of the top buttons is selected
    private View.OnClickListener buttonNavigation()
    {
        View.OnClickListener here = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventList.clear();
                requestQueue = Volley.newRequestQueue(getActivity());

                view = v;

                //reset button colors
                for (Button b : topBottons) {
                    b.setBackgroundColor(getResources().getColor(R.color.white));
                    b.setTextColor(getResources().getColor(R.color.black));
                }

                //set selected button to orange
                v.setBackgroundColor(getResources().getColor(R.color.orange));
                ((Button)v).setTextColor(getResources().getColor(R.color.white));

                //SET WHICH TAB
                if(v.getTag().equals("foryou"))
                {
                    response = 0;
                    listView.removeFooterView(btnLoadMore);
                    listView.addFooterView(btnLoadMore);
                    SetURL("http://www.wpoppin.com/api/events.json");
                }
                else if (v.getTag().equals("today"))
                {
                    response = 1;
                    listView.removeFooterView(btnLoadMore);
                    SetURL("http://www.wpoppin.com/api/events_today.json");
                }
                else if(v.getTag().equals("week"))
                {
                    response = 2;
                    listView.removeFooterView(btnLoadMore);
                    SetURL("http://www.wpoppin.com/api/events_this_week.json");
                }
                else if(v.getTag().equals("month"))
                {
                    response = 3;
                    listView.removeFooterView(btnLoadMore);
                    SetURL("http://www.wpoppin.com/api/events_this_month.json");
                }

                //Restart process to update list
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.show();

                ConnectivityManager connectivityManager
                        = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
                    fetchPosts();
                else
                    noInternetSQL();
                listView.setSelectionAfterHeaderView();
            }
        };
        return here;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.activity_top_navigation, container, false);
        return currentView;
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
            if(view != null) {
                db.deleteRequest(view.getTag().toString());
                db.addRequest(view.getTag().toString(), response);
            }
            else {
                db.deleteRequest("foryou");
                db.addRequest("foryou", response);
            }
            List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));
            hidePDialog();
            customResponsePerPage(posts);
            adapter.notifyDataSetChanged();
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.toString();
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
