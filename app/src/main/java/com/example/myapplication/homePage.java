package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
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
    FloatingActionButton btn_profile,btn_geoCodeDisplay,btn_SharedGEOCode, btn_Search;
    ImageButton add_information;
    GoogleMap gMap;
    FrameLayout map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        btn_profile         = findViewById(R.id.fab);
        add_information     = findViewById(R.id.info_add);
        btn_geoCodeDisplay  = findViewById(R.id.GeoCodeSee);
        btn_SharedGEOCode   = findViewById(R.id.SharedGeoCode);
        btn_Search          = findViewById(R.id.SearchGEOCode);

        map = findViewById(R.id.mapView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);



        btn_geoCodeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent geo_intent = new Intent(homePage.this, GeoList.class);
                startActivity(geo_intent);
                finish();
            }
        });

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
        btn_SharedGEOCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharedGEO_intent = new Intent(homePage.this, sharedGEOList.class);
                startActivity(sharedGEO_intent);
                finish();
            }
        });
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent geo_intent = new Intent(homePage.this, Search_Geo_Code.class);
                startActivity(geo_intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(homePage.this);
        builder.setTitle("Exit")
                .setMessage("Are you sure you want to exit here ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng mapDhaka = new LatLng(23.7979, 90.4497);
        this.gMap.addMarker(new MarkerOptions().position(mapDhaka).title("UIU"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapDhaka));
    }
}