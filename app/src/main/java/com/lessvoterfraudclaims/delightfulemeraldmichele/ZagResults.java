package com.lessvoterfraudclaims.delightfulemeraldmichele;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZagResults extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    static public TextView analysis;
    boolean async = true;
    Handler handler = new Handler();
    String[] args;
    boolean asd = true;
    int counter = 0;
    String str = "";
    String name = "User";
    int[] answers;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        name = Objects.requireNonNull(arguments).getString("name");
        answers = Objects.requireNonNull(arguments).getIntArray("answers");
        setContentView(R.layout.show_results);
        analysis = findViewById(R.id.analysis);
        args = getResources().getStringArray(R.array.analysis);

        final Runnable r = new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                try {
                    if (asd) {
                        str += args[counter++];
                        analysis.setText(str);
                        asd = false;
                    } else {
                        str += " - done\n";
                        analysis.setText(str);
                        asd = true;
                    }
                } catch (Exception ignored) {
                    async = false;
                    analysis.setText(str + "complete!");
                }
                if (async)
                    handler.postDelayed(this, (int) (500 + Math.random() * (asd ? 0 : 1000)));
                else showFinal();
            }
        };
        handler.postDelayed(r, 1000);

    }

    @SuppressLint("SetTextI18n")
    void showFinal() {
        findViewById(R.id.lay_res_p).setVisibility(View.INVISIBLE);
        findViewById(R.id.lay_res_f).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.g_image)).setImageDrawable(getDrawable(getRID("g" + ((int) (Math.random() * 10.9)), R.mipmap.class)));

        ((TextView) findViewById(R.id.title_result)).setText(getTextFromID("g_names", true, -1) + ", " + ((int) (Math.random() * 16 + 23)) + "y.o.");
        String text = getString(R.string.final_show).replaceAll("\\(name\\)", name);
        //
        Pattern pattern = Pattern.compile("\\[.+?]");
        Matcher matcher = pattern.matcher(text);
        ArrayList<String> torep = new ArrayList<>();
        while (matcher.find()) torep.add(text.substring(matcher.start(), matcher.end()));
        for (String s : torep) {
            String to = s.replaceAll("\\[|]", "");
            Log.e("------", s + "," + to);
            text = text.replace(s, getTextFromID(to, true, -1));
        }
        //
        Pattern pattern2 = Pattern.compile("ANSWER\\d+");
        Matcher matcher2 = pattern2.matcher(text);
        ArrayList<String> torep2 = new ArrayList<>();
        while (matcher2.find()) torep2.add(text.substring(matcher2.start(), matcher2.end()));
        for (String s : torep2) {
            int to = Integer.parseInt(s.replaceAll("ANSWER", ""));
            Log.e("------", s + "," + to+","+ answers[to]);
            text = text.replace(s, getTextFromID(getTextFromID("quest", true, to), true, answers[to]+6 ));
        }
        ((TextView) findViewById(R.id.about_g)).setText(text);
    }

    public String getTextFromID(String resName, boolean is_arr, int i) {
        try {
            if (is_arr) {
                String[] arr = getResources().getStringArray(getRID(resName, R.array.class));
                return arr[i == -1 ? (int) ((arr.length - 0.1) * Math.random()) : i];
            } else return getString(getRID(resName, R.string.class));
        } catch (Exception ignored) {
            return "???";
        }
    }

    public static int getRID(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void onClickRedo(View v) {
        Intent intent = new Intent(this, ZagQuest.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
