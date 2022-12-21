package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class homePage extends FragmentActivity implements OnMapReadyCallback {
    FloatingActionButton btn_profile;
    ImageButton add_information;
    GoogleMap gMap;
    FrameLayout map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btn_profile     = findViewById(R.id.fab);
        add_information = findViewById(R.id.info_add);

        map = findViewById(R.id.mapView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile_intent = new Intent(homePage.this, profilePage.class);
                startActivity(profile_intent);
                finish();
            }
        });

        add_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent information_intent = new Intent(homePage.this, informationAttach.class);
                startActivity(information_intent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng mapDhaka = new LatLng(23.8103, 90.4125);
        this.gMap.addMarker(new MarkerOptions().position(mapDhaka).title("Dhaka"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapDhaka));
    }
}