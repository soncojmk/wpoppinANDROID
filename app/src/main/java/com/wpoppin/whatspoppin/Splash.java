package com.wpoppin.whatspoppin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.R.attr.key;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by joseph on 12/24/2016.
 * <p>
 * <p>
 * This class is the initial activity that is loaded while android waits for the app to load.
 * It displays the What'sPoppin Logo
 */

public class Splash extends AppCompatActivity {


    private String usersname;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FlurryAgent.Builder().withLogEnabled(true)
                .build(this, "B9XSGWDDCCY43738TY8S");

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String restoredText = prefs.getString("username", null);




        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.wpoppin.whatspoppin", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                Toast.makeText(getApplicationContext(),sign, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }*/

        if (PrefUtils.getCurrentUser(Splash.this) == null) {

            Intent homeIntent = new Intent(Splash.this, login.class);
            startActivity(homeIntent);
            finish();
        }

        else if(!PrefUtils.getCurrentUser(Splash.this).interestSet){
            Intent intent = new Intent(Splash.this, SelectInterests.class);
            startActivity(intent);
            finish();
        }


        else {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            finish();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Splash Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

