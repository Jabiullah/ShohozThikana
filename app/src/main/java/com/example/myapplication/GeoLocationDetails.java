package com.example.myapplication;

public class GeoLocationDetails {
    private String geocode;
    private String locationName;
    private String locationHouse;
    private String locationStreet;

    public GeoLocationDetails(String geocode, String locationName,String locationHouse, String locationStreet) {
        this.geocode        = geocode;
        this.locationName   = locationName;
        this.locationHouse  = locationHouse;
        this.locationStreet = locationStreet;

    }
    public String getGeocode() {
        return geocode;
    }
    public String getLocationName() {return locationName;}
    public String getLocationHouse() {
        return locationStreet;
    }
    public String getLocationStreet() {return locationStreet;}
}
