package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quizapp.Activities.SetsActivity;

public class MainActivity extends AppCompatActivity {

    CardView Placements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Placements = findViewById(R.id.placements);

        Placements.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetsActivity.class);
                startActivity(intent);
            }
        });
    }
}