package com.android_group10.needy.ui.Profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    final User[] currentUser = new User[1];
    private StorageReference storageReference;
    private Uri imgUri;
    ImageView profilePictureImageView;
    private ProgressBar progressBar;
    private ImageButton editPic;
    private Uri imageURI;
    private static final int GALLERY_REQ_CODE = 10;
    String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView firstNameText = view.findViewById(R.id.profileFirstNameTextView);
        TextView lastNameText = view.findViewById(R.id.profileLastNameTextView);
        TextView cityText = view.findViewById(R.id.profileCityTextView);
        TextView zipcodeText = view.findViewById(R.id.profileZipcodeTextView);
        TextView phNumText = view.findViewById(R.id.profilePhoneNumTextView);
        TextView emailIdText = view.findViewById(R.id.profileEmailTextView);
        profilePictureImageView = view.findViewById(R.id.profilePicimageView);
        Button editButton = view.findViewById(R.id.profileEditButton);
        editPic = view.findViewById(R.id.editPicImageButton);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        ProfilePictureManager ppManager = new ProfilePictureManager();
        ppManager.displayProfilePic(getActivity(), profilePictureImageView);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser[0] = snapshot.getValue(User.class);
                //firstNameText.setText(currentUser[0].getFirstName());
                firstNameText.setText(snapshot.child("firstName").getValue(String.class));
                lastNameText.setText(snapshot.child("lastName").getValue(String.class));
                zipcodeText.setText(Integer.toString(snapshot.child("zipCode").getValue(Integer.class)));
                emailIdText.setText(snapshot.child("email").getValue(String.class));
                phNumText.setText(snapshot.child("phone").getValue(String.class));
                cityText.setText(snapshot.child("city").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Could not find profile.", Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                //startActivity(intent);
                editProfileDialog();
            }
        });

        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage(v);
            }
        });
        return view;
    }

    //tools:context="ui.Profile.EditProfileActivity"
    public void editProfileDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.activity_edit_profile, null);
        EditProfileDialogManager editmanager = new EditProfileDialogManager(getActivity());
        editmanager.onCreateDialog(view);
        final AlertDialog alertD = new AlertDialog.Builder(getContext()).create();
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertD.setView(view);
        alertD.show();
    }

    public void editImage(View view){
        editPic.setEnabled(false);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQ_CODE){
            if(resultCode == Activity.RESULT_OK){
                imageURI = data.getData();
                Log.i(TAG,"Obtained Uri");
                Glide.with(this).load(imageURI).centerCrop().placeholder(R.drawable.anonymous_mask).into(profilePictureImageView);
                uploadProfilePic();
            }
        }
    }

    public void uploadProfilePic(){
        progressBar.setVisibility(View.VISIBLE);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + uid + "/profile_pic");
        storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                editPic.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Image failed to upload.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                editPic.setEnabled(true);
            }
        });
    }

}
