package com.android_group10.needy;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DAO {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DataSnapshot snapshot;

    public void writeNewPost(Post post) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously and at
        // /services/$serviceType simultaneously, etc..
        String key = db.getReference().child("Posts").push().getKey();

        Map<String, Object> postValues = post.toMap();
        postValues.put("postUID", key);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Posts/" + key, postValues);
        //  temporarily commented
  //      childUpdates.put("/User-posts/" + post.getAuthorUID() + "/" + key, postValues);
  //      childUpdates.put("/Services/" + post.getServiceType() + "/" + key, postValues);
  //      childUpdates.put("/Zip-codes/" + post.getZipCode() + "/" + key, postValues);
  //      childUpdates.put("/Cities/" + post.getCity() + "/" + key, postValues);

        db.getReference().updateChildren(childUpdates);
    }

    public void writeReport(Report report){
        String key = db.getReference().child("Reports").child(report.getBlamedUserUID()).push().getKey();
        Map<String, Object> postValues = report.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Reports/" + report.getBlamedUserUID() + "/" + key, postValues);
        db.getReference().updateChildren(childUpdates);
    }

    public void writeRating(UserRating rating){
        DatabaseReference currentUserRatingRef = db.getReference().child("Ratings").child(rating.getUserUID()).child(String.valueOf(rating.getRatingType()));
        String key = currentUserRatingRef.push().getKey();
        Map<String, Object> ratingValue = rating.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Ratings/" + rating.getUserUID() + "/" + rating.getRatingType() + "/" + key, ratingValue);
        Log.e("rating recorded", "record saved " + rating);
        db.getReference().updateChildren(childUpdates);

     //   DatabaseReference currentPostRef = db.getReference().child("Ratings").child(rating.getUserUID()).child(String.valueOf(rating.getRatingType()));
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              //  int[] array;
                if (snapshot != null && snapshot.hasChildren()){
                        int count = 1;
                        int sum = 0;
                        Log.e("inside datasnapshot", "IN");
                        for (DataSnapshot child : snapshot.getChildren()) {
                            sum = sum + child.getValue(UserRating.class).getRatingValue();
                            count++;
                        }
                        Log.e("count", String.valueOf(sum));

                } else  {
                    Log.e("outside datasnapshot", "NOT IN");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        currentUserRatingRef.addValueEventListener(postListener);
    }
}
