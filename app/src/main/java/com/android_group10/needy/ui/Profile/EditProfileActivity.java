package com.android_group10.needy.ui.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android_group10.needy.ImageHelper;
import com.android_group10.needy.R;
import com.android_group10.needy.User;
import com.android_group10.needy.ui.ToDo.ToDoFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.android_group10.needy.ImageHelper.decodeSampledBitmapFromPath;

public class EditProfileActivity extends AppCompatActivity {
    TextView firstNameText;
    TextView lastNameText;
    EditText cityText;
    EditText zipcodeText;
    TextView phNumText;
    ImageView profilePicture;
    ImageButton editPic;
    Uri imageURI;
    Button updateButton;
    String uid;
    DatabaseReference userRef;
    private final User[] currentUser = new User[1];
    private static final int GALLERY_REQ_CODE = 10;
    private static final String TAG = "EditProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firstNameText = findViewById(R.id.editFirstNameTextView);
        lastNameText = findViewById(R.id.editLastNameTextView);
        cityText = findViewById(R.id.cityEditText);
        zipcodeText = findViewById(R.id.zipEditText);
        phNumText = findViewById(R.id.editPhoneTextView);
        profilePicture = findViewById(R.id.editPicImageView);
        editPic = findViewById(R.id.editPicimageButton);
        updateButton = findViewById(R.id.updateButton);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                phNumText.setText(snapshot.child("phone").getValue(String.class));
                cityText.setText(snapshot.child("city").getValue(String.class));
                Uri profilePic = currentUser[0].getImgUri();
                if(profilePic != null){
                    showPicture(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(this, "Could not find profile.", Toast.LENGTH_SHORT).show();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    public void onEditImageClick(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQ_CODE){
            if(resultCode == Activity.RESULT_OK){
                imageURI = data.getData();
                //Log.i(TAG, "Image URI : " + imageURI);
                //String imageURL = imageURI.getPath();
                //Bitmap imageBitmap = ImageHelper.decodeSampledBitmapFromPath(imageURI.toString(), profilePicture.getWidth(), profilePicture.getHeight());
                //profilePicture.setImageURI(imageURI);
                Glide.with(this).load(imageURI).centerCrop().placeholder(R.drawable.anonymous_mask).into(profilePicture);
            }
        }
    }

    public void showPicture(ImageView imgView){
        Glide.with(this).load(currentUser[0].getImgUri()).centerCrop().placeholder(R.drawable.anonymous_mask).into(imgView);
    }

    private void updateProfile(){
        String newCity = cityText.getText().toString();
        String newZip = zipcodeText.getText().toString();
        currentUser[0].setCity(newCity);
        if(!newZip.matches("\\d{5}"))
            Toast.makeText(this, "Please provide a valid Zip-code", Toast.LENGTH_SHORT).show();
        else{
            currentUser[0].setZipCode(Integer.parseInt(newZip));
            //currentUser[0].setImgUri(imageURI);
            Map<String, Object> postValues = currentUser[0].toMap();
            userRef.updateChildren(postValues);
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
        //Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/posts/" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
    }
}