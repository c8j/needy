package com.android_group10.needy.ui.LogInAndRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android_group10.needy.R;

public class LogIn extends AppCompatActivity {
    private Button btnLogIn;
    private TextView forgetPassword, join_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setTitle("Log In");
        initializeElements();


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void initializeElements() {
        btnLogIn = findViewById(R.id.btnLogIn);
        forgetPassword = findViewById(R.id.forgetPassword);
        join_now = findViewById(R.id.join_now);
    }
}