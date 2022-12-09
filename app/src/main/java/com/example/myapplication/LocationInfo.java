package com.example.myapplication;

public class LocationInfo {
    private int location_id;
    private String location_name, location_house, location_street;

    public LocationInfo(int location_id, String location_name, String location_house, String location_street) {
        this.location_id = location_id;
        this.location_name = location_name;
        this.location_house = location_house;
        this.location_street = location_street;
    }

    public int getLocationId() {
        return location_id;
    }

    public String getLocationName() {return location_name;}

    public String getLocationHouse() {
        return location_house;
    }

    public String getLocationStreet(){return location_street;}
}
