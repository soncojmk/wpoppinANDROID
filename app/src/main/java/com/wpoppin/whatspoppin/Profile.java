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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.wpoppin.whatspoppin.AppController.TAG;

public class Profile extends Fragment {

    private User user;
    private ImageView profileImage;
    private ImageButton profileImageedit;
    Bitmap bitmap;
    private TextView name;
    private Spinner school_et;
    private EditText bio_et;
    private TextView school_tv;
    private TextView bio_tv;

    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }

    private View view;

    public Profile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar); // Attaching the layout to the toolbar object
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setHasOptionsMenu(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        return view;
    }

    Button notifCount;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        notifCount = (Button) MenuItemCompat.getActionView(item);
        notifCount.setOnClickListener(new View.OnClickListener() {
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent i = new Intent(getActivity(), Settings.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            i.putExtra("url", url_to);


            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            startActivity(i);
            return true;
        }

        else if (item.getItemId() == R.id.add) {
            Intent i = new Intent(getActivity(), RecommendedFollow.class);
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

        user = PrefUtils.getCurrentUser(getActivity());
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        profileImageedit = (ImageButton) view.findViewById(R.id.profileImagenew);
        profileImageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 100);
            }
        });

        name = (TextView) view.findViewById(R.id.username);
        school_tv = (TextView) view.findViewById(R.id.schooltv);
        school_et = (Spinner) view.findViewById(R.id.schoolet);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList("Penn State University", "Temple")));
        school_et.setAdapter(sAdapter);
        bio_tv = (TextView) view.findViewById(R.id.bio_tv);
        bio_et = (EditText) view.findViewById(R.id.bio_et);
        bio_tv.setText("Hi, tell us about your favorite event");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("" + user.getUsername());
        convertToken(user.getToken());



        ImageButton edit = (ImageButton)view.findViewById(R.id.settings);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("USER", "Edit");
                ViewSwitcher bio = (ViewSwitcher)view.findViewById(R.id.bio_switch);
                bio.showNext(); //or switcher.showPrevious();

                ViewSwitcher school = (ViewSwitcher)view.findViewById(R.id.school);
                school.showNext();

                ViewSwitcher profile = (ViewSwitcher)view.findViewById(R.id.image_switch);
                profile.showNext();

                ViewSwitcher im = (ViewSwitcher)view.findViewById(R.id.image_edit_switch);
                im.showNext();


            }
        });

        ImageButton save = (ImageButton)view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String b = bio_et.getText().toString();
                int n = school_et.getSelectedItemPosition() + 1;
                bio_tv.setText(b);
                if(n==1) {
                    school_tv.setText("Penn State University");
                }
                else {
                    school_tv.setText("Temple");
                }

                PostDataToServer.UpdatePatch(getActivity(), url_to + "/update_profile/", user.getToken(), b, n, imageString);

                Log.i("url_to", url_to);

                ViewSwitcher bio = (ViewSwitcher)view.findViewById(R.id.bio_switch);
                bio.showNext(); //or switcher.showPrevious();

                ViewSwitcher profile = (ViewSwitcher)view.findViewById(R.id.image_switch);
                profile.showNext();

                ViewSwitcher school = (ViewSwitcher)view.findViewById(R.id.school);
                school.showNext();

                ViewSwitcher im = (ViewSwitcher)view.findViewById(R.id.image_edit_switch);
                im.showNext();

            }
        });

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Add_Event.class);
                i.putExtra("token", user.getToken());
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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {

                    imageURL = new URL("https://graph.facebook.com/" + user.facebookID + "/picture?type=large");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

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

    String imageString = null;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 100:
                if(resultCode == RESULT_OK){
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        profileImage.setImageBitmap(yourSelectedImage);
                        profileImageedit.setImageBitmap(yourSelectedImage);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();
                        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
        }
    }

    Bitmap bit;
    private void convertToken(final String token) {
        final String url = "http://www.wpoppin.com/api/myaccount/";
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
                            school_et.setSelection(Integer.parseInt(college) - 1);
                            if (college.equals("1"))
                                college = "Penn State University";
                            else
                                college = "Temple";

                            if (about.equals("null"))
                                about = "Click to enter a Bio";

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    try {
                                        Log.e("USER", "IL " + avatar);
                                        bit = BitmapFactory.decodeStream((InputStream) new URL(avatar).getContent());

                                    }catch (Exception e)
                                    {
                                        Log.e("Avatar Error", e.toString());
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    profileImage.setImageBitmap(bit);
                                    profileImageedit.setImageBitmap(bit);
                                }
                            }.execute();

                            name.setText(username);
                            bio_et.setText(about);
                            bio_tv.setText(about);
                            school_tv.setText(college);

                            getNumber(user.getToken(), url_to + "following");
                            getNumber(user.getToken(), url_to + "followers");
                            getNumber(user.getToken(), url_to + "requesting");

                        } catch (JSONException e) {
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
                        } catch (Exception e) {
                        }
                        if (url.contains("following")) {
                            TextView following = (TextView) view.findViewById(R.id.following);
                            following.setText(num + "");
                        } else if (url.contains("followers")){
                            TextView followers = (TextView) view.findViewById(R.id.followers);
                            followers.setText(num + "");
                        }
                        else {
                            if(notifCount != null)
                                notifCount.setText(num + "");
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