package com.android_group10.needy.ui.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android_group10.needy.ProfilePictureManager;
import com.android_group10.needy.R;
import com.android_group10.needy.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class EditProfileDialogManager extends AppCompatActivity {
    private TextView firstNameText;
    private TextView lastNameText;
    private EditText cityText;
    private EditText zipcodeText;
    private TextView phNumText;
    private ImageView profilePictureImageView;
    private ImageButton editPic;
    private Uri imageURI;
    private Button updateButton;
    private String uid;
    private DatabaseReference userRef;
    private final User[] currentUser = new User[1];
    private static final int GALLERY_REQ_CODE = 10;
    private static final String TAG = "EditProfileActivity";
    Activity thisActivity;
    private ProgressBar progressBar;

    public EditProfileDialogManager(Activity activity){
        thisActivity = activity;
    }

    //protected void onCreate(Bundle savedInstanceState)
    protected void onCreateDialog(View view) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_profile);

        firstNameText = view.findViewById(R.id.editFirstNameTextView);
        lastNameText = view.findViewById(R.id.editLastNameTextView);
        cityText = view.findViewById(R.id.cityEditText);
        zipcodeText = view.findViewById(R.id.zipEditText);
        phNumText = view.findViewById(R.id.editPhoneTextView);
        profilePictureImageView = view.findViewById(R.id.editPicImageView);
        editPic = view.findViewById(R.id.editPicImageButton);
        updateButton = view.findViewById(R.id.updateButton);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ProfilePictureManager ppManager = new ProfilePictureManager();
        ppManager.displayProfilePic(thisActivity, profilePictureImageView);


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
                phNumText.setText(snapshot.child("phone").getValue(String.class));
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

    public void onEditImageClick(View view){
        editPic.setEnabled(false);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQ_CODE){
            if(resultCode == Activity.RESULT_OK){
                imageURI = data.getData();
                Log.i(TAG,"Obtained Uri");
                Glide.with(this).load(imageURI).centerCrop().placeholder(R.drawable.anonymous_mask).into(profilePictureImageView);
                //uploadProfilePic();
            }
        }
    }

    private void updateProfile(){
        String newCity = cityText.getText().toString();
        String newZip = zipcodeText.getText().toString();
        currentUser[0].setCity(newCity);
        if(!newZip.matches("\\d{5}"))
            Toast.makeText(thisActivity, "Please provide a valid Zip-code", Toast.LENGTH_SHORT).show();
        else{
            currentUser[0].setZipCode(Integer.parseInt(newZip));
            Map<String, Object> postValues = currentUser[0].toMap();
            userRef.updateChildren(postValues);
            Toast.makeText(thisActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadProfilePic(){
        progressBar.setVisibility(View.VISIBLE);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + uid + "/profile_pic");
        storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(thisActivity, "Image Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                editPic.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //makeToast("Image failed to upload.");
                Toast.makeText(thisActivity, "Image failed to upload.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}