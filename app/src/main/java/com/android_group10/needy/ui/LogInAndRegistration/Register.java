package com.android_group10.needy.ui.LogInAndRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android_group10.needy.MainActivity;
import com.android_group10.needy.R;
import com.android_group10.needy.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText registerPassword, registerCity, registerPhone, registerFirstName, registerLastName, registerEmail, registerZip;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeElements();

    }

    public void initializeElements() {

        registerPassword = findViewById(R.id.registerPassword);
        registerCity = findViewById(R.id.registerCity);
        registerPhone = findViewById(R.id.registerPhone);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);
        registerEmail = findViewById(R.id.registerEmail);
        registerButton = findViewById(R.id.registerButton);
        registerZip = findViewById(R.id.zip);
        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> {
            registerUser();
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    public void updateUI(FirebaseUser account) {

        if (account != null) {
            Toast.makeText(this, "You Signed In successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));

        } else {
            Toast.makeText(this, "You haven't signed in", Toast.LENGTH_LONG).show();
        }

    }

    public void registerUser() {
        String email = registerEmail.getText().toString().trim();
        String firstName = registerFirstName.getText().toString().trim();
        String lastName = registerLastName.getText().toString().trim();
        String phone = registerPhone.getText().toString().trim();
        String city = registerCity.getText().toString().trim();
        String zipCode = registerZip.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        if (firstName.isEmpty()) {

            errorMessage(registerFirstName, "First Name is required ");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            errorMessage(registerEmail, "please Provide valid email ");
        }
        if (lastName.isEmpty()) {

            errorMessage(registerLastName, "Last Name is required ");
        }
        if (phone.isEmpty()) {

            errorMessage(registerPhone, "Phone is required ");
        }
        if (firstName.isEmpty()) {

            errorMessage(registerCity, "First Name is required ");
        }
        if (zipCode.isEmpty()) {

            errorMessage(registerZip, "Zip Code is required ");
        }
        if (password.isEmpty()) {

            errorMessage(registerPassword, "Password is required ");
        }
        if (password.length() < 6) {

            errorMessage(registerPassword, "Password must be more than 6 Characters ");
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User(email, password, firstName, lastName, phone, city, Integer.parseInt(zipCode));
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Register.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    } else {
                        Toast.makeText(Register.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void errorMessage(EditText editText, String text) {
        editText.setError(text);
        editText.requestFocus();
    }
}