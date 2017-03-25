package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;

/**
 * Created by joseph on 3/24/2017.
 */

public class FollowRequests extends AppCompatActivity {

    private User user;
    private ImageView profileImage;
    Bitmap bitmap;
    private TextView name;
    private TextView school_tv;
    private TextView bio;

    ProgressDialog pDialog;
    String url;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<User> accountList = new ArrayList<>();
    private ListView listView;
    private FollowRequestsListAdapater adapter;
    private Gson gson;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_requests);


        requestQueue = Volley.newRequestQueue(this);
        user = PrefUtils.getCurrentUser(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        listView = (ListView) findViewById(R.id.requesting_list);
        adapter = new FollowRequestsListAdapater(this, accountList);
        listView.setAdapter(adapter);

        Intent i = getIntent();
  //      url = i.getStringExtra("url");
//        url.replace(".json", "/requesting");
        fetchPosts(user.getToken());


    }

    public void addAccount(User user)
    {
        User account = new User();
        account.setUrl(user.url);
        account.setBio(user.about);
        account.setUser(user.user);
        account.setAvatar(user.avatar);
        account.setCollege(user.college);
        accountList.add(account);
    }

    public void customResponsePerPage(List<User> accounts) {
        for (User account : accounts) {
            addAccount(account);
        }
    }

    private void fetchPosts(final String token) {
        String url = "http://www.wpoppin.com/api/accounts/1/requesting";
        StringRequest strreq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "USER" + Response.toString());
                        String json = Response.toString();
                        //split the json string to get the token value then store it in local memory
                        List<User> accounts = Arrays.asList(gson.fromJson(Response, User[].class));

                        try {
                            for (User account : accounts) {
                                addAccount(account);

                            }
                            adapter.notifyDataSetChanged();

                        }catch (Exception e)
                        {
                            Log.e(TAG, "USER " + e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Token a9edb73eb1ecfa66b87037cbfeada07406749f96");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strreq);

    }


}
