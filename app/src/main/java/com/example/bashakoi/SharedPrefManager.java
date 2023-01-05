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
                sharedPreferences.getString(COM_ID, null),
                sharedPreferences.getString(COM_NAME, null));

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

    public void changeInProfile(String s,String r, String com_n, String com_id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, s);
        editor.putString(KEY_EMAIL, r);
        editor.putString(COM_NAME, com_n);
        editor.putString(COM_ID, com_id);
        editor.apply();
    }

}
