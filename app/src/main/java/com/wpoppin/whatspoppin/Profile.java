package com.wpoppin.whatspoppin;

/**
 * Created by joseph on 12/24/2016.
 * This is the user profile page where the user can logout and see their info
 *
 *
 * Updated 3/10/2017 by Abagail Tarosky
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;
import static com.wpoppin.whatspoppin.PostDataToServer.UpdatePatch;

public class Profile extends Fragment {

    private User user;
    private ImageView profileImage;
    Bitmap bitmap;
    private TextView name;
    private TextView school_tv;
    private TextView bio;

    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }
    private View view;

    public Profile(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        user=PrefUtils.getCurrentUser(getActivity());
        profileImage= (ImageView) view.findViewById(R.id.profileImage);
        name = (TextView) view.findViewById(R.id.username);
        school_tv = (TextView)view.findViewById(R.id.school);
        bio = (TextView) view.findViewById(R.id.bio);
        bio.setText("Hi, tell us about your favorite event");
        Button requesting = (Button) view.findViewById(R.id.requesting);

        user.setUrl(url_to);
        PrefUtils.setCurrentUser(user, getActivity());

        convertToken(user.getToken());
        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("USER URL TO ", url_to);
               //PostDataToServer.UpdatePatch(getActivity(), url_to, user.getToken(), "android gods pls work", 2);
                PostDataToServer.PostEvent(getContext(), user.getToken());


            }
        });

        requesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FollowRequests.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                i.putExtra("url", url_to);



                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                startActivity(i);
            }
        });



        FloatingActionButton settings = (FloatingActionButton)view.findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                Intent i = new Intent(getActivity(), Settings.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                startActivity(i);
            }
        });

        name.setText(user.username);

        // fetching facebook's profile picture
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {

                    imageURL = new URL("https://graph.facebook.com/" + user.facebookID + "/picture?type=large");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                profileImage.setImageBitmap(bitmap);
            }
        }.execute();
        super.onActivityCreated(savedInstanceState);
    }

    String url_to;
    String username;
    String email;
    String avatar;
    String about;
    String college;

    private void convertToken(final String token) {
        String url = "http://www.wpoppin.com/api/myaccount/";
        StringRequest strreq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "USER" + Response.toString());
                        String json = Response.toString();
                        //split the json string to get the token value then store it in local memory
                        try {
                            JSONArray jsonObj = new JSONArray(json);
                            url_to = jsonObj.getJSONObject(0).getString("url");
                            JSONObject ja = jsonObj.getJSONObject(0).getJSONObject("user");
                            username = ja.getString("username");
                            email = ja.getString("email");
                            avatar = jsonObj.getJSONObject(0).getString("avatar");
                            about = jsonObj.getJSONObject(0).getString("about");
                            college = jsonObj.getJSONObject(0).getString("college");
                            if(college.equals("1"))
                                college = "Penn State University";
                            else
                                college = "Temple";

                            if(about.equals("null"))
                                about = "Click to enter a Bio";

                            name.setText(username);
                            bio.setText(about);
                            school_tv.setText(college);

                            getNumber(user.getToken(), url_to + "following");
                            getNumber(user.getToken(), url_to + "followers");


                        }catch (JSONException e)
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
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strreq);

    }

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
                            TextView following = (TextView) view.findViewById(R.id.following);
                            following.setText(num + "");
                        }else
                        {
                            TextView followers = (TextView) view.findViewById(R.id.followers);
                            followers.setText(num + "");
                        }
                        //split the json string to get the token value then store it in local memory

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


}