package com.example.bashakoi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManager {
    //the constants
    private static final String SHARED_PREF_NAME = "check";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";
    private static final String COM_NAME = "companyname";
    private static final String COM_ID = "companyid";

    // SharedPrefManager of Clients Part
    private static  final String KEY_GEO_CODE = "keygeocode";
    private static  final String KEY_GEO_CODE_ID = "keygeocodeid";
    private static  final String KEY_LOCATION_SE = "keylocationse";
    private static final String KEY_USER_SE = "keyuserse";


    private static SharedPrefManager mInstance;
    private static Context mCtx;
    public SharedPrefManager(){};
    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_NAME, user.getServicer_name());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(COM_NAME, user.getCompany());
        editor.putString(COM_ID, user.getCompanyId());


        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(COM_NAME, null),
                sharedPreferences.getString(COM_ID, null));
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }

    public void update(String servicer_name,String servicer_email, String servicer_phn, String company_name, String company_id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, servicer_name);
        editor.putString(KEY_EMAIL, servicer_email);
        editor.putString(KEY_PHONE, servicer_phn);
        editor.putString(COM_NAME, company_name);
        editor.putString(COM_ID, company_id);
        editor.apply();
    }

    // This method will get Clients details
    public void getClient(Client client) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_GEO_CODE_ID, client.getGeocode_id());
        editor.putString(KEY_GEO_CODE, client.getGeo_code());
        editor.putInt(KEY_USER_SE, client.getUser_se());
        editor.putInt(KEY_LOCATION_SE, client.getLocation_se());


        editor.apply();
    }
}
