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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Search_Geo_Code extends AppCompatActivity {
    FloatingActionButton btn_back;
    AutoCompleteTextView text;
    ImageView search;
    User user          = SharedPrefManager.getInstance(this).getUser();
    TextView t1;

    String[] languages={"Shakil325 ","Shakil100","Shakil191","Shakil161","Shakil50","Adiba18","Adiba14"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_geo_code);
        btn_back   = findViewById(R.id.back_home);
        text       =(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        search     = findViewById(R.id.search);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);


        t1 = findViewById(R.id.searchResult);

        text.setAdapter(adapter);
        text.setThreshold(1);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(Search_Geo_Code.this, homePage.class);
                startActivity(home_intent);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(text.getText().toString())){
                    text.setError("ইনপুট ক্ষেত্র খালি");
                    text.requestFocus();
                    return;
                }else{
                    search();
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(Search_Geo_Code.this, homePage.class);
        startActivity(home_intent);
        finish();
    }

    private void search() {

        String search = text.getText().toString().trim();
        String userId = String.valueOf(user.getId());
        class search extends AsyncTask<Void, Void, String> {
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
                        //getting the user from the response
                        JSONObject searchJson = obj.getJSONObject("user");
                        //creating a new user object
                        SearchGeo search_geo = new SearchGeo(
                                searchJson.getString("geo_code"),
                                searchJson.getString("location_name"),
                                searchJson.getString("location_house"),
                                searchJson.getString("location_street")
                        );
                        t1.setVisibility(View.VISIBLE);
                        t1.setText("আমরা আপনার ঠিকানা খুঁজে পেয়েছি \n\nজিওকোড "+ search_geo.getCode()+"\nস্থানের নাম  "+search_geo.getHouseName() +"\nবাসা নং. "+search_geo.getHouse()+"\nরাস্তা, এলাকার নাম "+search_geo.getStreet());
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid search input", Toast.LENGTH_SHORT).show();
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
                params.put("search", search);
                params.put("userId", userId);
                return requestHandler.sendPostRequest(URLs.URL_SEARCH, params);
            }
        }

        search ul = new search();
        ul.execute();
    }
}