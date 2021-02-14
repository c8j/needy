package com.android_group10.needy.ui.LogInAndRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android_group10.needy.MainActivity;
import com.android_group10.needy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private Button logInButton;
    private TextView forgetPassword, registerInLogIn;
    private EditText logInEmail, logInPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private EditText forgetPasswordEmail;
    private Button resetPasswordButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setTitle("Log In");
        initializeItems();

    }

    public void initializeItems() {
        logInButton = findViewById(R.id.logInButton);
        logInPassword = findViewById(R.id.logInPassword);
        logInEmail = findViewById(R.id.logInEmail);
        registerInLogIn = findViewById(R.id.registerInLogIn);
        forgetPassword = findViewById(R.id.forgetPassword);
        firebaseAuth = firebaseAuth.getInstance();


        registerInLogIn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        logInEmail.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerInLogIn) {
            Intent intent = new Intent(LogIn.this, Register.class);
            startActivity(intent);
        } else if (v.getId() == R.id.forgetPassword) {
            forgetPasswordDialog();
        } else if (v.getId() == R.id.logInButton) {
            loginAction();
        }
    }

    public void forgetPasswordDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.forget_passwrod, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        forgetPasswordEmail = view.findViewById(R.id.email_forget_password);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);

        firebaseAuth = FirebaseAuth.getInstance();
        resetPasswordButton.setOnClickListener(v -> {
            resetPassword();
        });
        alertD.setView(view);
        alertD.show();
    }

    public void loginAction() {
        String email = logInEmail.getText().toString().trim();
        String password = logInPassword.getText().toString().trim();

        if (email.isEmpty()) {
            errorMessage(logInEmail, "Email is required");
        } else if (password.isEmpty()) {
            errorMessage(logInPassword, "Password is required");
        } else if ((!Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            errorMessage(logInEmail, "please Provide valid email ");
        } else if (password.length() < 6) {
            errorMessage(logInPassword, "Password must be more than 6 Characters");
        } else {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user.isEmailVerified()) {
                            startActivity(new Intent(LogIn.this, MainActivity.class));

                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(LogIn.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LogIn.this, "Failed to login! Please check your Email and Password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void errorMessage(EditText editText, String text) {
        editText.setError(text);
        editText.requestFocus();
    }

    public void resetPassword() {
        String email = forgetPasswordEmail.getText().toString().trim();

        if (email.isEmpty()) {
            errorMessage(forgetPasswordEmail, "Email is required");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage(forgetPasswordEmail, "Please provide valid email");
        }
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(LogIn.this, "Check your email", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(LogIn.this, "Check your email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}