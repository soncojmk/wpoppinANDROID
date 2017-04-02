package com.wpoppin.whatspoppin;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.R.attr.description;
import static com.wpoppin.whatspoppin.AppController.TAG;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> Items;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Bitmap bitmap;
    private ExpandableTextView description;
    private String s;
    private User user;
    private List<String> saving;
    private ImageButton save;
    private Post m;

    public CustomListAdapter(Activity activity, List<Post> Items) {
        this.activity = activity;
        this.Items = Items;
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

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        //TextView author = (TextView) convertView.findViewById(R.id.author);
        TextView title = (TextView) convertView.findViewById(R.id.title);
       // TextView price = (TextView) convertView.findViewById(R.id.price);
       // description = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
        TextView date = (TextView) convertView.findViewById(R.id.date);
       // TextView address = (TextView) convertView.findViewById(
       //         R.id.address);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        save = (ImageButton) convertView.findViewById(R.id.save);
        ImageButton share = (ImageButton) convertView.findViewById(R.id.share);
        final NetworkImageView avatar = (NetworkImageView) convertView.findViewById(R.id.avatar);
        TextView username = (TextView) convertView.findViewById(R.id.username);

        user = PrefUtils.getCurrentUser(activity);


        // getting movie data for the row
        m = Items.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getImage(), imageLoader);

        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, EventPage.class);
                String url = m.getUrl();
                i.putExtra("url", url);
                i.putExtra("title",m.getTitle());
                activity.startActivity(i);
            }
        });

      //  author.setText("By " + m.getAuthor());
        final String TITLE = fixEncoding(m.getTitle());
        title.setText(TITLE);

        avatar.setImageUrl(m.account.getAvatar(), imageLoader);


    //    if(m.getPrice() == 0){
    //        price.setText("Free");
    //    }

    //    else if(m.getTicket_link() != null){
    //        price.setText("$" + String.valueOf(m.getPrice()));
    //    }

    //    else {
    //        price.setText(String.valueOf(m.getPrice()));
    //    }

        final User account = m.account;

        username.setText(m.getAuthor());
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, UserPage.class);
                String url = account.getUrl();
                i.putExtra("url", url);
                //i.putExtra("title",m.getAuthor());
                activity.startActivity(i);
            }
        });



          s =  fixEncoding(m.getDescription());
           //description.setText(s);

        saving = new ArrayList<>();
        getNumber(user.getToken(), m.getUrl() + "people_saving/");


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
      //      address.setText(x);


        time.setText(outputTime);

        final String calendarObject = m.getDate() + " " + m.getTime();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date date1 = sdf.parse(calendarObject);
        }catch (Exception e){
            e.printStackTrace();
        }

        getSaved(user.getToken(), m.getUrl() + "people_saving/");


        /*
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Calendar beginTime = sdf.getCalendar();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
                //intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                intent.putExtra(CalendarContract.Events.TITLE, TITLE);
                intent.putExtra(CalendarContract.Events.DESCRIPTION, s);
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, m.getAddress() +", " + m.getCity() + ", " + m.getState());
                intent.putExtra(CalendarContract.Events.RRULE, false);

                v.getContext().startActivity(intent);

            }
        });
        */

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

        // routine to add reminders with the event
        // + ", " + m.getCity() + ", " + m.getState()

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
                                Log.i("save", follow.user.getUsername());

                            }
                            Log.i("savinglist", saving.toString());
                            //Log.i("cureent", currentUserPage.user.getUsername());


                            //checks whether the person saving the current user has saved the event
                            if (saving.contains(user.getUsername())){
                                save.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));

                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //url = url.replace(".json", "/follow");
                                        PostDataToServer.follow(Request.Method.DELETE, user.getToken(), m.getUrl()+"save/");
                                        save.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                    }
                                });
                            }
                            else{
                                save.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PostDataToServer.follow(Request.Method.POST, user.getToken(), m.getUrl() + "save/");
                                        save.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
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