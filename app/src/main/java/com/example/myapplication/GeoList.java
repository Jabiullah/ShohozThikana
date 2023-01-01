package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeoList extends AppCompatActivity {
    List<GeoLocationDetails> GeoLocationList;
    RecyclerView recyclerView;
    User user = SharedPrefManager.getInstance(this).getUser();

    FloatingActionButton btn_home;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGEOLIST);
        btn_home     = findViewById(R.id.homeReturn);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GeoLocationList = new ArrayList<>();
        load_GEOLOCATION_list();



        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(GeoList.this, homePage.class);
                startActivity(home_intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(GeoList.this, homePage.class);
        startActivity(home_intent);
        finish();
    }

    private void load_GEOLOCATION_list() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GEOCODE_DISPLAY+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject n = array.getJSONObject(i);
                                GeoLocationList.add(new GeoLocationDetails(
                                        n.getString("geocode"),
                                        n.getString("locationName"),
                                        n.getString("locationHouse"),
                                        n.getString("locationStreet"),
                                        n.getString("locationCategory")
                                ));
                            }
                            GeoLocationAdapter adapter = new GeoLocationAdapter(GeoList.this, GeoLocationList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}