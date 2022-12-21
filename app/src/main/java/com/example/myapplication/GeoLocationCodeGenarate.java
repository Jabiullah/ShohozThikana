package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
            voiceFileURL    = bundle_geo.getString("voiceFilePath");
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

        geoCodeGenerate();
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
                        }else {
                            inputName.setVisibility(View.VISIBLE);
                            inputName.setText("");
                            final_result.setText("Please input a simple name for your GeoCode");
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