package com.example.myapplication;

public class URLs {
    private static final String ROOT_URL = "http://192.168.1.3/bashakoi/";

    public static final String URL_AUTH                 =  ROOT_URL + "Authentication.php?" ;
    public static final String URL_R_EMAIL              =  ROOT_URL + "RecoveryEmail.php?" ;
    public static final String URL_LOCATION_INFO_INSERT =  ROOT_URL + "InsertLocationTextInfo.php?" ;
    public static final String URL_PROFILE              =  ROOT_URL + "ProfileUpdate.php?" ;
    public static final String URL_IMAGE                =  ROOT_URL + "UploadImage.php?" ;
    public static final String URL_GEOCODE              =  ROOT_URL + "GeoCode.php?" ;
    public static final String URL_GEOCODE_DISPLAY      =  ROOT_URL + "GeoCodeDisplay.php?user_id=" ;
}
