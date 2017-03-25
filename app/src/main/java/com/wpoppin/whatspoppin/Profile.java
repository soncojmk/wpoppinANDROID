package com.wpoppin.whatspoppin;

/**
 * Created by joseph on 12/24/2016.
 * This is the user profile page where the user can logout and see their info
 *
 *
 * Updated 3/10/2017 by Abagail Tarosky
 */

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;

public class Profile extends Fragment {

    private User user;
    private ImageView profileImage;
    Bitmap bitmap;
    private TextView name;
    private TextView school_tv;
    private TextView bio;
    Spinner mm;
    Spinner dd;
    Spinner yyyy;
    Spinner hour;
    Spinner minute;
    Spinner am;
    Spinner state;

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

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar); // Attaching the layout to the toolbar object
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setHasOptionsMenu(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent i = new Intent(getActivity(), Settings.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            startActivity(i);
            return true;
        }
        return false;
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("" + user.getUsername());
        convertToken(user.getToken());

        FloatingActionButton add = (FloatingActionButton)view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(getContext());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.profile_popup);
                d.show();

                ArrayList<String> num = new ArrayList<>();
                for(int i = 0; i < 60; i++) {
                    if(i < 10)
                        num.add("0" + i);
                    else
                        num.add(i + "");
                }

                mm = (Spinner)d.findViewById(R.id.mm);
                ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, num.subList(1, 13));
                mm.setAdapter(categoriesAdapter);

                dd = (Spinner)d.findViewById(R.id.dd);
                ArrayAdapter<String> ddAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, num.subList(1,32));
                dd.setAdapter(ddAdapter);

                yyyy = (Spinner)d.findViewById(R.id.yyyy);
                ArrayAdapter<String> yyyyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<String>(Arrays.asList("2017", "2018",
                        "2019", "2020", "2021", "2022")));
                yyyy.setAdapter(yyyyAdapter);

                hour = (Spinner)d.findViewById(R.id.hour);
                hour.setAdapter(categoriesAdapter);

                minute = (Spinner)d.findViewById(R.id.minute);
                ArrayAdapter<String> minAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, num);
                minute.setAdapter(minAdapter);

                am = (Spinner)d.findViewById(R.id.am);
                ArrayAdapter<String> amAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                        new ArrayList<>(Arrays.asList("pm", "am")));
                am.setAdapter(amAdapter);

                state = (Spinner)d.findViewById(R.id.state);
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                        R.array.us_states);


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
                        Log.i(TAG, "USER" + Response);
                        String json = Response;
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

                            bio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("USER URL TO ", url_to);
                                    UpdatePatch(user.getToken());
                                }
                            });

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
                Map<String, String> params = new HashMap<>();
                return params;

            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
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
                        Log.i(TAG, "USER" + Response);
                        String json = Response;
                        int num = 0;
                        try {
                            num = (new JSONArray(json)).length();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
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

    private void UpdatePatch(final String Password) {
        StringRequest strreq = new StringRequest(Request.Method.POST,
                "http://www.wpoppin.com/api/accounts/922/update_profile/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "RESPONDE" + Response.toString());
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
                params.put("about", "asd;kfj");

                return params;

            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Token " + Password);
                return headers;
            }


        };
        AppController.getInstance().addToRequestQueue(strreq);


    }


}