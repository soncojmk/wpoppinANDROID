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

public class NotificationListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<NotificationClass> Items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Bitmap bitmap;
    private User user;
    private List<String> saving;
    private NotificationClass m;
    private Gson gson;
    static ArrayList<String> savedurl = new ArrayList<>();

    public NotificationListAdapter(Activity activity, List<NotificationClass> Items, String userURL) {
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
            convertView = inflater.inflate(R.layout.notifications_list, null);


        user = PrefUtils.getCurrentUser(activity);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

//        NetworkImageView thumbNail = (NetworkImageView) convertView
//                .findViewById(R.id.avatar);

        TextView actor = (TextView) convertView.findViewById(R.id.username);
        TextView verb = (TextView) convertView.findViewById(R.id.verb);
        //final NetworkImageView avatar = (NetworkImageView) convertView.findViewById(R.id.avatar);
        // getting movie data for the row
        m = Items.get(position);

        /*
        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, EventPage.class);
                i.putExtra("url", urls);
                i.putExtra("title",titles);
                activity.startActivity(i);
            }
        });
        */

        try {
            // thumbnail image
            //thumbNail.setImageUrl(m.getActor_account().getAvatar(), imageLoader);
            verb.setText(m.getVerb());
            actor.setText(m.getActor());

        } catch (Exception e)
        {
            e.printStackTrace();
        }

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

    private RequestQueue requestQueue;
    private void fetchPosts(String endpoint) {
        Log.e("FETCH", endpoint);
        StringRequest request = new StringRequest(Request.Method.GET, endpoint, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<NotificationClass> posted_saved_list = Arrays.asList(gson.fromJson(response, NotificationClass[].class));
            Log.e("SHOW", posted_saved_list.toString());
            for(NotificationClass p : posted_saved_list)
            {
                savedurl.add(p.getActor());
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