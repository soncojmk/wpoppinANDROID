package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by joseph on 12/22/2016.
 * This class allows the user to login with facebook which we get a token from facebook that is saved in a variable
 * and sent to What'sPoppin server to be converted to a What'sPoppin user token
 * or to be redirected to login or sign up
 */

public class login extends AppCompatActivity {

    private TextView WP;
    private TextView description;
    private Button explore;
    private Button signUp;
    private LoginButton fbLogin;
    private CallbackManager callbackManager;
    User user;

    private String TAG = "TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.login);


        //AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        WP = (TextView) findViewById(R.id.login);
        WP.setText("What'sPoppin");

        description = (TextView) findViewById(R.id.description);
        description.setText("CONNECT, EXPLORE, DISCOVER");

        explore = (Button) findViewById(R.id.explore);
        explore.setText("Login");

        signUp = (Button) findViewById(R.id.signup);
        signUp.setText("Sign Up");

        fbLogin = (LoginButton) findViewById(R.id.fb_login);
        fbLogin.setText("Login with Facebook");

        explore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(login.this, LoginWP.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(login.this, Signup.class));
            }
        });


        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final AccessToken accessToken = loginResult.getAccessToken();
                String UserID = accessToken.getUserId();

                final String token = accessToken.getToken();
                Log.i("AccessToken", token);


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.e("response: ", response + "");
                                JSONObject json = response.getJSONObject();
                                try {
                                    user = new User();
                                    user.facebookID = json.getString("id");
                                    //user.email = json.getString("email");
                                    user.username = json.getString("name");
                                    PrefUtils.setCurrentUser(user,login.this);

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(login.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("username",user.username);
                                    editor.apply();

                                    convertToken(token, user);


                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                               // Toast.makeText(login.this,"welcome "+ user.username,Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(login.this, SelectInterests.class);
                                startActivity(intent);
                                finish();

                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                //Log.i("Login Attempt Cancelled", "");
            }

            @Override
            public void onError(FacebookException e) {
               // Log.i("Login attempt failed.", " ");
            }
        });
    }


    private void convertToken(final String accessToken, final User user) {
        StringRequest strreq = new StringRequest(Request.Method.POST,
                "http://www.wpoppin.com/api/auth/convert-token",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "RESPONDE" + Response.toString());

                        String values[] = Response.split(",");
                        String token = values[0];

                        String keyvalue[] = token.split(":");
                        String value = keyvalue[1].replace("\"", "");
                        user.setToken(value);

                        Log.i(TAG, "TOKEN" + " " + value);

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
                params.put("grant_type", "convert_token");
                params.put("client_id", "ALovBuA6sReToET7LnGJwIV2FU7Q3RWGXk8PdkD0");
                params.put("client_secret", "Bu0cY65cY7mIYC0nqL7YAy9naeAwx5gwO0hHdxZ03GzddNIZ1NXy4SpmOOnhEz7zy4v1FxuE3iFDqCsF114lkMBY7XihkKIB9ZGusTMq16kmr6zErYOylUDhhGku5ybu");
                params.put("backend", "facebook");
                params.put("token", accessToken);

                return params;

            }
        };
        AppController.getInstance().addToRequestQueue(strreq);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
