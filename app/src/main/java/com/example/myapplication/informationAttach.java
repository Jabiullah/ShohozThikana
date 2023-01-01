package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class informationAttach extends AppCompatActivity {
    EditText name,house,street;
    Button btn_add_info;
    RadioGroup radioGroupLocation;
    private ProgressBar progressBar;

    FloatingActionButton btn_home;

    String loc_name,loc_house,loc_street,cat="";
    int progress = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_attach);

        name    = findViewById(R.id.location_name);
        house   = findViewById(R.id.location_house);
        street  = findViewById(R.id.location_street);

        radioGroupLocation = (RadioGroup) findViewById(R.id.radioCategory);
        btn_add_info       = findViewById(R.id.btn_add_info);

        progressBar        = (ProgressBar) findViewById(R.id.IProgressBar);

        btn_home           = findViewById(R.id.main_back_home);

        btn_add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataStore();
                }
        });

        Bundle bundle_back = getIntent().getExtras();
        if (bundle_back != null) {
            loc_name    = bundle_back.getString("location_name");
            loc_house   = bundle_back.getString("location_house");
            loc_street  = bundle_back.getString("location_street");
            cat         = bundle_back.getString("category");

            name.setText(loc_name);
            house.setText(loc_house);
            street.setText(loc_street);
        }


        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(informationAttach.this, homePage.class);
                startActivity(home_intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(informationAttach.this, homePage.class);
        startActivity(home_intent);
        finish();
    }

    private void dataStore() {
        final String location_name = name.getText().toString().trim();
        final String location_house = house.getText().toString().trim();
        final String location_street = street.getText().toString().trim();
        final String category = ((RadioButton) findViewById(radioGroupLocation.getCheckedRadioButtonId())).getText().toString();


        if (TextUtils.isEmpty(location_name)) {
            name.setError(" ঠিকভাবে বাড়ি / সম্পত্তি / কর্মস্থলের নাম - দিন");
            name.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(location_house)) {
            house.setError(" ঠিকভাবে ফ্ল্যাট নং / বিল্ডিং নং - দিন");
            house.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(location_street)) {
            street.setError(" ঠিকভাবে  কলোনি / রোড নং / এলাকা - দিন ");
            street.requestFocus();
            return;
        }
        // data bundle and send it to voice page
        Bundle bundle = new Bundle();
        bundle.putString("location_name", location_name);
        bundle.putString("location_house", location_house);
        bundle.putString("location_street", location_street);
        bundle.putString("category", category);

        Intent intent_voice = new Intent(informationAttach.this, voiceAttach.class);
        intent_voice.putExtras(bundle);
        startActivity(intent_voice);

    }

}