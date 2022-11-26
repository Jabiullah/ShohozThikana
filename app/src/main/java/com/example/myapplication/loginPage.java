package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginPage extends AppCompatActivity {
    EditText phone,bdPart;
    Button btn_otp;
    TextView header,caption,information;

    FirebaseAuth mAuth;
    String verificationID;
    Boolean btn_otp_click = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        header = findViewById(R.id.ltext1); // textView
        caption= findViewById(R.id.ltext2); // textView
        information = findViewById(R.id.information_for_start);
        bdPart = findViewById(R.id.ltext3); // EditText

        phone = findViewById(R.id.ltext4);
        btn_otp = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(loginPage.this, "Enter a valid Number", Toast.LENGTH_SHORT).show();
                }
                if(btn_otp_click==false){
                    String number = phone.getText().toString();
                    sendverificationcode(number);

                    // how to put center android:layout_centerHorizontal="true"
                    header.setVisibility(View.GONE);
                    caption.setText(" অনুগ্রহ করে ভেরিফিকেশন সম্পূর্ণ করুণ \n\n\n\n");
                    caption.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    information.setText("");
                    information.setText("\n আর মাত্র একটি ধাপ বাকি \n" +
                            "আপনার ওটিপি এই নাম্বারে  "+ number +"\n   পাঠানো হছে কিছুক্ষণের মধ্যেই");
                    information.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    bdPart.setVisibility(View.INVISIBLE);
                    phone.setText("");
                    phone.setHint(" ৬ ডিজিট ওটিপি ");
                    btn_otp.setText("ওটিপি চেক করুন");
                    btn_otp_click= true;
                }else if(btn_otp_click==true){  //"verifyCode" function
                    verifycode(phone.getText().toString());
                    btn_otp_click= false;
                }

            }
        });

    }
    private void sendverificationcode(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+88"+phoneNumber)        // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if(code!=null){
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(loginPage.this, "যাচাই ব্যর্থ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s,token);
            verificationID = s;
        }
    };

    private void verifycode(String Code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,Code);
        signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //successful part
                    Toast.makeText(loginPage.this, "Great job ! bashaKoi", Toast.LENGTH_SHORT).show();
                    //Database check now.
                    startActivity(new Intent(loginPage.this, otpPage.class));
                }
            }
        });
    }
}