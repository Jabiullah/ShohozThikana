package com.example.bashakoi;

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

public class Registration extends AppCompatActivity {
    EditText companyName, companyId, servicerEmail, servicerName;
    Button submit;
    String servicer_phone ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        servicerName = findViewById(R.id.service_prov_name);
        servicerEmail = findViewById(R.id.servicer_email); // textView
        companyName = findViewById(R.id.company_name);
        companyId = findViewById(R.id.company_id);
        submit = findViewById(R.id.btn_submit); // button

        Intent intent = getIntent();
        servicer_phone = intent.getStringExtra("phone");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration();
            }
        });
    }

    private void registration() {
        final String servicer_name = servicerName.getText().toString();
        final String servicer_email = servicerEmail.getText().toString();
        final String company_name = companyName.getText().toString();
        final String company_id = companyId.getText().toString();



        class UserRegistration extends AsyncTask<Void, Void, String> {
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
                            sobj.update(servicer_phone,servicer_email, servicer_name,company_name,company_id);

                            Intent intent_home = new Intent(getApplicationContext(), home_page.class);

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
                params.put("servicer_phone", servicer_phone);
                params.put("servicer_name", servicer_name);
                params.put("servicer_email", servicer_email);
                params.put("company_name", company_name);
                params.put("company_id", company_id);



                return requestHandler.sendPostRequest(URLs.URL_R_EMAIL, params);
            }
        }

        UserRegistration uR = new UserRegistration();
        uR.execute();
    }

}