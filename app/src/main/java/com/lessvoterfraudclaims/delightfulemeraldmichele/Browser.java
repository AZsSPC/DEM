package com.lessvoterfraudclaims.delightfulemeraldmichele;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.lessvoterfraudclaims.delightfulemeraldmichele.MainActivity.debug;
import static com.lessvoterfraudclaims.delightfulemeraldmichele.MainActivity.url_user;

public class Browser extends AppCompatActivity {

    ArrayList<String> urls = new ArrayList<>();
    int on_url = 0;
    final int max_pages = 32;
    static public SharedPreferences sp;
    final String SP_KEY_LAST_PAGE = "last_page";
    final String SP_KEY_PAGE_COUNT = "page_count";
    final String SP_KEY_PAGE_NUM = "page_";
    WebView ww;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        goMainPage(null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        on_url = sp.getInt(SP_KEY_LAST_PAGE, 0);
        urls.add(url_user);
        for (int i = 0; i < sp.getInt(SP_KEY_PAGE_COUNT, 0); i++)
            urls.add(sp.getString(SP_KEY_PAGE_NUM + i, url_user));
        setContentView(R.layout.web_view_lay);
        ww = findViewById(R.id.ww);
        ww.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        ww.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ww.getSettings().setJavaScriptEnabled(true);
        ww.loadUrl(url_user);
        ww.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                update();
                if (urls.size() >= max_pages) urls.remove(0);
                if (!url.equals(url_user))
                    if (++on_url < urls.size()) urls.set(on_url, url);
                    else urls.add(url);

                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goBackPage(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void update() {
        findViewById(R.id.but_pre).setEnabled(on_url > 0);
        findViewById(R.id.but_nex).setEnabled(on_url + 1 < urls.size());
        if (!debug) return;
        Log.e("[ ]", "------------\\");
        Log.e("[ ]", on_url + "");
        int i = 0;
        for (String s : urls)
            Log.e((i++ == on_url ? "[+]" : "[-]"), s);
        Log.e("[ ]", "------------/");
    }

    public void goNextPage(View v) {
        if (!(on_url + 1 > max_pages || on_url + 2 > urls.size()))
            ww.loadUrl(urls.get(++on_url));
        update();
    }

    public void goBackPage(View v) {
        if (on_url > 0)
            ww.loadUrl(urls.get(--on_url));
        update();
    }

    public void goMainPage(View v) {
        try {
            ww.loadUrl(url_user);
        } catch (Exception ignored) {
            Toast.makeText(this, "no", Toast.LENGTH_LONG).show();
        }
        update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            final int page_count = urls.size();
            SharedPreferences.Editor edit = sp.edit();
            for (int i = 0; i < max_pages; i++)
                if (i > page_count) edit.remove(SP_KEY_PAGE_NUM + i);
                else edit.putString(SP_KEY_PAGE_NUM + i, urls.get(i));
            edit.putInt(SP_KEY_LAST_PAGE, on_url);
            edit.putInt(SP_KEY_PAGE_COUNT, page_count);
            edit.apply();
        } catch (Exception ignored) {
        }
    }
}
