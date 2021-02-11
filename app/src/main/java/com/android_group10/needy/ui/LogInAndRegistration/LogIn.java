package com.android_group10.needy.ui.LogInAndRegistration;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

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



        btnLogIn.setOnClickListener(v -> {

        });
    }


}