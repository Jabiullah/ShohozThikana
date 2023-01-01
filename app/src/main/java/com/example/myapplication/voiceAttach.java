package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class voiceAttach extends AppCompatActivity {
    Button record,another_Record,btnImage;
    String location_serial;
    FloatingActionButton addAnotherVoice;
    FloatingActionButton crossVoice;

    //audio Record
    private static int MICRROPHONE_PERMISSION_CODE = 200;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    EditText instruction,anotherInstruction;

    String location_name,location_house,location_street,category="";

    String[] voiceFilePath = new String[1]; // Creating a new Array of Size 2
    int increment = 0;
    SharedPrefManager sharedPrefManager = new SharedPrefManager();
    int userID  = sharedPrefManager.getUser().getId();

    FloatingActionButton btn_bck_info;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_attach);

        // here is the data from informationAttach
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            location_name    = bundle.getString("location_name");
            location_house   = bundle.getString("location_house");
            location_street  = bundle.getString("location_street");
            category         = bundle.getString("category");
        }

        record              = findViewById(R.id.record_button);
        instruction         = findViewById(R.id.audio_ins);


        another_Record      = findViewById(R.id.record_button_next);
        anotherInstruction  = findViewById(R.id.audio_ins_another);

        crossVoice          = findViewById(R.id.cross);

        btn_bck_info        = findViewById(R.id.back_info_attach);
        btnImage            = findViewById(R.id.imagePage);

        btn_bck_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle_back = new Bundle();
                bundle_back.putString("location_name",location_name);
                bundle_back.putString("location_house",location_house);
                bundle_back.putString("location_street",location_street);
                bundle_back.putString("category",category);

                Intent intent_info = new Intent(voiceAttach.this, informationAttach.class);
                intent_info.putExtras(bundle_back);
                startActivity(intent_info);

            }
        });


        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle_geo   =  new Bundle();
                bundle_geo.putString("lastPartOfHouse",location_house);
                bundle_geo.putString("locationPK",location_serial);
                bundle_geo.putStringArray("voiceFilePath",voiceFilePath);
                Intent intent_geo = new Intent(voiceAttach.this, GeoLocationCodeGenarate.class);
                intent_geo.putExtras(bundle_geo);
                startActivity(intent_geo);
                // Toast.makeText(getApplicationContext(), Arrays.toString(voiceFilePath), Toast.LENGTH_SHORT).show();
            }
        });


        crossVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    another_Record.setVisibility(View.GONE);
                    anotherInstruction.setVisibility(View.GONE);
                    addAnotherVoice.setVisibility(View.VISIBLE);
                    crossVoice.setVisibility(View.GONE);
            }
        });


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //started recording process for first file
                if(TextUtils.isEmpty(instruction.getText().toString().trim())){
                    instruction.setError(" একটি দিক নির্দেশনা লিখে ভয়েজ ইনপুট করুন ");
                    instruction.requestFocus();
                    return;
                }else{
                    if(record.getText().toString().equals("রেকর্ড শুরু করুন")){
                        record.setText("রেকর্ডিং চলছে");
                        try{
                            mediaRecorder = new MediaRecorder();
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mediaRecorder.setOutputFile(getRecordingFilePath());
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            instruction.setEnabled(false);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if(record.getText().toString().equals("রেকর্ডিং চলছে")){ //stopped record
                        record.setText("এখন রেকর্ডিং হয়ে গেছে, এখন অডিও চেক করুন");
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }else if(record.getText().toString().equals("এখন রেকর্ডিং হয়ে গেছে, এখন অডিও চেক করুন")){ // playing record
                        record.setText("অডিও এখন চলছে");
                        try {
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(getRecordingFilePath());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            voiceFilePath[increment]=getRecordingFilePath();
                            btnImage.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        //audio file related Work
        if(isMicrophonePresent()) {
            getMicrophonePermission();
        }


        dataStore();
    }//end of OnCreate

    private void dataStore() {
        User user                    = SharedPrefManager.getInstance(this).getUser();
        class dataStore extends AsyncTask<Void, Void, String> {
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
                        location_serial = obj.getString("location_serial");

                        //creating a new location object
                        if (user_message.contains("Successful")) {
                            //
                            //display.setText("আপনার সহজ ঠিকানা হচ্ছে - ");

                        }
                        //error check

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid information", Toast.LENGTH_SHORT).show();
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
                params.put("location_name", location_name);
                params.put("location_house", location_house);
                params.put("location_street", location_street);
                params.put("location_category", category);
                params.put("user_serial", String.valueOf(user.getId()));

                return requestHandler.sendPostRequest(URLs.URL_LOCATION_INFO_INSERT, params);
            }
        }

        dataStore ds = new dataStore();
        ds.execute();
    }

    //microPhone Permission
    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }
    private void getMicrophonePermission (){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},MICRROPHONE_PERMISSION_CODE);

        }
    }

    //audio File Path.
    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file   = new File(musicDirectory,  String.valueOf(userID)+"-"+instruction.getText().toString().trim()+".mp3");
        return file.getPath();
    }


}