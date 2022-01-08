package com.otec.webdealit.UI;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.otec.webdealit.R;
import com.otec.webdealit.Utils.utils;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottomNav);
        progressBar = findViewById(R.id.progressBar);
        new utils().bottom_nav(navigationView,this,progressBar);
    }
}