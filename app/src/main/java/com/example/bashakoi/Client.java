package com.example.bashakoi;

public class Client {
    private int geocode_id, user_se, location_se;
    private String geo_code;

    public Client(int geocode_id, int user_se, int location_se, String geo_code) {
        this.geocode_id = geocode_id;
        this.user_se = user_se;
        this.location_se = location_se;
        this.geo_code = geo_code;
    }

    public  int getGeocode_id(){ return geocode_id; }

    public int getUser_se() {
        return user_se;
    }

    public int getLocation_se() {return location_se;}

    public String getGeo_code() {
        return geo_code;
    }

}
