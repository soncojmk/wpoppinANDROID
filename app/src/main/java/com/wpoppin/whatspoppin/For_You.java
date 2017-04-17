package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

/**
 * Created by Abby on 4/16/2017.
 */

public class For_You extends Fragment {
    private String endpoint = "http://www.wpoppin.com/api/filteredevents/"; //initially for you
    private View currentView;
    private ProgressDialog pDialog;
    private CustomListAdapter adapter;
    private Gson gson;
    private RequestQueue requestQueue;
    private static List<Post> eventList = new ArrayList<>();
    private ListView listView;
    private ArrayList<Integer> interest = new ArrayList<>();

    private Button btnLoadMore;
    SQLSaveEvents db = new SQLSaveEvents(getContext());


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
            if (interest.contains(Integer.parseInt(post.getCategory()))) {
                addEvent(post);
            }
        }
    }

    public void noInternetSQL()
    {
        String saved = db.getRequest("foryou");
        List<Post> posts = null;
        if(saved != null) {
            try {
                posts = Arrays.asList(gson.fromJson(saved, Post[].class));
            } catch (Exception e)
            {
                db.deleteRequest("foryou");
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
        User account = new User();
        account = post.account;
        if(post.author != null)
            account.setUsername(post.author);
        // account.setBio(post.account.getBio());

        event.setAccount(account);

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
        adapter = new CustomListAdapter(getActivity(), eventList, PrefUtils.getCurrentUser(getContext()).getUrl());
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

        ConnectivityManager connectivityManager
                = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(eventList.size() != 0) {
            pDialog.dismiss();
        }
        else if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
            fetchPosts();
        else
            noInternetSQL();
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.activity_for_you, container, false);
        return currentView;
    }



    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }


    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, endpoint, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            db.deleteRequest("foryou");
            db.addRequest("foryou", response);
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
