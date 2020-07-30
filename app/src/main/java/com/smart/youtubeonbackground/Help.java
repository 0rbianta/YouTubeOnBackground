package com.smart.youtubeonbackground;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //Animate Background
        LinearLayout LN=findViewById(R.id.help_bg);
        AnimationDrawable AnimationDraw=(AnimationDrawable)LN.getBackground();
        AnimationDraw.setEnterFadeDuration(1000);
        AnimationDraw.setExitFadeDuration(5000);
        AnimationDraw.start();

        Button btnNBack=findViewById(R.id.btnBack);
        btnNBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartApp();
            }
        });


    }

    public void btnViewL(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://raw.githubusercontent.com/0rbianta/YouTubeOnBackground/master/LICENSE"));
        startActivity(browserIntent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed(){restartApp();}

    private void restartApp(){
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}