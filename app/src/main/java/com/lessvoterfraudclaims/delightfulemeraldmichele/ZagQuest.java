package com.lessvoterfraudclaims.delightfulemeraldmichele;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ZagQuest extends AppCompatActivity {
    String name;
    int q_num;
    int[] answers, questions;
    TextView title, quest;
    ConstraintLayout li, lt;
    Button i1, i2, i3, i4,  // images
            t1, t2, t3;     // texts
    ArrayList<Integer> arr = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        name = Objects.requireNonNull(getIntent().getExtras()).getString("name");
        String[] qarr = getResources().getStringArray(getRID("quest", R.array.class));
        answers = new int[qarr.length];
        questions = new int[qarr.length];
        for (int i = 0; i < qarr.length; i++) {
            arr.add(i);
            questions[i] = getRID(qarr[i], R.array.class);
        }
        Collections.shuffle(arr);
        launchNextQuest();
    }

    void init() {
        setContentView(R.layout.zag_q);

        title = findViewById(R.id.title_q);
        quest = findViewById(R.id.zag_q);
        li = findViewById(R.id.li);
        lt = findViewById(R.id.lt);
        i1 = findViewById(R.id.li1);
        i2 = findViewById(R.id.li2);
        i3 = findViewById(R.id.li3);
        i4 = findViewById(R.id.li4);
        t1 = findViewById(R.id.lt1);
        t2 = findViewById(R.id.lt2);
        t3 = findViewById(R.id.lt3);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) return true;
        return super.onKeyDown(keyCode, event);
    }

    public String getTextFromID(String resName, boolean is_arr) {
        try {
            if (!is_arr) return getString(
                    getRID(resName, R.string.class));

            String[] arr = getResources().getStringArray(
                    getRID(resName, R.array.class));
            return arr[(int) ((arr.length - 0.1)  * Math.random())];
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

    public void setAnswer(View v) {
        if (i1.equals(v) || t1.equals(v)) answers[q_num] = 0;
        if (i2.equals(v) || t2.equals(v)) answers[q_num] = 1;
        if (i3.equals(v) || t3.equals(v)) answers[q_num] = 2;
        if (i4.equals(v)) answers[q_num] = 3;
        Log.e("----", v.getTransitionName() + ", " + q_num + ":" + answers[q_num]);
        launchNextQuest();
    }

    void launchNextQuest() {
        if (arr.size() == 0) {
            showResults();
            return;
        }
        q_num = arr.get(0);
        arr.remove(0);
        title.setText(
                getTextFromID("q_title", false)
                        .replace(
                                "(q_num)",
                                "" + (answers.length - arr.size())
                        )
                        .replace(
                                "(q_max)",
                                "" + answers.length
                        )
        );
        String[] quest_s = getResources().getStringArray(questions[q_num]);
        quest.setText(quest_s[1]);
        if (quest_s[0].equals("#t")) {
            li.setVisibility(View.INVISIBLE);
            lt.setVisibility(View.VISIBLE);
            t1.setText(quest_s[2]);
            t2.setText(quest_s[3]);
            t3.setText(quest_s[4]);
        } else if (quest_s[0].equals("#i")) {
            li.setVisibility(View.VISIBLE);
            lt.setVisibility(View.INVISIBLE);
            int[] icons = new int[4];
            for (int i = 0; i < 4; i++) icons[i] = getRID(quest_s[i + 2], R.mipmap.class);
            i1.setForeground(getDrawable(icons[0]));
            i2.setForeground(getDrawable(icons[1]));
            i3.setForeground(getDrawable(icons[2]));
            i4.setForeground(getDrawable(icons[3]));

        }

    }

    void showResults() {
        StringBuilder s = new StringBuilder("all");
        for (int i = 0; i < answers.length; i++)
            s.append("\n").append(i).append("=").append(answers[i]);
        Log.e("answers", s.toString());
        Intent intent = new Intent(this, ZagResults.class);
        intent.putExtra("answers", answers);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
