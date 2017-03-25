package com.wpoppin.whatspoppin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;

/**
 * Created by joseph on 1/12/2017.
 */

public class Signup extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private EditText passWord2;
    private EditText Email;
    private Button submit;
    private String username;
    private String password;
    private String password2;
    private String email;
    private TextView error;
    private User user;
    private TextView login;

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        passWord2 = (EditText) findViewById(R.id.password2);
        Email = (EditText) findViewById(R.id.email);
        submit = (Button) findViewById(R.id.loginButton);
        error = (TextView) findViewById(R.id.error);
        login= (TextView) findViewById(R.id.link_login);


        user = new User();
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                username = userName.getText().toString();
                password = passWord.getText().toString();
                password2 = passWord2.getText().toString();
                email = Email.getText().toString();
                user.username = username;
                convertToken(username, password);

                if(password != password2){
                    error.setText("Passwords Don't Match");
                }
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, LoginWP.class));
                finish();

            }
        });
    }


    private void convertToken(final String Username, String Password) {
        StringRequest strreq = new StringRequest(Request.Method.POST,
                "http://www.wpoppin.com/api/wp/auth/register/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "RESPONDE" + Response.toString());

                        //split the json string to get the token value then store it in local memory
                        String values[] = Response.split(",");
                        String email = values[0];
                        String username = values[1];
                        String id = values[2];
                        String ids[] = id.split(":");
                        ids[1] = ids[1].replace("}", "");
                        user.setId(Integer.parseInt(ids[1]));

                        Log.i(TAG, "id" + user.getId());

                        getTokenAfterSignup(user.getUsername(), password);

                        PrefUtils.setCurrentUser(user,Signup.this);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Signup.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username",user.username);
                        editor.apply();
                        startActivity(new Intent(Signup.this, SelectInterests.class));
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                ConnectivityManager connectivityManager
                        = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if(activeNetworkInfo == null || !activeNetworkInfo.isConnected())
                    error.setText("No Internet Connection");
                else
                    error.setText("Invalid Username, Email, or Password");

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strreq);
    }

 //Converts a users username and password into a token -- same as the loginwp method, but that is static so needed to redeclare
    private void getTokenAfterSignup(final String Username, String Password) {
        StringRequest strreq = new StringRequest(Request.Method.POST,
                "http://www.wpoppin.com/token-auth/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "RESPONDE" + Response.toString());

                        //split the json string to get the token value then store it in local memory
                        String values[] = Response.split("\":\"");
                        String key = values[0];
                        key = key.replace("{\"", "");
                        String value = values[1];
                        value = value.replace("\"}", "");

                        Log.i(TAG, "KEY" + key + " " + value);
                        user.setToken(value);

                        PostDataToServer.setMyUrl(Signup.this, user.getToken());

                        PrefUtils.setCurrentUser(user,Signup.this);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Signup.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username",user.username);
                        editor.apply();
                        startActivity(new Intent(Signup.this, SelectInterests.class));
                        finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                ConnectivityManager connectivityManager
                        = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if(activeNetworkInfo == null || !activeNetworkInfo.isConnected())
                    error.setText("No Internet Connection");
                else
                    error.setText("Invalid Username or Password");

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;

            }


        };
        AppController.getInstance().addToRequestQueue(strreq);


    }


}
