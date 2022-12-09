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
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class voiceAttach extends AppCompatActivity {
    Button record,another_Record;
    FloatingActionButton addAnotherVoice;
    FloatingActionButton crossVoice;

    //audio Record
    private static int MICRROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    EditText instruction,anotherInstruction;

    String location_name,location_house,location_street,category="";

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

        addAnotherVoice     = findViewById(R.id.AddVoiceAnother);
        crossVoice          = findViewById(R.id.cross);

        btn_bck_info        = findViewById(R.id.back_info_attach);

        addAnotherVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(instruction.getText())){
                    instruction.setError("Please input 1st voice first");
                    instruction.requestFocus();
                }else{
                    another_Record.setVisibility(View.VISIBLE);
                    anotherInstruction.setVisibility(View.VISIBLE);
                    addAnotherVoice.setVisibility(View.GONE);
                    crossVoice.setVisibility(View.VISIBLE);
                }
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

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //started recording process
                if(record.getText().toString().equals("রেকর্ড শুরু করুন")){
                    record.setText("Continuing recording");
                    try{
                        mediaRecorder = new MediaRecorder();
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mediaRecorder.setOutputFile(getRecordingFilePath());
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(record.getText().toString().equals("Continuing recording")){ //stopped record
                    record.setText("Now check Audio");

                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }else if(record.getText().toString().equals("Now check Audio")){ // playing record
                    record.setText("Playing the Audio");
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(getRecordingFilePath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }



            }
        });

        //audio file related Work
        if(isMicrophonePresent()) {
            getMicrophonePermission();
        }



    }//end of OnCreate

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

        File file = new File(musicDirectory,  instruction.getText().toString().trim()+".mp3");

        return file.getPath();
    }
}