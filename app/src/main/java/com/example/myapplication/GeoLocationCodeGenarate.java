package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class GeoLocationCodeGenarate extends AppCompatActivity {
    TextView final_result;
    String HouseInfo,voiceFileURL,locationSerial;
    Button generate,backToHome,addAgain;
    EditText inputName;
    String geoCodeFirstPart;
    NotificationManagerCompat notificationManagerCompat;
    Notification n;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_location_code_genarate);


        User user          = SharedPrefManager.getInstance(this).getUser();
        String[] splitName = user.getUser_name().split("[\\s+-,.]");

        geoCodeFirstPart   = splitName[0];

        Bundle bundle_geo = getIntent().getExtras();
        if (bundle_geo != null) {
            HouseInfo       = bundle_geo.getString("lastPartOfHouse");
            voiceFileURL    = Arrays.toString(bundle_geo.getStringArray("voiceFilePath"));
            locationSerial  = bundle_geo.getString("locationPK");
        }

        backToHome  = (Button)findViewById(R.id.doneGoBackHome);
        generate    = (Button)findViewById(R.id.generateAgain);
        addAgain    = (Button)findViewById(R.id.generateAgain);
        final_result= findViewById(R.id.result);
        inputName   = findViewById(R.id.commonGeoCode);

        addAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geoCodeFirstPart  = inputName.getText().toString().trim();
                geoCodeGenerate();
            }
        });
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent_home = new Intent(GeoLocationCodeGenarate.this, homePage.class);
               startActivity(intent_home);
            }
        });
        voiceData();
        geoCodeGenerate();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("MyID","My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"MyID").setSmallIcon(R.mipmap.ic_launcher_round).setContentText("অভিনন্দন, আপনি আপনার বাড়ির ঠিকানা \n সহজ ঠিকানায় রূপান্তর করেছেন.").setContentTitle("সফল");
        n = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }
    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(GeoLocationCodeGenarate.this, homePage.class);
        startActivity(home_intent);
        finish();
    }

    private void voiceData(){
        class voiceURL extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (!obj.getBoolean("error")) {
                        final String user_message = obj.getString("message");
                        if (user_message.contains("Successful")) {
                            //
                            //display.setText("আপনার সহজ ঠিকানা হচ্ছে - ");

                        }
                        //error check

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid information", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("voiceURL", voiceFileURL);
                params.put("LocationID", locationSerial);
                return requestHandler.sendPostRequest(URLs.URL_VOICE_FILE_PATH, params);
            }
        }

        voiceURL ds = new voiceURL();
        ds.execute();
    }

    private void geoCodeGenerate(){
        String[] houseNo = HouseInfo.split("[\\s+-,.]");
        //System.out.println(houseNo[0]);
        String houseNumber = houseNo[0];
        String geoCode     = geoCodeFirstPart+houseNumber;
        User user          = SharedPrefManager.getInstance(this).getUser();
        class GeoCode extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        final String geo_message = obj.getString("message");

                        if(geo_message.contains("GeoCode Inserted")){
                            final_result.setText("আপনার বাসার সহজ ঠিকানা কোড - "+geoCode);
                            backToHome.setVisibility(View.VISIBLE);
                            inputName.setVisibility(View.GONE);
                            addAgain.setVisibility(View.GONE);
                            notificationManagerCompat.notify(1,n);

                        }else {
                            inputName.setVisibility(View.VISIBLE);
                            inputName.setText("");
                            final_result.setText("অনুগ্রহ করে আপনার জিওকোডের জন্য একটি সহজ নাম ইনপুট করুন");
                            addAgain.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Something wrong with server", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("geoCode",geoCode);
                params.put("userId",String.valueOf(user.getId()));
                params.put("locationId",locationSerial);

                return requestHandler.sendPostRequest(URLs.URL_GEOCODE, params);
            }
        }

        GeoCode gc = new GeoCode();
        gc.execute();
    }
}