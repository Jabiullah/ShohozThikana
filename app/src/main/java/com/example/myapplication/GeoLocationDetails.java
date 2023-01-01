package com.example.myapplication;

public class GeoLocationDetails {
    private String geocode;
    private String locationName;
    private String locationHouse;
    private String locationStreet;
    private String locationCategory;
    public GeoLocationDetails(String geocode, String locationName,String locationHouse, String locationStreet,String locationCategory) {
        this.geocode            = geocode;
        this.locationName       = locationName;
        this.locationHouse      = locationHouse;
        this.locationStreet     = locationStreet;
        this.locationCategory   = locationCategory;
    }
    public String getGeocode() {
        return geocode;
    }
    public String getLocationName() {return locationName;}
    public String getLocationHouse() {
        return locationHouse;
    }
    public String getLocationStreet() {return locationStreet;}
    public String getLocationCategory() {return locationCategory;}
}
