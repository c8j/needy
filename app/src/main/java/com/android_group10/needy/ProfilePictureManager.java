package com.android_group10.needy;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfilePictureManager {
    public ProfilePictureManager(){
    }

    //Download image from local or remote storage, and display it in the window:
    public void displayProfilePic(Activity activity, ImageView dpImageView){
        //First try to get it from local database:
        if(false) {}
        //If fails, get from remote firebase storage:
        else {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + uid + "/profile_pic");
            //Log.i(TAG,"Storage reference = " + storageReference);
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    showPicture(activity, uri, dpImageView);
                }
            });
        }
    }

    public void showPicture(Activity activity, Uri uri, ImageView profilePictureImageView){
        Glide.with(activity).load(uri).centerCrop().placeholder(R.drawable.anonymous_mask).into(profilePictureImageView);
    }
}
