package com.example.myapplication;

public class SharedGeoLocationDetails {
    private String geocode;
    private String locationName;
    private String locationHouse;
    private String locationStreet;
    private String locationGMAP;
    private String locationCategory;
    private String GeoCodeOwner;
    //geo.geo_code , l.location_name ,l.location_house , l.location_street ,  l.location_gmap , l.location_category ,user.user_name
    public SharedGeoLocationDetails(String geocode, String locationName,String locationHouse, String locationStreet,String locationGMAP, String locationCategory, String GeoCodeOwner) {
        this.geocode            = geocode;
        this.locationName       = locationName;
        this.locationHouse      = locationHouse;
        this.locationStreet     = locationStreet;
        this.locationGMAP       = locationGMAP;
        this.locationCategory   = locationCategory;
        this.GeoCodeOwner       = GeoCodeOwner;
    }
    public String getGeocode() {
        return geocode;
    }
    public String getLocationName() {return locationName;}
    public String getLocationHouse() {
        return locationHouse;
    }
    public String getLocationStreet() {return locationStreet;}
    public String getLocationGMAP() {return locationGMAP;}
    public String getLocationCategory() {return locationCategory;}
    public String getGeoCodeOwner(){return GeoCodeOwner;}
}
