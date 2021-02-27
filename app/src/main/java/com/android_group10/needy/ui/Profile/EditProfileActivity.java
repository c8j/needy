package com.android_group10.needy.ui.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android_group10.needy.R;
import com.android_group10.needy.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {
    private TextView firstNameText;
    private TextView lastNameText;
    private EditText cityText;
    private EditText zipcodeText;
    private TextView phNumText;
    private ImageView profilePicture;
    private ImageButton editPic;
    private Uri imageURI;
    private Button updateButton;
    private String uid;
    private DatabaseReference userRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
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
        //Glide.with(this).load(currentUser[0].getImgKey()).centerCrop().placeholder(R.drawable.anonymous_mask).into(imgView);
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
            storeProfilePic();
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
        //Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/posts/" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
    }

    public void storeProfilePic(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final String RANDOM_KEY = UUID.randomUUID().toString();
        StorageReference profilePicImageRef = storageReference.child("profile_images/" + RANDOM_KEY);
        profilePicImageRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                currentUser[0].setImgKey(RANDOM_KEY);
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("Image failed to upload.");
            }
        });

// While the file names are the same, the references point to different files
        //profilePicStored.getName().equals(profilePicImageRef.getName());    // true
        //profilePicStored.getPath().equals(profilePicImageRef.getPath());    // false
    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}