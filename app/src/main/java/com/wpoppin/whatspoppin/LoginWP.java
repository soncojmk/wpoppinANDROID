package com.wpoppin.whatspoppin;

import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.wpoppin.whatspoppin.AppController.TAG;

/**
 * Created by joseph on 12/27/2016.
 */

public class LoginWP extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private Button submit;
    private String username;
    private String password;
    private TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_wp);

        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.loginButton);
        error = (TextView) findViewById(R.id.error);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                username = userName.getText().toString();
                password = passWord.getText().toString();

                convertToken(username, password);
            }

            });
    }


    private void convertToken(final String Username, String Password) {
        StringRequest strreq = new StringRequest(Request.Method.POST,
                "http://www.wpoppin.com/api-token-auth/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        Log.i(TAG, "RESPONDE" + Response.toString());
                        startActivity(new Intent(LoginWP.this, JSON.class));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
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
