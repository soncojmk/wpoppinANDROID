package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;

/**
 * Created by joseph on 3/23/2017.
 */

public class UserPage extends AppCompatActivity {

    private User user;
    private ImageView profileImage;
    Bitmap bitmap;
    private TextView name;
    private TextView school_tv;
    private TextView bio;

    Gson gson;
    ProgressDialog pDialog;
    String url;
    private RequestQueue requestQueue;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private User currentUserPage;
    private List<String> myFollowing;
    private List<String> myRequested;
    private TextView follow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);


        requestQueue = Volley.newRequestQueue(this);
        user=PrefUtils.getCurrentUser(this);

        Intent i = this.getIntent();
        url = i.getStringExtra("url");
        //String title = i.getStringExtra("title");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        currentUserPage = new User();

        myFollowing = new ArrayList<>();
        myRequested = new ArrayList<>();

        follow = (TextView) findViewById(R.id.follow);
        follow.setText("Follow");
        follow.setBackgroundColor(getResources().getColor(R.color.white));
        follow.setTextColor(getResources().getColor(R.color.orange));

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //url = url.replace(".json", "/follow");
                follow(Request.Method.POST, user.getToken(), "http://www.wpoppin.com/api/account/1150/follow/");
                follow.setText("Requested");
            }

        });


//        Log.i("following", myFollowing.toString());
  //      Log.i("requested", myRequested.toString());

        fetchPost();  //currentUserPage is set inside the response listener


    }

    private void fetchPost() {
        StringRequest request = new StringRequest(Request.Method.GET, url, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //set all the data
            Log.e("RESPONSE", response);
            User account = gson.fromJson(response, User.class);

            currentUserPage = account;

            profileImage= (ImageView) findViewById(R.id.profileImage);
            name = (TextView) findViewById(R.id.username);
            school_tv = (TextView)findViewById(R.id.school);
            bio = (TextView) findViewById(R.id.bio);
            //Log.i("account", account.getBio());
            //profileImage = account.getAvatar();

            name.setText(account.user.getUsername());
            school_tv.setText(Integer.toString(account.getCollege()));
            String about = account.getBio();
            bio.setText(about);


            String url_to = account.getUrl().replace(".json", "/");
            getNumber(user.getToken(), url_to + "following");
            //getNumber(user.getToken(), url_to + "followers");

            getFollowing(user.getToken(), "http://www.wpoppin.com/api/accounts/1/following/");
            getRequested(user.getToken(), "http://www.wpoppin.com/api/account/1/requested/");
            Log.i("myfollowing", myFollowing.toString());
            Log.i("myrequested", myRequested.toString());

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // data = error.toString();
            Log.e("ERROR", error.toString());
        }
    };


    private void getNumber(final String token, final String url) {
        StringRequest strreq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "USER" + Response.toString());
                        String json = Response.toString();
                        int num = 0;
                        try {
                            num = (new JSONArray(json)).length();
                        }catch (Exception e)
                        {}
                        if(url.contains("following")) {
                            TextView following = (TextView) findViewById(R.id.following);
                            following.setText(num + "");
                        }else if(url.contains("followers"))
                        {
                            TextView followers = (TextView) findViewById(R.id.followers);
                            followers.setText(num + "");
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
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strreq);
    }


    private void follow (int request, final String token, final String url) {
        StringRequest strreq = new StringRequest(request,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "USER" + Response.toString());

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
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strreq);
    }


    private void getFollowing(final String token, final String url) {
        StringRequest strreq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "REQUESTED" + Response.toString());
                        GsonBuilder gsonBuilder = new GsonBuilder();

                        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                        Gson gson = gsonBuilder.create();

                        List<User> accounts = Arrays.asList(gson.fromJson(Response, User[].class));

                        try {
                            for (User account : accounts) {
                                User follow = new User();
                                follow.setUrl(account.url);
                                follow.setBio(account.about);
                                follow.setUser(account.user);
                                follow.setAvatar(account.avatar);
                                follow.setCollege(account.college);
                                myFollowing.add(account.user.getUsername());
                                Log.i("follow", follow.user.getUsername());

                            }
                            Log.i("followlist", myFollowing.toString());
                            Log.i("cureent", currentUserPage.user.getUsername());

                            if (myFollowing.contains(currentUserPage.user.getUsername())){
                                follow.setText("Following");

                                follow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //url = url.replace(".json", "/follow");
                                        follow(Request.Method.DELETE, user.getToken(), "http://www.wpoppin.com/api/account/1150/follow/");
                                        follow.setText("Follow");
                                    }

                                });
                            }


                        } catch (Exception e) {
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
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strreq);
    }


    private void getRequested(final String token, final String url) {
        StringRequest strreq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "REQUESTED" + Response.toString());
                        GsonBuilder gsonBuilder = new GsonBuilder();

                        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                        Gson gson = gsonBuilder.create();

                        List<User> accounts = Arrays.asList(gson.fromJson(Response, User[].class));

                        try {
                            for (User account : accounts) {
                                User follow = new User();
                                follow.setUrl(account.url);
                                follow.setBio(account.about);
                                follow.setUser(account.user);
                                follow.setAvatar(account.avatar);
                                follow.setCollege(account.college);
                                myRequested.add(account.user.getUsername());
                                Log.i("follow", follow.user.getUsername());

                            }
                            Log.i("followlist", myRequested.toString());
                            Log.i("cureent", currentUserPage.user.getUsername());

                            if (myRequested.contains(currentUserPage.user.getUsername())){
                                follow.setText("Requested");

                                follow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //url = url.replace(".json", "/follow");
                                        follow(Request.Method.DELETE, user.getToken(), "http://www.wpoppin.com/api/account/1150/follow/");
                                        follow.setText("Follow");
                                    }

                                });
                            }
                        } catch (Exception e) {
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
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strreq);

    }

}

