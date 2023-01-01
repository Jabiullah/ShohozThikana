package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class sharedGEOList extends AppCompatActivity {
    List<SharedGeoLocationDetails> SharedGeoLocationList;
    RecyclerView recyclerView;
    User user = SharedPrefManager.getInstance(this).getUser();
    FloatingActionButton btn_home;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_geolist);
        btn_home = findViewById(R.id.hReturn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerSharedGEOLIST);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedGeoLocationList = new ArrayList<>();
        load_SHARED_GEOLOCATION_list();
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(sharedGEOList.this, homePage.class);
                startActivity(home_intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(sharedGEOList.this, homePage.class);
        startActivity(home_intent);
        finish();
    }
    private void load_SHARED_GEOLOCATION_list() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_SHARED_GEOCODE_DISPLAY+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject n = array.getJSONObject(i);
                                SharedGeoLocationList.add(new SharedGeoLocationDetails(
                                        n.getString("geocode"),
                                        n.getString("locationName"),
                                        n.getString("locationHouse"),
                                        n.getString("locationStreet"),
                                        n.getString("locationGMAP"),
                                        n.getString("locationCategory"),
                                        n.getString("GeoCodeOwner")
                                ));
                            }
                            SharedGeoLocationAdapter adapter = new SharedGeoLocationAdapter(sharedGEOList.this, SharedGeoLocationList);
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