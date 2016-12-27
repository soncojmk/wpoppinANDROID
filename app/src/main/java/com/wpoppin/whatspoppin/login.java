package com.wpoppin.whatspoppin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by joseph on 12/22/2016.
 */

public class login extends AppCompatActivity {

    private TextView WP;
    private TextView description;
    private Button explore;
    private Button signUp;
    private LoginButton fbLogin;
    private CallbackManager callbackManager;
    private TextView info;
    User user;

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

        info = (TextView)findViewById(R.id.info);

        explore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(login.this, JSON.class));
            }
        });


        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();
                String UserID = accessToken.getUserId();

                String access = accessToken.getToken();
                Log.i("AccessToken", access);


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.e("response: ", response + "");
                                try {
                                    user = new User();
                                    user.facebookID = object.getString("id").toString();
                                    user.email = object.getString("email").toString();
                                    user.username = object.getString("name").toString();
                                    PrefUtils.setCurrentUser(user,login.this);

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(login.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("username",user.username);
                                    editor.apply();

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                Toast.makeText(login.this,"welcome "+user.username,Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(login.this,JSON.class);
                                startActivity(intent);
                                finish();

                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
