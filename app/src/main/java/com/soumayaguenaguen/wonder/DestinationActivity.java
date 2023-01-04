package com.soumayaguenaguen.wonder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DestinationActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_item_2);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_item_1:
                    startActivity(new Intent(this, MainActivity.class));
                    break;

                case R.id.nav_item_2:
                    startActivity(new Intent(this, DestinationActivity.class));
                    break;

                case R.id.nav_item_3:
                    startActivity(new Intent(this, EventsActivity.class));
                    break;
            }
            return true;

        });


    }
}