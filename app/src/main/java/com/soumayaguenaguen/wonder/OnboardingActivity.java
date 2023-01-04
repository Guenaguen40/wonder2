package com.soumayaguenaguen.wonder;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ViewFlipper viewFlipper = findViewById(R.id.view_flipper);
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);

        viewFlipper.setFlipInterval(3000); // 3 seconds
        viewFlipper.startFlipping();
        TextView textView = findViewById(R.id.skip);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(OnboardingActivity.this, SigninActivity.class);
            startActivity(intent);
        });

    }}