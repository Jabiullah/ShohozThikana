package com.example.bashakoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProfilePage extends AppCompatActivity {
    EditText EdPhone, EdEmail ,EdName, EdCompanyName, EdCompanyId;
    TextView TxLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Registration.class));
        }

        EdName   = findViewById(R.id.name);
        EdPhone  = findViewById(R.id.phone);
        EdEmail  = findViewById(R.id.email);
        EdCompanyName = findViewById(R.id.company_name);
        EdCompanyId = findViewById(R.id.company_id);
        TxLogout = findViewById(R.id.logout);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        EdPhone.setText(user.getPhone());
        EdEmail.setText(user.getEmail());
        EdName.setText(user.getServicer_name());
        EdCompanyName.setText(user.getCompany());
        EdCompanyId.setText(user.getCompanyId());

        TxLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }
}