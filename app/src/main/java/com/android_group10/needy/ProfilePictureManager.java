package com.android_group10.needy;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android_group10.needy.LocalDatabase.DbBitmapUtility;
import com.android_group10.needy.LocalDatabase.LocalDatabaseHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;

public class ProfilePictureManager {
    private static final String TAG = "ProfilePictureManager";
    public ProfilePictureManager(){
    }

    //Download image from local or remote storage, and display it in the window:
    public void displayProfilePic(Activity activity, ImageView dpImageView, boolean circleCrop){
        //First try to get it from local database:
        if(false) {}
        //If fails, get from remote firebase storage:
        else {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + uid + "/profile_pic");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    showPicture(activity, uri, dpImageView, circleCrop);
                }
            });
        }
    }

    public void showPicture(Activity activity, Uri uri, ImageView profilePictureImageView, boolean circlecrop){
        if(!circlecrop)
            Glide.with(activity).load(uri).centerCrop().placeholder(R.drawable.anonymous_mask).into(profilePictureImageView);
        else
            Glide.with(activity).load(uri).centerCrop().apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.anonymous_mask).into(profilePictureImageView);
    }

    public void uploadPicToRemote(Uri imageURI, String uid,Activity thisActivity){
        //progressBar.setVisibility(View.VISIBLE);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + uid + "/profile_pic");
        storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(thisActivity, "Image Uploaded", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(thisActivity, "Image failed to upload.", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void uploadPicToLocalDb(Context contxt, Uri selectedImageUri, String userId) {
        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(contxt);
        try {
            localDatabaseHelper.open();
            InputStream iStream = contxt.getContentResolver().openInputStream(selectedImageUri);
            byte[] inputData = DbBitmapUtility.getBytes(iStream);
            localDatabaseHelper.insertImage(inputData, userId);
            localDatabaseHelper.close();
            //Log.i(TAG, "Inserted image");
        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            localDatabaseHelper.close();
        }
    }
}
