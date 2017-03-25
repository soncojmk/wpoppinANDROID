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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;

/**
 * Created by joseph on 12/27/2016.
 * This class allows a user to login with their What'sPoppin username and password.
 * The API is pinged and returns a What'sPoppin user token if the user exists
 */

public class LoginWP extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private Button submit;
    private String username;
    private String password;
    private TextView error;
    private User user;
    private TextView signup;

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_wp);

        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.loginButton);
        error = (TextView) findViewById(R.id.error);
        signup = (TextView) findViewById(R.id.link_signup);


        user = new User();
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                username = userName.getText().toString();
                password = passWord.getText().toString();
                user.username = username;
                convertToken(username, password);
            }

            });

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginWP.this, Signup.class));
                finish();

            }
        });
    }


    private void convertToken(final String Username, String Password) {
        StringRequest strreq = new StringRequest(Request.Method.POST,
                "http://www.wpoppin.com/token-auth/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "RESPONDE" + Response.toString());

                        //split the json string to get the token value then store it in local memory

                        String values[] = Response.split(",");
                        String token = values[0];
                        String tokenKeyvalues[] = token.split(":");
                        String tokenValue = tokenKeyvalues[1].replace("\"", "");
                        //get id
                        String id = values[1];
                        String idKeyvalues[] = id.split(":");
                        String idValue = idKeyvalues[1].replace("\"", "");
                        idValue = idValue.replace("}", "");

                        Log.i(TAG, "KEY" + tokenValue + " " + idValue);
                        user.setToken(tokenValue);
                        user.setId(Integer.parseInt(idValue));

                        //set the user url
                        PostDataToServer.setMyUrl(LoginWP.this, user.getToken());

                        PrefUtils.setCurrentUser(user,LoginWP.this);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginWP.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username",user.username);
                        editor.apply();
                        startActivity(new Intent(LoginWP.this, SelectInterests.class));
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
