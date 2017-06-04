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
import android.support.v4.util.ArrayMap;
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
import java.util.Map;

/**
 * Created by joseph on 6/3/2017.
 */

public class NotificationsFeedPage extends Fragment {
    private String endpoint = "http://www.wpoppin.com/api/notificationfeed/";
    private View currentView;
    private ProgressDialog pDialog;
    private NotificationListAdapter adapter;
    private Gson gson;
    private RequestQueue requestQueue;
    private static List<NotificationClass> notificationList = new ArrayList<>();
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
     * @param notifications loaded from website
     */
    public void customResponsePerPage(List<NotificationClass> notifications) {

        for (NotificationClass notification : notifications) {
                addNotification(notification);
        }
    }

    public void noInternetSQL()
    {
        String saved = db.getRequest("foryou");
        List<NotificationClass> posts = null;
        if(saved != null) {
            try {
                posts = Arrays.asList(gson.fromJson(saved, NotificationClass[].class));
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

    public void addNotification (NotificationClass notification)
    {
        NotificationClass newNotification = new NotificationClass();
        newNotification.setActor(notification.actor);
        newNotification.setUnread(notification.unread);
        newNotification.setAction_object_event(notification.action_object_event);
        newNotification.setActor_account(notification.actor_account);
        newNotification.setVerb(notification.verb);

        notificationList.add(newNotification);
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

        listView = (ListView) currentView.findViewById(R.id.notifications_list);
        adapter = new NotificationListAdapter(getActivity(), notificationList, PrefUtils.getCurrentUser(getContext()).getUrl());
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
       // listView.addFooterView(btnLoadMore);



        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        ConnectivityManager connectivityManager
                = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
            fetchPosts();
        else
            noInternetSQL();
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.activity_notifications_feed, container, false);
        return currentView;
    }



    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }


    private void fetchPosts() {
        final User user = PrefUtils.getCurrentUser(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, endpoint, onPostsLoaded, onPostsError) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            public Map<String, String> getHeaders() {
                Map<String, String> headers = new ArrayMap<String, String>();
                headers.put("Authorization", "Token " + user.getToken());
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            db.deleteRequest("foryou");
            db.addRequest("foryou", response);
            List<NotificationClass> posts = Arrays.asList(gson.fromJson(response, NotificationClass[].class));
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
