package com.example.bashakoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfilePage extends AppCompatActivity {
    EditText EdPhone, EdEmail ,EdName, EdCompanyName, EdCompanyId;
    TextView TxLogout, Update, updateText, update_done;
    FloatingActionButton btn_home;

    private static Context mCtx;
    private static final int CODE_POST_REQUEST = 1025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Registration.class));
        }

        EdName   = findViewById(R.id.name);
        EdPhone  = findViewById(R.id.phone);
        EdEmail  = findViewById(R.id.email);
        EdCompanyName = findViewById(R.id.company_name);
        EdCompanyId = findViewById(R.id.company_id);
        TxLogout = findViewById(R.id.logout);
        Update = findViewById(R.id.update);
        updateText = findViewById(R.id.updateTextView);
        btn_home = findViewById(R.id.back_home);
        update_done =  findViewById(R.id.update_done);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(ProfilePage.this, home_page.class);
                startActivity(home_intent);
                finish();
            }
        });


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        EdName.setText(user.getServicer_name());
        EdEmail.setText(user.getEmail());
        EdPhone.setText(user.getPhone());
        EdCompanyName.setText(user.getCompany());
        EdCompanyId.setText(user.getCompanyId());


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateText.setVisibility(View.VISIBLE);
//  Enabled Edittext bt clicking the update button
                EdName.setInputType(InputType.TYPE_CLASS_TEXT );
                EdEmail.setInputType(InputType.TYPE_CLASS_TEXT);
                EdCompanyName.setInputType(InputType.TYPE_CLASS_TEXT);
                EdCompanyId.setInputType(InputType.TYPE_CLASS_TEXT);
                update_done.setVisibility(View.VISIBLE);
                Update.setVisibility(View.GONE);


            }
        });
        update_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name  = EdName.getText().toString();
                String email = EdEmail.getText().toString().trim();
                String comName = EdCompanyName.getText().toString();
                String comId = EdCompanyId.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    EdName.setInputType(InputType.TYPE_CLASS_TEXT);
                    EdName.setError("Please enter Name");
                    EdName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    EdEmail.setInputType(InputType.TYPE_CLASS_TEXT);
                    EdEmail.setError("Please enter Email");
                    EdEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(comName)) {
                    EdCompanyName.setInputType(InputType.TYPE_CLASS_TEXT);
                    EdCompanyName.setError("Please enter company Name");
                    EdCompanyName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(comId)) {
                    EdCompanyId.setInputType(InputType.TYPE_CLASS_TEXT);
                    EdCompanyId.setError("Please enter companyId");
                    EdCompanyId.requestFocus();
                    return;
                }

                HashMap<String, String> params = new HashMap<>();

                params.put("servicer_id",String.valueOf(user.getId()));
                params.put("servicer_name", name);
                params.put("servicer_email", email);
                params.put("company_name", comName);
                params.put("company_id", comId);


                PerformNetworkRequest request = new PerformNetworkRequest(URLs.URL_PROFILE, params, CODE_POST_REQUEST);
                request.execute();

                //TxEdit.setVisibility(View.VISIBLE);
               // TxUpdateComplete.setVisibility(View.GONE);
                EdEmail.setEnabled(false);
                EdPhone.setEnabled(false);
                EdName.setEnabled(false);
                EdCompanyName.setEnabled(false);
                EdCompanyId.setEnabled(false);

                Toast.makeText(getApplicationContext(),("Successfully Updated"), Toast.LENGTH_SHORT).show();

                SharedPrefManager s = new SharedPrefManager();
                s.changeInProfile(name, email, comId, comName);

                User user = SharedPrefManager.getInstance(mCtx).getUser();
                EdName.setText(String.valueOf((user.getServicer_name())));
                EdEmail.setText(String.valueOf((user.getEmail())));
                EdCompanyName.setText(String.valueOf(user.getCompany()));
                EdCompanyId.setText(String.valueOf(user.getCompanyId()));
                update_done.setVisibility(View.GONE);


                Toast.makeText(ProfilePage.this, "পরিবর্তন সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show();

            }
        });

        TxLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });


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