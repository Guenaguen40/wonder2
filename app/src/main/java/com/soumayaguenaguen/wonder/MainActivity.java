package com.soumayaguenaguen.wonder;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_item_1);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_item_1:
                    startActivity(new Intent(this, MainActivity.class));
                    break;

                case R.id.nav_item_2:
                    startActivity(new Intent(this, DestinationActivity.class));
                    break;

                case R.id.nav_item_4:
                    startActivity(new Intent(this, EventsActivity.class));
                    break;
            }
            return true;

        });
    }
}