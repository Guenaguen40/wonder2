package com.soumayaguenaguen.wonder;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_DELAY = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Button button = findViewById(R.id.start);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(SplashScreenActivity.this, OnboardingActivity.class);
            startActivity(intent);
        });

        RelativeLayout layout = findViewById(R.id.layout);
        new Thread(() -> {
            AnimationDrawable animation = (AnimationDrawable) layout.getBackground();
            animation.start();
        }).start();

                new Handler().postDelayed(() -> {
                    SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
                    boolean previouslyStarted = prefs.getBoolean("previously_started", false);
                    if(!previouslyStarted) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean("previously_started", Boolean.TRUE);
                        edit.apply();
                        Intent intent2 = new Intent(SplashScreenActivity.this, welcomeActivity.class);
                        startActivity(intent2);
                    } else {
                        Intent intent3 = new Intent(SplashScreenActivity.this, OnboardingActivity.class);
                        startActivity(intent3);
                    }
                }, SPLASH_SCREEN_DELAY);
            }

}

