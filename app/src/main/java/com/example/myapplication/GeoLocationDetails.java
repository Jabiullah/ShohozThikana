package com.example.myapplication;

public class GeoLocationDetails {
    private String voiceLINK_ser;
    private int GEO_ID;
    private String geocode;
    private String locationName;
    private String locationHouse;
    private String locationStreet;
    private String locationCategory;
    public GeoLocationDetails(String voiceLINK_ser,int GEO_ID,String geocode, String locationName,String locationHouse, String locationStreet,String locationCategory) {
        this.voiceLINK_ser      = voiceLINK_ser;
        this.GEO_ID             = GEO_ID;
        this.geocode            = geocode;
        this.locationName       = locationName;
        this.locationHouse      = locationHouse;
        this.locationStreet     = locationStreet;
        this.locationCategory   = locationCategory;
    }
    public String getVoiceLINK_ser(){return voiceLINK_ser;}
    public int getGEO_ID() {return GEO_ID;}
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
