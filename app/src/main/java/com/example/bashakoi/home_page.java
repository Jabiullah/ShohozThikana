package com.example.bashakoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class home_page extends AppCompatActivity {
    Button btnClick;
    EditText textEnter;
    TextToSpeech textToSpeech;
    FloatingActionButton btn_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btn_profile = findViewById(R.id.fab);
        btnClick = findViewById(R.id.button2);
        textEnter = findViewById(R.id.text1);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    int language = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = textEnter.getText().toString();
                int speech = textToSpeech.speak(s,textToSpeech.QUEUE_FLUSH,null);

            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile_intent = new Intent(home_page.this, ProfilePage.class);
                startActivity(profile_intent);
                finish();
            }
        });

    }
}