package com.lessvoterfraudclaims.delightfulemeraldmichele;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Zaglush extends AppCompatActivity {

    static public SharedPreferences sp;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaglush);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ((CheckBox) findViewById(R.id.check_pp)).setChecked(sp.getBoolean("pp", false));
        String[] g_names = getResources().getStringArray(R.array.g_names);
        ((TextView) findViewById(R.id.welcome)).setText(getString(
                R.string.main_welcome).replaceAll(
                "\\[g_names\\]",
                g_names[(int) (Math.random() * g_names.length)]));
    }

    public void showPrivacyPolicy(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://docs.google.com/document/d/16XWKf9zS4kYswGsxtxGXTcBpiyJQXwdpXgqqlIf5agw/"));
        startActivity(intent);
        //https://docs.google.com/document/d/16XWKf9zS4kYswGsxtxGXTcBpiyJQXwdpXgqqlIf5agw/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) return true;
        return super.onKeyDown(keyCode, event);
    }

    public void goNextPage(View v) {
        if (!((CheckBox) findViewById(R.id.check_pp)).isChecked()) {
            Toast.makeText(this, R.string.u_must_cpp, Toast.LENGTH_LONG).show();
            return;
        }
        String name = ((EditText) findViewById(R.id.user_name)).getText().toString();
        if (name.length() < 2) name = "User";
        sp.edit().putBoolean("pp", true).apply();
        Intent intent = new Intent(this, ZagQuest.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
