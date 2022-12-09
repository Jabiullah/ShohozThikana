package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class voiceAttach extends AppCompatActivity {

    Button record;
    //audio Record
    private static int MICRROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String location_name,location_house,location_street,category="";
    EditText instruction;

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



        record      = findViewById(R.id.record_button);
        instruction = findViewById(R.id.audio_ins);
        //audio file related Work
        if(isMicrophonePresent()) {
            getMicrophonePermission();
        }

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //started recording process
                if(record.getText().toString().equals("Start Recording")){
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

        File file = new File(musicDirectory,  instruction.getText().toString().trim()+".mp3");

        return file.getPath();
    }
}