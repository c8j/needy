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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android_group10.needy.MainActivity;
import com.android_group10.needy.R;

import com.android_group10.needy.User;
import com.android_group10.needy.ui.SharedPreference;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;

import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;


public class LogIn extends AppCompatActivity implements View.OnClickListener {
    private EditText logInEmail, logInPassword, forgetPasswordEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private CheckBox rememberMeCheckBox;
    private CallbackManager callbackManager;
    public String id, email, firstName, lastName;
    private String facebookUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setTitle("Log In");
        initializeItems();
        keepLogin();


    }

    public void initializeItems() {
        Button logInButton = findViewById(R.id.logInButton);
        logInPassword = findViewById(R.id.logInPassword);
        logInEmail = findViewById(R.id.logInEmail);
        TextView registerInLogIn = findViewById(R.id.registerInLogIn);
        TextView forgetPassword = findViewById(R.id.forgetPassword);
        rememberMeCheckBox = findViewById(R.id.remeberMe);
        Button facebookButton = findViewById(R.id.facebook_login_button);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        rememberMe();
        firebaseAuth = FirebaseAuth.getInstance();

        registerInLogIn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        logInEmail.setOnClickListener(this);
        rememberMeCheckBox.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
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
        } else if (v.getId() == R.id.facebook_login_button) {
            logInWithFacebook();

        }
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            checkIfFacebookUserExistsInDataBase();

        } else {
            createToast("Please sign in to continue");
        }
    }

    public void forgetPasswordDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.forget_passwrod, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        forgetPasswordEmail = view.findViewById(R.id.email_forget_password);
        Button resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(v -> resetPassword());
        alertD.setView(view);
        alertD.show();
    }

    public void loginAction() {
        String inputEmail = logInEmail.getText().toString().trim();
        String inputPassword = logInPassword.getText().toString().trim();

        if (inputEmail.isEmpty()) {
            errorMessage(logInEmail, "Email is required");
        } else if (inputPassword.isEmpty()) {
            errorMessage(logInPassword, "Password is required");
        } else if ((!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches())) {
            errorMessage(logInEmail, "please Provide valid inputEmail ");
        } else if (inputPassword.length() < 6) {
            errorMessage(logInPassword, "Password must be more than 6 Characters");
        } else {

            if (rememberMeCheckBox.isChecked()) {
                SharedPreference mySharedPreference = new SharedPreference(LogIn.this, SharedPreference.SESSION_REMEMBERME);
                mySharedPreference.createRememberMeSession(inputEmail, inputPassword);

            }
            firebaseAuth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (firebaseUser.isEmailVerified()) {
                        startActivity(new Intent(LogIn.this, MainActivity.class));
                        finish();

                    } else {
                        firebaseUser.sendEmailVerification();
                        createToast("Check your inputEmail to verify your account!");
                    }
                } else {
                    try {
                        throw task.getException();
                    }
                    // if user enters wrong email.
                    catch (FirebaseAuthInvalidUserException invalidEmail) {

                        createToast("Check the Email or register yourself.");
                    }
                    // if user enters wrong password.
                    catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                        createToast("Wrong Password! Try again.");

                    } catch (Exception e) {

                        createToast("Failed to login! Please check your Email and Password");
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
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                createToast("Check your email");

            } else {

                createToast("Check your email");
            }
        });
    }

    public void rememberMe() {
        SharedPreference mySharedPreference = new SharedPreference(LogIn.this, SharedPreference.SESSION_REMEMBERME);
        if (mySharedPreference.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = mySharedPreference.getRememberMeDetailsFromSession();
            logInEmail.setText(rememberMeDetails.get(SharedPreference.KEY_SESSION_EMAIL));
            logInPassword.setText(rememberMeDetails.get(SharedPreference.KEY_SESSION_PASSWORD));
        }

    }

    public void createToast(String text) {
        Toast.makeText(LogIn.this, text, Toast.LENGTH_LONG).show();
    }

    public void keepLogin() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(LogIn.this, MainActivity.class));
            finish();
        }
    }


    // Facebook Methods


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LogIn.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                });
    }

    public void logInWithFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.facebook_login_button);
        loginButton.setPermissions("email", "public_profile", "user_birthday");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                updateUI(user);
            } else {
                updateUI(null);
            }
        };
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    firebaseAuth.signOut();
                } else {
                    loadUserProfile(currentAccessToken);
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(newAccessToken, ((object, response) -> {
            if (object != null) {
                try {
                    email = object.getString("email");
                    id = object.getString("id");
                    firstName = object.getString("first_name");
                    lastName = object.getString("last_name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
        Bundle params = new Bundle();
        params.putString("fields", "email,id,first_name,last_name");
        graphRequest.setParameters(params);
        graphRequest.executeAsync();
    }

    public void register_dialog() {
        EditText phoneNumber_editText, zipCode_editText, city_editText;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.register_dialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.setView(view);
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button confirm = view.findViewById(R.id.confirm_button_dialog);

        phoneNumber_editText = view.findViewById(R.id.Phone_dialog);
        zipCode_editText = view.findViewById(R.id.zip_dialog);
        city_editText = view.findViewById(R.id.City_dialog);


        alertD.setCancelable(true);
        alertD.show();
        confirm.setOnClickListener(v -> {

            if (phoneNumber_editText.getText().toString().isEmpty()) {
                errorMessage(phoneNumber_editText, "Required");
            } else if (zipCode_editText.getText().toString().isEmpty()) {
                errorMessage(zipCode_editText, "Required");
            } else if (city_editText.getText().toString().isEmpty()) {
                errorMessage(city_editText, "Required");
            } else {

                String phoneNumber = phoneNumber_editText.getText().toString().trim();
                int zipCode = Integer.parseInt(zipCode_editText.getText().toString().trim());
                String city = city_editText.getText().toString().trim();

                String password = "1111";
                registerNewFacebookUser(email, password, firstName, lastName, phoneNumber, city, zipCode);


                alertD.dismiss();

            }
        });

    }

    public void registerNewFacebookUser(String email, String password, String firstName, String lastName, String phone, String city,
                                        int zipCode) {
        User user = new User(email, password, firstName, lastName, phone, city, zipCode);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(facebookUserId).setValue(user);

        startActivity(new Intent(LogIn.this, MainActivity.class));
        finish();

    }


    public void checkIfFacebookUserExistsInDataBase() {
        facebookUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(facebookUserId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    register_dialog();
                } else {
                    startActivity(new Intent(LogIn.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }

}