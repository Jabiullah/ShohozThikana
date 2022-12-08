package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class homePage extends AppCompatActivity {
    FloatingActionButton btn_profile;
    ImageButton add_information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btn_profile     = findViewById(R.id.fab);
        add_information = findViewById(R.id.info_add);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile_intent = new Intent(homePage.this, profilePage.class);
                startActivity(profile_intent);
                finish();
            }
        });

        add_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent information_intent = new Intent(homePage.this, informationAttach.class);
                startActivity(information_intent);
                finish();
            }
        });

    }
}