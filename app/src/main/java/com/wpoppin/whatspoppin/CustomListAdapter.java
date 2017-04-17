package com.wpoppin.whatspoppin;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import static com.wpoppin.whatspoppin.AppController.TAG;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> Items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Bitmap bitmap;
    private User user;
    private List<String> saving;
    private Post m;
    private Gson gson;
    static ArrayList<String> savedurl = new ArrayList<>();

    public CustomListAdapter(Activity activity, List<Post> Items, String userURL) {
        this.activity = activity;
        this.Items = Items;
        try {
            requestQueue = Volley.newRequestQueue(activity);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();
            fetchPosts(userURL + "saved/");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int location) {
        return Items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.event_custom_list_view, null);


        user = PrefUtils.getCurrentUser(activity);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        LinearLayout save = (LinearLayout) convertView.findViewById(R.id.save);
        LinearLayout share = (LinearLayout) convertView.findViewById(R.id.share);
        final NetworkImageView avatar = (NetworkImageView) convertView.findViewById(R.id.avatar);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        final ImageButton saveImage = (ImageButton)convertView.findViewById(R.id.saveimage);

        // getting movie data for the row
        m = Items.get(position);

        //data for m
        final String titles = m.getTitle();
        final String urls = m.getUrl();
        final String TITLE = fixEncoding(m.getTitle());
        final User account = m.account;
        final String s =  fixEncoding(m.getDescription());
        final String token = user.getToken();

        // thumbnail image
        thumbNail.setImageUrl(m.getImage(), imageLoader);

        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, EventPage.class);
                i.putExtra("url", urls);
                i.putExtra("title",titles);
                activity.startActivity(i);
            }
        });

      //  author.setText("By " + m.getAuthor()))
        title.setText(TITLE);

        try {
            avatar.setImageUrl(m.account.getAvatar(), imageLoader);
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        username.setText(m.getAuthor());
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, UserPage.class);
                String url = account.getUrl();
                i.putExtra("url", url);
                activity.startActivity(i);
            }
        });

        if(savedurl.contains(m.getUrl()))
        {
            saveImage.setImageResource(R.drawable.ic_bookmark);
        }
        else
        {
            saveImage.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDataToServer.follow(Request.Method.POST, token, urls+ "save/");
                if(savedurl.contains(urls))
                {
                    Log.e("REMOVE", urls);
                    saveImage.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    savedurl.remove(savedurl.indexOf(urls));
                }
                else
                {
                    Log.e("SAVE", urls);
                    saveImage.setImageResource(R.drawable.ic_bookmark);
                    savedurl.add(urls);
                }
                notifyDataSetChanged();
            }
        });

        String dateString = m.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("  EE'\n'MMM d");
        String newFormat = formatter.format(convertedDate);


        String timeString = m.getTime();
        Date dateObj = new Date();
        try {
             SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
             dateObj = sdf.parse(timeString);

        } catch ( ParseException e) {
            e.printStackTrace();
        }

        final String outputTime = new SimpleDateFormat("h:mm aa").format(dateObj);
        final String intentTime = new SimpleDateFormat("HH:mm:ss").format(dateObj);

        date.setText(newFormat);


        String x = fixEncoding(m.getAddress());

        time.setText(outputTime);

        final String calendarObject = m.getDate() + " " + m.getTime();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date date1 = sdf.parse(calendarObject);
        }catch (Exception e){
            e.printStackTrace();
        }

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "What'sPoppin event: " + s + '\n' + m.getEventShareUrl();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Do you wanna go to this? " + TITLE);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

        return convertView;
    }

    public static String fixEncoding(String latin1) {
        try {
            byte[] bytes = latin1.getBytes("ISO-8859-1");
            if (!validUTF8(bytes))
                return latin1;
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Impossible, throw unchecked
            throw new IllegalStateException("No Latin1 or UTF-8: " + e.getMessage());
        }

    }

    public static boolean validUTF8(byte[] input) {
        int i = 0;
        // Check for BOM
        if (input.length >= 3 && (input[0] & 0xFF) == 0xEF
                && (input[1] & 0xFF) == 0xBB & (input[2] & 0xFF) == 0xBF) {
            i = 3;
        }

        int end;
        for (int j = input.length; i < j; ++i) {
            int octet = input[i];
            if ((octet & 0x80) == 0) {
                continue; // ASCII
            }

            // Check for UTF-8 leading byte
            if ((octet & 0xE0) == 0xC0) {
                end = i + 1;
            } else if ((octet & 0xF0) == 0xE0) {
                end = i + 2;
            } else if ((octet & 0xF8) == 0xF0) {
                end = i + 3;
            } else {
                // Java only supports BMP so 3 is max
                return false;
            }

            while (i < end) {
                i++;
                octet = input[i];
                if ((octet & 0xC0) != 0x80) {
                    // Not a valid trailing byte
                    return false;
                }
            }
        }
        return true;
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
//                        description.setText(num + " " + " people save this" + s);

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


    private void getSaved(final String token, final String url) {
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


                        try {  //adds the people saving the event to the saving list
                            for (User account : accounts) {
                                User follow = new User();
                                follow.setUrl(account.url);
                                follow.setBio(account.about);
                                follow.setUser(account.user);
                                follow.setAvatar(account.avatar);
                                follow.setCollege(account.college);
                                saving.add(account.user.getUsername());
                                //Log.i("save", follow.user.getUsername());

                            }
                            //Log.i("savinglist", saving.toString());
                            //Log.i("cureent", currentUserPage.user.getUsername());


                        } catch (Exception e) {
                            Log.e(TAG, "ERROR USER " + e.toString());
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
    private RequestQueue requestQueue;
    private void fetchPosts(String endpoint) {
        Log.e("FETCH", endpoint);
        StringRequest request = new StringRequest(Request.Method.GET, endpoint, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<Post> posted_saved_list = Arrays.asList(gson.fromJson(response, Post[].class));
            Log.e("SHOW", posted_saved_list.toString());
            for(Post p : posted_saved_list)
            {
                savedurl.add(p.getUrl());
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.toString();
            Log.e("Fetch Posted/Saved", error.toString());
        }
    };



}