package com.example.bashakoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class home_page extends AppCompatActivity {
    Button btnClick, btnClientDetails;
    EditText textEnter, clientCode;
    TextToSpeech textToSpeech;
    FloatingActionButton btn_profile;
    TextView clientDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btn_profile = findViewById(R.id.fab);
        btnClick = findViewById(R.id.button2);
        textEnter = findViewById(R.id.text1);

        //Client Address generate by code no
        btnClientDetails = findViewById(R.id.goButton);
        clientCode = findViewById(R.id.codeNo);
        clientDetails = findViewById(R.id.showText);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    int language = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = textEnter.getText().toString();
                int speech = textToSpeech.speak(s,textToSpeech.QUEUE_FLUSH,null);

            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile_intent = new Intent(home_page.this, ProfilePage.class);
                startActivity(profile_intent);
                finish();
            }
        });

        //Client Address generate by code no
        btnClientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code = clientCode.getText().toString();
                    class ShowClient extends AsyncTask<Void, Void, String> {
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

                                    //getting the user from the response
                                    JSONObject userJson = obj.getJSONObject("client");

                                    Toast.makeText(home_page.this,  code, Toast.LENGTH_SHORT).show();

                                    //creating a new client object
                                    Client client = new Client(
                                            userJson.getInt("geocode_id"),
                                            userJson.getInt("user_se"),
                                            userJson.getInt("location_se"),
                                            userJson.getString("geo_code")
                                    );

                                    SharedPrefManager.getInstance(getApplicationContext()).getClient(client);

                                    clientDetails.setText(code);

                                } else {
                                    Toast.makeText(getApplicationContext(), code, Toast.LENGTH_SHORT).show();
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
                            params.put("geo_code", code);

                            return requestHandler.sendPostRequest(URLs.URL_CLIENT, params);
                        }
                    }

                    ShowClient ul = new ShowClient();
                    ul.execute();

            }
        });
    }
}