package com.smart.youtubeonbackground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String KEY = "289462392";
    public static final String SHARED_PREFS = "sharedPrefs";

    public static String ServiceID = "YoutubeOnBackground", USERAGENT="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36";
    private Boolean IsVideoPlaying=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StopVoiceAndVideo();
        //Stop Service
        Channel();
        Intent BGService = new Intent(getBaseContext(), service.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            stopService(BGService);
        } else {
            stopService(BGService);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Animate Background
        LinearLayout LN=findViewById(R.id.App);
        AnimationDrawable AnimationDraw=(AnimationDrawable)LN.getBackground();
        AnimationDraw.setEnterFadeDuration(1000);
        AnimationDraw.setExitFadeDuration(5000);
        AnimationDraw.start();

        final CheckBox chkLoadWebsite=findViewById(R.id.chkLoadWebsite);
        chkLoadWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPPClick(view);
            }
        });
    }

    public void btnPPClick(View v) {
        EditText etxtURL=findViewById(R.id.etxtURL);
        ListView lv=findViewById(R.id.VList);
        WebView wv=findViewById(R.id.webView);
        Button btnPP=findViewById(R.id.btnPP);
        CheckBox chkLoadWebsite=findViewById(R.id.chkLoadWebsite);

        try {
            InputMethodManager sk = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            sk.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){}

        if(IsVideoPlaying){
            //True
            StopVoiceAndVideo();

        }else {
            //False

            if (etxtURL.getText().toString().contains("yout")==false) {
                Toast.makeText(getApplicationContext(), "Please enter url like:https://www.youtube.com/watch?v=VIDEO_ID", Toast.LENGTH_LONG).show();
            }else if(etxtURL.getText().toString().contains("youtu.be")){
                //Shorten URL Youtube

                //Yes, It is a listview but it's have cool UI.
                final List<String> ArrayList = new ArrayList();

                ArrayList.add("Playing, " + etxtURL.getText().toString() + " right now.");

                ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ArrayList);
                lv.setAdapter(adapter);
                lv.setVisibility(View.VISIBLE);

                String reverseID = new StringBuilder(etxtURL.getText()).reverse().toString().split("/")[0];
                String ID = new StringBuilder(reverseID).reverse().toString();
                System.out.println(ID);

                String FVideo;

                if(chkLoadWebsite.isChecked()){
                    FVideo =etxtURL.getText().toString();
                }else{
                    FVideo ="https://youtube.com/embed/"+ID;
                }


                wv.setVisibility(View.VISIBLE);
                try {wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); } catch (Exception e) {}
                wv.getSettings().setDomStorageEnabled(true);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.setWebViewClient(new WebViewClient());
                wv.getSettings().setUserAgentString(USERAGENT);
                wv.loadUrl("");
                wv.loadUrl(FVideo);

                IsVideoPlaying = true;
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnPP.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_pause_24) );
                } else {
                    btnPP.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_pause_24));
                }
                //Start Service
                Channel();
                Intent s = new Intent(getBaseContext(), service.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(s);
                } else {
                    startService(s);
                }

                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
                wakeLock.acquire();
                etxtURL.setVisibility(View.GONE);
            }else{
                //Full URL Youtube

                //Yes, It is a listview but it's have cool UI.
                final List<String> ArrayList = new ArrayList();

                ArrayList.add("Playing, " + etxtURL.getText().toString() + " right now.");

                ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ArrayList);
                lv.setAdapter(adapter);
                lv.setVisibility(View.VISIBLE);

                String reverseID = new StringBuilder(etxtURL.getText()).reverse().toString().split("=")[0];
                String ID = new StringBuilder(reverseID).reverse().toString();

                String FVideo;

                if(chkLoadWebsite.isChecked()){
                    FVideo =etxtURL.getText().toString();
                }else{
                    FVideo ="https://youtube.com/embed/"+ID;
                }

                wv.setVisibility(View.VISIBLE);
                try {wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); } catch (Exception e) {}
                wv.getSettings().setDomStorageEnabled(true);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.setWebViewClient(new WebViewClient());
                wv.getSettings().setUserAgentString(USERAGENT);
                wv.loadUrl("");
                wv.loadUrl(FVideo);

                IsVideoPlaying = true;
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnPP.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_pause_24) );
                } else {
                    btnPP.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_pause_24));
                }
                //Start Service
                Channel();
                Intent s = new Intent(getBaseContext(), service.class);
                s.putExtra("times", 5);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(s);
                } else {
                    startService(s);
                }

                etxtURL.setVisibility(View.GONE);

            }
        }
    }

    public void btnHelp(View v){
        //View Help
        Intent intent = new Intent(MainActivity.this, Help.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        //Stop Service
        Channel();
        Intent BGService = new Intent(getBaseContext(), service.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            stopService(BGService);
        } else {
            stopService(BGService);
        }
        //Destroy this
        finish();
    }

    private void Channel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notify = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel CH = new NotificationChannel(ServiceID,
                    getString(R.string.cn),
                    NotificationManager.IMPORTANCE_LOW);
            CH.setDescription(getString(R.string.cd));
            CH.enableLights(true);
            CH.setShowBadge(true);
            notify.createNotificationChannel(CH);
        }
    }

    private void StopVoiceAndVideo(){
        //True
        EditText etxtURL=findViewById(R.id.etxtURL);
        ListView lv=findViewById(R.id.VList);
        WebView wv=findViewById(R.id.webView);
        Button btnPP=findViewById(R.id.btnPP);

        wv.setVisibility(View.GONE);
        try {wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); } catch (Exception e) {}
        wv.getSettings().setDomStorageEnabled(false);
        wv.getSettings().setJavaScriptEnabled(false);
        wv.loadUrl("");
        lv.setVisibility(View.GONE);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btnPP.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_play_arrow_24) );
        } else {
            btnPP.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_play_arrow_24));
        }


        //Stop Service
        Channel();
        Intent BGService = new Intent(getBaseContext(), service.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            stopService(BGService);
        } else {
            stopService(BGService);
        }

        etxtURL.setVisibility(View.VISIBLE);
        IsVideoPlaying = false;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed(){hideApp();}

    public void hideApp() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

}
