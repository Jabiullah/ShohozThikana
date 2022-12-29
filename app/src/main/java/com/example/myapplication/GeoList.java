package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeoList extends AppCompatActivity {
    List<GeoLocationDetails> GeoLocationList;
    RecyclerView recyclerView;
    User user = SharedPrefManager.getInstance(this).getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGEOLIST);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GeoLocationList = new ArrayList<>();
        load_GEOLOCATION_list();
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
                                        n.getString("locationStreet")
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