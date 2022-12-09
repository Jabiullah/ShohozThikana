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


        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(informationAttach.this, homePage.class);
                startActivity(home_intent);
                finish();
            }
        });

    }

    private void dataStore() {
        final String location_name   = name.getText().toString().trim();
        final String location_house  = house.getText().toString().trim();
        final String location_street = street.getText().toString().trim();
        //User user                    = SharedPrefManager.getInstance(this).getUser();
        final String category        = ((RadioButton) findViewById(radioGroupLocation.getCheckedRadioButtonId())).getText().toString();


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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Progressbar changing
                if (progress < 50) {
                    progressBar.setProgress(progress);
                    progress++;
                    if(progress==16){
                        name.setEnabled(false);
                        name.setTextColor(Color.GREEN);
                    }
                    if(progress==33){
                        house.setEnabled(false);
                        house.setTextColor(Color.GREEN);
                    }
                    if(progress==45){
                        street.setEnabled(false);
                        street.setTextColor(Color.GREEN);
                    }
                    handler.postDelayed(this, 15);
                }
            }
        }, 15);
        // data bundle and send it to voice page
        Bundle bundle = new Bundle();
        bundle.putString("location_name",location_name);
        bundle.putString("location_house",location_house);
        bundle.putString("location_street",location_street);
        bundle.putString("category",category);

        Intent intent_voice = new Intent(informationAttach.this, voiceAttach.class);
        intent_voice.putExtras(bundle);
        startActivity(intent_voice);

//        class dataStore extends AsyncTask<Void, Void, String> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                try {
//                    //converting response to json object
//                    JSONObject obj = new JSONObject(s);
//
//                    //if no error in response
//                    if (!obj.getBoolean("error")) {
//
//                        final String user_message = obj.getString("message");
//
//                        //creating a new location object
//                        if(user_message.contains("Successful")){
//                            Toast.makeText(getApplicationContext(), "information successful", Toast.LENGTH_SHORT).show();
//
//                            Intent intent_profile = new Intent(getApplicationContext(), profilePage.class);
//
//                            startActivity(intent_profile);
//                        }
//                        //error check
//
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Invalid information", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                //creating request handler object
//                RequestHandler requestHandler = new RequestHandler();
//
//                //creating request parameters
//                HashMap<String, String> params = new HashMap<>();
//                params.put("location_name", location_name);
//                params.put("location_house",location_house);
//                params.put("location_street", location_street);
//                params.put("location_category", category);
//                params.put("user_serial", String.valueOf(user.getId()));
//
//                return requestHandler.sendPostRequest(URLs.URL_LOCATION_INFO_INSERT, params);
//            }
//        }
//
//        dataStore ds = new dataStore();
//        ds.execute();

    }

}