package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Animation fadeIn, fadeOut;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineGlobalVariables();
        hideSystem();


        TimerTask fadeOutLogo = new TimerTask() {
            @Override
            public void run() {
                logo.setAnimation(fadeOut);
            }
        };
        TimerTask ToMenu = new TimerTask() {
            @Override
            public void run() {
                Intent menu = new Intent(MainActivity.this, Menu.class);
                startActivity(menu);
                finish();
            }
        };

        logo.setAnimation(fadeIn);

        Timer timeline = new Timer();
        timeline.schedule(fadeOutLogo, 2100);
        timeline.schedule(ToMenu, 3400);

    }

    private void hideSystem() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void defineGlobalVariables() {
        logo = findViewById(R.id.logo);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
    }

}