package com.android_group10.needy.ui.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android_group10.needy.R;
import com.android_group10.needy.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class EditProfileDialogManager extends AppCompatActivity {
    private TextView firstNameText;
    private TextView lastNameText;
    private EditText cityText;
    private EditText zipcodeText;
    private EditText phNumEditText;
    private Button updateButton;
    private String uid;
    private DatabaseReference userRef;
    private final User[] currentUser = new User[1];
    private static final String TAG = "EditProfileActivity";
    Activity thisActivity;

    public EditProfileDialogManager(Activity activity){
        thisActivity = activity;
    }

    protected void onCreateDialog(View view) {
        firstNameText = view.findViewById(R.id.authFirstNameTextView);
        lastNameText = view.findViewById(R.id.authLastNameTextView);
        cityText = view.findViewById(R.id.cityEditText);
        zipcodeText = view.findViewById(R.id.zipEditText);
        phNumEditText = view.findViewById(R.id.phoneEditText);
        updateButton = view.findViewById(R.id.updateButton);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Get current values for fields in profile:
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser[0] = snapshot.getValue(User.class);
                //firstNameText.setText(currentUser[0].getFirstName());

                firstNameText.setText(snapshot.child("firstName").getValue(String.class));
                lastNameText.setText(snapshot.child("lastName").getValue(String.class));
                int zipCode = snapshot.child("zipCode").getValue(Integer.class);
                zipcodeText.setText(Integer.toString(zipCode));
                phNumEditText.setText(snapshot.child("phone").getValue(String.class));
                cityText.setText(snapshot.child("city").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(thisActivity, "Could not find profile.", Toast.LENGTH_SHORT).show();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile(){
        String newCity = cityText.getText().toString();
        currentUser[0].setCity(newCity);

        String newPhone = phNumEditText.getText().toString();
        currentUser[0].setPhone(newPhone);

        String newZip = zipcodeText.getText().toString();
        if(!newZip.matches("\\d{5}"))
            Toast.makeText(thisActivity, "Please provide a valid Zip-code", Toast.LENGTH_SHORT).show();
        else{
            currentUser[0].setZipCode(Integer.parseInt(newZip));
            Map<String, Object> postValues = currentUser[0].toMap();
            userRef.updateChildren(postValues);
            Toast.makeText(thisActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
}