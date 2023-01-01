package com.example.myapplication;

public class SearchGeo {
    private String geo_code, location_name, location_house, location_street;

    public SearchGeo(String geo_code, String location_name, String location_house, String location_street) {
        this.geo_code = geo_code;
        this.location_name = location_name;
        this.location_house = location_house;
        this.location_street = location_street;
    }

    public String getCode() {
        return geo_code;
    }

    public String getHouseName() {
        return location_name;
    }

    public String getHouse() {
        return location_house;}

    public String getStreet(){return location_street;}

}
