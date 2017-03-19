package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Debug;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.level;
import static com.wpoppin.whatspoppin.CustomListAdapter.fixEncoding;

public class EventPage extends AppCompatActivity {

    Gson gson;
    ProgressDialog pDialog;
    String url;
    private RequestQueue requestQueue;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        requestQueue = Volley.newRequestQueue(this);

        Intent i = getIntent();
        url = i.getStringExtra("url");
        String title = i.getStringExtra("title");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
//
//        Toolbar toolbar = (Toolbar)findViewById(R.id.main_menu); // Attaching the layout to the toolbar object
//        this.setSupportActionBar(toolbar);
//        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
//        this.getSupportActionBar().setTitle(title);

        fetchPost();
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
            pDialog.dismiss();
            Post post = gson.fromJson(response, Post.class);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            NetworkImageView thumbNail = (NetworkImageView)findViewById(R.id.thumbnail);
            // thumbnail image
            thumbNail.setImageUrl(post.getImage(), imageLoader);

            SetData(post);
        }
    };


    private void SetData(final Post m)
    {
        TextView title = (TextView)findViewById(R.id.title);
        TextView price = (TextView)findViewById(R.id.price);
        TextView date = (TextView)findViewById(R.id.date);
        TextView address = (TextView)findViewById(R.id.address);
        TextView time = (TextView)findViewById(R.id.time);
        ImageButton save = (ImageButton)findViewById(R.id.save);
        TextView description = (TextView) findViewById(R.id.description);
        ImageButton share = (ImageButton)findViewById(R.id.share);

        //  author.setText("By " + m.getAuthor());
        final String TITLE = fixEncoding(m.getTitle());
        title.setText(TITLE);


        if(m.getPrice() == 0){
            price.setText("Free");
        }

        else if(m.getTicket_link() != null){
            price.setText("$" + String.valueOf(m.getPrice()));
        }

        else {
            price.setText(String.valueOf(m.getPrice()));
        }

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

        final String s =  fixEncoding(m.getDescription());
        description.setText("By " + m.getAuthor() + ": " + s);


        String x = fixEncoding(m.getAddress());
        address.setText(x);


        time.setText(outputTime);

        final String calendarObject = m.getDate() + " " + m.getTime();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date date1 = sdf.parse(calendarObject);
        }catch (Exception e){
            e.printStackTrace();
        }


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

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
              //  String shareBody = "What'sPoppin event: " + s + '\n' + m.getEventShareUrl();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Do you wanna go to this? " + TITLE);
             //   sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
    }


    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           // data = error.toString();
            Log.e("ERROR", error.toString());
        }
    };
}
