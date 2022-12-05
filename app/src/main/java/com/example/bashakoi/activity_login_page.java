package com.example.bashakoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class activity_login_page extends AppCompatActivity {
    EditText phone,bdPart;
    Button btn_otp;
    TextView header,caption,information;

    FirebaseAuth mAuth;
    String verificationID;

    Boolean btn_otp_click = false;
    String number="";

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
                    Toast.makeText(activity_login_page.this, "Enter a valid Number", Toast.LENGTH_SHORT).show();
                }
                if(btn_otp_click==false){
                    number = phone.getText().toString();
                    sendverificationcode(number);

                    // how to put center android:layout_centerHorizontal="true"
                    header.setVisibility(View.GONE);
                    caption.setText("\n\n অনুগ্রহ করে ভেরিফিকেশন সম্পূর্ণ করুণ \n\n");
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
            Toast.makeText(activity_login_page.this, "যাচাই ব্যর্থ", Toast.LENGTH_SHORT).show();
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
                    //Database check now.
//                    startActivity(new Intent(activity_login_page.this, otpPage.class));
                    authenticationUser();
                }
            }
        });
    }

    private void authenticationUser() {
        final String phone = number;

        class UserLogin extends AsyncTask<Void, Void, String> {
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
                        JSONObject userJson = obj.getJSONObject("servicer");

                        Toast.makeText(activity_login_page.this, "Great job ! bashaKoi", Toast.LENGTH_SHORT).show();

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("servicer_id"),
                                userJson.getString("servicer_phone"),
                                userJson.getString("servicer_email"),
                                userJson.getString("company_name"),
                                userJson.getString("company_id")

                                );

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();

                        if(user_message.contains("old")){

                            Intent intent_home = new Intent(getApplicationContext(), home_page.class);

                            Toast.makeText(getApplicationContext(), "স্বাগতম পুনরায় লগইন করার জন্য", Toast.LENGTH_SHORT).show();

                            startActivity(intent_home);

                        }else {

                            Intent intent_recovery = new Intent(getApplicationContext(), Registration.class);
                            intent_recovery.putExtra("phone", phone);

                            Toast.makeText(getApplicationContext(), "স্বাগতম আপনাকে আমাদের প্লাটফর্মে", Toast.LENGTH_SHORT).show();


                            startActivity(intent_recovery);

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
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
                params.put("servicer_phone", phone);

                return requestHandler.sendPostRequest(URLs.URL_AUTH, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}


