package com.wpoppin.whatspoppin;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;

/**
 * Created by joseph on 3/22/2017.
 */

public class PostDataToServer {


    public static void UpdatePatch(Context context, String url, final String token, String about, int college) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = url;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("about", about);
            jsonBody.put("college", Integer.toString(college));

            //jsonBody.put("Author", "BNK");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL, new Response.Listener<String>() {
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


    public static void PostEvent(final Context context, final String token) {
        final String parameter;
        final Map<String, String> params = new ArrayMap<String, String>();
        params.put("title", "What'sPoppin Presents:TV Dinners");
        params.put("description", "Come out to the second What'sPoppin Presents event of the semester: TV Dinner's live at the launchbox. TV Dinner's is an up and coming local Penn State student band that has been playing around State College for the last year. Their fusion of indie and folk music sets them apart from all other bands around the area. Their potential is infinite! \n \n Admission is free with a download and sign-up of the What'sPoppin App.");


        final Response.Listener responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response.toString());
                Log.i(TAG, "itworks" + response.toString());
                String json = response.toString();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        };
        String url = "http://www.wpoppin.com/api/events/";


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CustomVolleyRequest jsObjRequest = new CustomVolleyRequest(Request.Method.POST, url, params, responseListener, errorListener);
        requestQueue.add(jsObjRequest);


    }


    public static void setMyUrl(final Context context, final String token) {
        String url = "http://www.wpoppin.com/api/myaccount/";
        StringRequest strreq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "USER" + Response.toString());
                        GsonBuilder gsonBuilder = new GsonBuilder();

                        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                        Gson gson = gsonBuilder.create();

                        User[] account = gson.fromJson(Response, User[].class);
                        User loggedInUser = PrefUtils.getCurrentUser(context);

                        loggedInUser.setUrl(account[0].getUrl());
                        PrefUtils.setCurrentUser(loggedInUser, context);

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



