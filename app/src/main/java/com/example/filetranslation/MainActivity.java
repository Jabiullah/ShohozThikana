package com.example.filetranslation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner fromSpinner,toSpinner;
    private TextInputEditText sourceText;
    private ImageView micIV;
    private MaterialButton translateBtn;
    private TextView translateIV;

    String[] fromlanguage = {"From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bangali","Catalan","Czech","Hindi","Urdu"};

    String[] tolanguage = {"To","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bangali","Catalan","Czech","Hindi","Urdu"};

    private static final int REQUEST_PERMISSION_CODE = 1;

    int languageCode,fromLanguageCode,toLanguageCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);

        sourceText = findViewById(R.id.idEditSource);

        micIV = findViewById(R.id.idIVMic);

        translateBtn = findViewById(R.id.idBtnTranslation);

        translateIV = findViewById(R.id.idTranslatedTV);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromlanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.spinner_item,fromlanguage);

        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(fromAdapter);


        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                toLanguageCode = getLanguageCode(tolanguage[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.spinner_item,tolanguage);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

       micIV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
               intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
               intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
               intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something to translate");
               try{
                   startActivityForResult(intent,REQUEST_PERMISSION_CODE);
               }
               catch (Exception e){
                   e.printStackTrace();
                   Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
               }

           }
       });

       translateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               translateIV.setVisibility(view.VISIBLE);
               translateIV.setText("");
               if(sourceText.getText().toString().isEmpty()){
                   Toast.makeText(MainActivity.this,"please enter text to translate",Toast.LENGTH_SHORT).show();
               }
               else if(fromLanguageCode == 0){
                   Toast.makeText(MainActivity.this,"please select source language",Toast.LENGTH_SHORT).show();
               }
               else if(toLanguageCode == 0){
                   Toast.makeText(MainActivity.this,"please select the language to make translation",Toast.LENGTH_SHORT).show();

               }
               else {
                   //check
                   translateText(fromLanguageCode,toLanguageCode,sourceText.getText().toString());
               }
           }
       });

    }

    private void translateText(int fromLanguageCode, int toLanguageCode, String source) {
        translateIV.setText("Downloading Model,please wait...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translateIV.setText("Translating...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translateIV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Failed to translate!! try again",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Failed to download model!! check your internet connection",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PERMISSION_CODE){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            sourceText.setText(result.get(0));
        }

    }
    ///String[] fromlanguage = {"From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bangali","Catalan","Czech","Hindi","Urdu"};
    private int getLanguageCode(String language) {
        int languageCode = 0;
        switch (language){
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;

            case "Afrikaans":
                languageCode = FirebaseTranslateLanguage.AF;
                break;
            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Belarusian":
                languageCode = FirebaseTranslateLanguage.BE;
                break;
            case "Bulgarian":
                languageCode = FirebaseTranslateLanguage.BG;
                break;
            case "Bangali":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "Catalan":
                languageCode = FirebaseTranslateLanguage.CA;
                break;
            case "Czech":
                languageCode = FirebaseTranslateLanguage.CS;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Urdu":
                languageCode = FirebaseTranslateLanguage.UR;
                break;

            default:
                languageCode = 0;

        }
        return languageCode;

    }
}
