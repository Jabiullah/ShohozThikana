package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class recoveryPage extends AppCompatActivity {
    EditText getEmail;
    Button btnEmail;
    String user_phone ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_page);

        getEmail = findViewById(R.id.recoveryEmail); // textView
        btnEmail = findViewById(R.id.btn_inputEmail); // button

        Intent intent = getIntent();
        user_phone = intent.getStringExtra("phone");

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoveryEmailUser();
            }
        });

    }
    private void recoveryEmailUser() {
        final String user_email = getEmail.getText().toString();

        class UserRecovery extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        final String user_message = obj.getString("message");

                        if(user_message.contains("Successful")){
                            //starting the profile activity
                            finish();

                            SharedPrefManager sobj = new SharedPrefManager();
                            sobj.update(user_email);


                            Intent intent_home = new Intent(getApplicationContext(), homePage.class);

                            Toast.makeText(getApplicationContext(), "স্বাগতম আপনাকে আমাদের প্লাটফর্মে", Toast.LENGTH_SHORT).show();

                            startActivity(intent_home);

                        }else{
                            // maybe some parameter-wise error happened
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_phone", user_phone);
                params.put("user_email", user_email);

                return requestHandler.sendPostRequest(URLs.URL_R_EMAIL, params);
            }
        }

        UserRecovery uR = new UserRecovery();
        uR.execute();
    }
}
