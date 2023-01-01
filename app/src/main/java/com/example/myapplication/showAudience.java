package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class showAudience extends AppCompatActivity {
    String geocodePK;
    List<AudienceDetails> AudienceList;
    RecyclerView recyclerView;
    FloatingActionButton btn_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_audience);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            geocodePK        = bundle.getString("GEO_ID");
        }
        btn_home = findViewById(R.id.homeReturn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAudList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AudienceList = new ArrayList<>();
        load_Audience_list();
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(showAudience.this, GeoList.class);
                startActivity(home_intent);
                finish();
            }
        });
    }

    private void load_Audience_list() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_AUDIENCE+geocodePK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject n = array.getJSONObject(i);
                                AudienceList.add(new AudienceDetails(
                                        n.getString("name"),
                                        n.getString("phone")
                                ));
                            }
                            ShowAudienceAdapter adapter = new ShowAudienceAdapter(showAudience.this, AudienceList);
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

    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(showAudience.this, GeoList.class);
        startActivity(home_intent);
        finish();
    }
}