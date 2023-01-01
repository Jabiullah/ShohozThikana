package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ShareCode extends AppCompatActivity {
    FloatingActionButton btn_home;
    String geocodePK;
    String GEOCODE;
    TextView h,f;
    ImageView Add;
    AutoCompleteTextView text;
    Button janBhai;
    String[] Phone={"01966640241","01688798675","01777015891","01834847719","01590044148","01535120716","01611451002"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_code);
        h = (TextView) findViewById(R.id.info_head);
        f = findViewById(R.id.FailedResult);
        Add = findViewById(R.id.add);
        janBhai = findViewById(R.id.HomeJan);

        Bundle bundleAnother = getIntent().getExtras();
        if (bundleAnother != null) {
            geocodePK        = bundleAnother.getString("GEO_ID");
            GEOCODE          = bundleAnother.getString("GEOCODE");
        }
        h.setText("আপনার জিওকোড "+GEOCODE+" আপনার বন্ধু, পরিবার এবং সহকর্মীদের সাথে ভাগ করুন");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Phone);
        text       =(AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewAnother);
        text.setAdapter(adapter);
        text.setThreshold(1);



        btn_home = findViewById(R.id.homeReturn);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(ShareCode.this, GeoList.class);
                startActivity(home_intent);
                finish();
            }
        });

        janBhai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(ShareCode.this, GeoList.class);
                startActivity(home_intent);
                finish();
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUser();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(ShareCode.this, GeoList.class);
        startActivity(home_intent);
        finish();
    }
    private void AddUser(){
        if(TextUtils.isEmpty(text.getText().toString())){
            text.setError("ফোন নম্বর ক্ষেত্র খালি");
            text.setText("");
            text.requestFocus();
            return;
        }
        if(text.getText().toString().length()<=10){
            text.setError("একটি বৈধ ফোন নম্বর ইনপুট করুন");
            text.setText("");
            text.requestFocus();
            return;
        }

        if(text.getText().toString().length()>11){
            text.setError("একটি বৈধ ফোন নম্বর ইনপুট করুন");
            text.requestFocus();
            text.setText("");
            return;
        }
        if(text.getText().toString().contains("SELECT * FROM")){
            text.setError("একটি বৈধ ফোন নম্বর ইনপুট করুন");
            text.requestFocus();
            text.setText("");
            return;
        }

        String Phone = text.getText().toString().trim();
        class shareUser extends AsyncTask<Void, Void, String> {
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
                        final String user_message = obj.getString("message");
                        //creating a new location object
                        if (user_message.contains("Insert Successful")) {
                            //
                            //display.setText("আপনার সহজ ঠিকানা হচ্ছে - ");

                            f.setVisibility(View.VISIBLE);
                            f.setText("আমরা আপনার বিশ্বস্ত ব্যক্তির সাথে আপনার কোড শেয়ার করেছি৷");
                            janBhai.setVisibility(View.VISIBLE);
                        }else{
                            f.setVisibility(View.VISIBLE);
                            f.setText("দুঃখিত, ব্যবহারকারী আমাদের সিস্টেমে নিবন্ধিত নয়");
                            janBhai.setVisibility(View.VISIBLE);
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
                params.put("user_phone", Phone);
                params.put("geoPK", geocodePK);

                return requestHandler.sendPostRequest(URLs.URL_ADD_USER_GEOCODE, params);
            }
        }

        shareUser ds = new shareUser();
        ds.execute();
    }
}