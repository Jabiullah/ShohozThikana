package com.example.bashakoi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView headerInfo;
    int progress_round = 0;
    Timer timer;
    // here is a dummy comment new
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set the id for the progressbar
        progressBar = findViewById(R.id.progress_bar);
        headerInfo  = findViewById(R.id.textAppear);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Progressbar changing
                if (progress_round <= 100) {
                    progressBar.setProgress(progress_round);
                    progress_round++;
                    handler.postDelayed(this, 10);
                } else {
                    handler.removeCallbacks(this);
                    headerInfo.setVisibility(View.VISIBLE);
                    @SuppressLint("ResourceType") Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.drawable.fade_in);
                    headerInfo.startAnimation(animFadeIn);
                }
            }
        }, 10);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //start the next page 3 second delay
                //here you can add next page easily.
            }
        },3000);
    }
}