package com.wpoppin.whatspoppin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;

public class Add_Event extends AppCompatActivity {

    EditText title;
    EditText desc;
    EditText sadd;
    EditText scity;
    EditText szip;
    EditText price;
    EditText ticket;

    Spinner mm;
    Spinner dd;
    Spinner yyyy;
    Spinner hour;
    Spinner minute;
    Spinner am;

    String imageString;

    Spinner state;

    Button image;
    String token;

    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        Intent in = getIntent();
        token = in.getStringExtra("token");
        View d = findViewById(R.id.add_event);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Add Event");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_action_arrow_left);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ArrayList<String> num = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10)
                num.add("0" + i);
            else
                num.add(i + "");
        }

        title = (EditText)d.findViewById(R.id.title);
        desc = (EditText)d.findViewById(R.id.description);
        sadd = (EditText)d.findViewById(R.id.street);
        scity = (EditText)d.findViewById(R.id.city);
        szip = (EditText)d.findViewById(R.id.zip);
        price = (EditText)d.findViewById(R.id.money);
        ticket = (EditText)d.findViewById(R.id.ticket);

        //  PostDataToServer.UpdatePatch(getActivity(), url_to, user.getToken(), "heehe", 1);
        mm = (Spinner) d.findViewById(R.id.mm);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_item, num.subList(1, 13));
        mm.setAdapter(categoriesAdapter);

        dd = (Spinner) d.findViewById(R.id.dd);
        ArrayAdapter<String> ddAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item, num.subList(1, 32));
        dd.setAdapter(ddAdapter);

        yyyy = (Spinner) d.findViewById(R.id.yyyy);
        ArrayAdapter<String> yyyyAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item, new ArrayList<String>(Arrays.asList("2017", "2018",
                "2019", "2020", "2021", "2022")));
        yyyy.setAdapter(yyyyAdapter);

        hour = (Spinner) d.findViewById(R.id.hour);
        hour.setAdapter(categoriesAdapter);

        minute = (Spinner) d.findViewById(R.id.minute);
        ArrayAdapter<String> minAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item, num);
        minute.setAdapter(minAdapter);

        am = (Spinner) d.findViewById(R.id.am);
        ArrayAdapter<String> amAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item,
                new ArrayList<>(Arrays.asList("pm", "am")));
        am.setAdapter(amAdapter);

        state = (Spinner) d.findViewById(R.id.state);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item,
                getResources().getStringArray(R.array.us_states));
        state.setAdapter(stateAdapter);

        image = (Button)d.findViewById(R.id.thumbnail);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        image.setText(" Image Selected");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_event_menu, menu);

        MenuItem item = menu.findItem(R.id.submit);
        MenuItemCompat.setActionView(item, R.layout.feed_submit);
        Button submit = (Button) MenuItemCompat.getActionView(item);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToken();
            }
        });
        submit.setText("Submit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    private void convertToken() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://www.wpoppin.com/api/events/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("category", "1");
            jsonBody.put("title", title.getText().toString());
            jsonBody.put("street_address", sadd.getText().toString());
            jsonBody.put("city", scity.getText().toString());
            jsonBody.put("state", state.getItemAtPosition(state.getSelectedItemPosition()).toString());
            jsonBody.put("zip_code", szip.getText().toString());
            jsonBody.put("date", "");
            jsonBody.put("time", (am.getSelectedItem().toString().equals("am")?hour.getSelectedItem().toString() : "" +(Integer.parseInt(hour.getSelectedItem().toString()) + 12)) + ":" + minute.getSelectedItem().toString() + ":00");
            jsonBody.put("description", desc.getText().toString());
            jsonBody.put("price", price.toString());
            jsonBody.put("image", imageString);
            jsonBody.put("ticket_link", ticket.toString());

            //jsonBody.put("Author", "BNK");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    Log.i(TAG, "itworks" + response.toString());
                    String json = response.toString();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new ArrayMap<String, String>();
                    headers.put("Authorization", "Token " + token);
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
