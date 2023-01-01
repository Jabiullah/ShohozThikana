package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class profilePage extends AppCompatActivity {
    EditText EdPhone, EdEmail ,EdName;
    TextView TxLogout,TxEdit,TxUpdateComplete;

    FloatingActionButton btn_home;

    private static Context mCtx;

    private static final int CODE_POST_REQUEST = 1025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, loginPage.class));
        }
        btn_home = findViewById(R.id.back_home);

        EdName   = findViewById(R.id.name);
        EdPhone  = findViewById(R.id.phone);
        EdEmail  = findViewById(R.id.email);

        TxLogout = findViewById(R.id.logout);
        TxEdit   = findViewById(R.id.update);
        TxUpdateComplete = findViewById(R.id.update_done);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        EdPhone.setText(user.getPhone());
        EdEmail.setText(user.getEmail());
         EdName.setText(user.getUser_name());

        TxEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TxEdit.setVisibility(View.GONE);
                TxUpdateComplete.setVisibility(View.VISIBLE);
                EdEmail.setEnabled(true);
                EdName.setEnabled(true);
            }
        });

        TxUpdateComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name  = EdName.getText().toString();
                String email = EdEmail.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    EdName.setError("Please enter Name");
                    EdName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    EdEmail.setError("Please enter Email");
                    EdEmail.requestFocus();
                    return;
                }

                HashMap<String, String> params = new HashMap<>();

                params.put("user_id",String.valueOf(user.getId()));
                params.put("user_name", name);
                params.put("user_email", email);

                PerformNetworkRequest request = new PerformNetworkRequest(URLs.URL_PROFILE, params, CODE_POST_REQUEST);
                request.execute();

                TxEdit.setVisibility(View.VISIBLE);
                TxUpdateComplete.setVisibility(View.GONE);
                EdEmail.setEnabled(false);
                EdName.setEnabled(false);

                Toast.makeText(getApplicationContext(),("Successfully Updated"), Toast.LENGTH_SHORT).show();

                SharedPrefManager s = new SharedPrefManager();
                s.changeInProfile(email,name);

                User user = SharedPrefManager.getInstance(mCtx).getUser();
                EdName.setText(String.valueOf((user.getUser_name())));
                EdEmail.setText(String.valueOf((user.getEmail())));

            }
        });

        TxLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(profilePage.this, homePage.class);
                startActivity(home_intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(profilePage.this, homePage.class);
        startActivity(home_intent);
        finish();
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);

            return null;
        }
    }

}