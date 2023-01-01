package com.example.myapplication;

public class URLs {
    private static final String ROOT_URL = "http://10.10.200.254/bashakoi/";

    public static final String URL_AUTH                 =  ROOT_URL + "Authentication.php?";
    public static final String URL_R_EMAIL              =  ROOT_URL + "RecoveryEmail.php?";
    public static final String URL_LOCATION_INFO_INSERT =  ROOT_URL + "InsertLocationTextInfo.php?" ;
    public static final String URL_PROFILE              =  ROOT_URL + "ProfileUpdate.php?" ;
    public static final String URL_GEOCODE              =  ROOT_URL + "GeoCode.php?" ;
    public static final String URL_GEOCODE_DISPLAY      =  ROOT_URL + "GeoCodeDisplay.php?user_id=" ;
    public static final String URL_SHARED_GEOCODE_DISPLAY= ROOT_URL + "SharedGeoCodeDisplay.php?user_id=" ;
    public static final String URL_GEOCODE_DELETE       =  ROOT_URL + "DeleteGeoCode.php?geo_id=" ;
    public static final String URL_AUDIENCE             =  ROOT_URL + "Audience.php?geo_id=" ;
    public static final String URL_SEARCH               =  ROOT_URL + "SearchGeoCode.php?" ;
    public static final String URL_ADD_USER_GEOCODE     =  ROOT_URL + "AddUser.php?" ;
    public static final String URL_VOICE_FILE_PATH      =  ROOT_URL + "voice.php?" ;
}
