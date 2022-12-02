package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class profilePage extends AppCompatActivity {
    EditText EdPhone, EdEmail ,EdName;
    TextView TxLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, loginPage.class));
        }

        EdName   = findViewById(R.id.name);
        EdPhone  = findViewById(R.id.phone);
        EdEmail  = findViewById(R.id.email);
        TxLogout = findViewById(R.id.logout);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        EdPhone.setText(user.getPhone());
        EdEmail.setText(user.getEmail());
         EdName.setText(user.getUser_name());

        TxLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

    }
}