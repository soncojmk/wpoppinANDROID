package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Abby on 2/21/2017.
 */

public class TopNavigationHandler extends Fragment {

    private String endpoint;
    private View currentView;
    private ProgressDialog pDialog;
    private CustomListAdapter adapter;
    private String data = "";
    private Gson gson;
    private RequestQueue requestQueue;
    private List<Post> eventList = new ArrayList<Post>();
    private ListView listView;

    private TextView author;
    private TextView title;
    private TextView price;
    private TextView description;
    private TextView date;

    private View today;
    private View this_week;
    private View this_month;
    private View for_you;

    private int response = 0;


    /**
     * Set the URL to get from the main page
     * @param url
     */
    public void SetURL(String url)
    {
        this.endpoint = url;
    }

    public void customResponsePerPage(List<Post> posts) {
        switch (response){
            case 0: //FOR YOU
                //// TODO: 2/22/2017
                break;
            case 1: //TODAY
                //TODO
                break;
            case 2: //THIS WEEK
                //TODO
                break;
            case 3: //THIS MONTH
                //TODO
                break;
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(getActivity());

        author = (TextView) currentView.findViewById(R.id.author);
        title = (TextView) currentView.findViewById(R.id.title);
        price = (TextView) currentView.findViewById(R.id.price);
        description = (TextView) currentView.findViewById(R.id.description);
        date = (TextView) currentView.findViewById(R.id.date);

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

        today = (View) currentView.findViewById(R.id.day).findViewById(R.id.today);
        this_week = (View) currentView.findViewById(R.id.day).findViewById(R.id.this_week);
        this_month = (View) currentView.findViewById(R.id.day).findViewById(R.id.this_month);
        for_you = (View) currentView.findViewById(R.id.day).findViewById(R.id.for_you);



        fetchPosts();
        super.onActivityCreated(savedInstanceState);
    }

    private View.OnClickListener onClickListener()
    {
        View.OnClickListener here = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SET WHICH RESPONSE
                fetchPosts();
            }
        };
        return here;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.activity_json, container, false);
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
            List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));
            hidePDialog();

            customResponsePerPage(posts);


            adapter.notifyDataSetChanged();

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // Log.e("JS" + "ON", error.toString());
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
