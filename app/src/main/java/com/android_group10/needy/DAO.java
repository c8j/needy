package com.android_group10.needy;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
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
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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

    //this will save a rating into the Ratings tree, calculate average and update a value of a User
    public void writeRating(UserRating rating){
        DatabaseReference currentUserRatingRef = db.getReference().child("Ratings").child(rating.getUserUID()).child(String.valueOf(rating.getRatingType()));
        String key = currentUserRatingRef.push().getKey();
        Map<String, Integer> ratingValue = rating.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Ratings/" + rating.getUserUID() + "/" + rating.getRatingType() + "/" + key, ratingValue);
        db.getReference().updateChildren(childUpdates);

        ValueEventListener postListener = new ValueEventListener() {
            double averageRating;
            @SuppressWarnings("unchecked")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                        int count = 1;
                        int sum = 0;

                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.hasChildren()) {
                                try {
                                    HashMap<String, Object> map = (HashMap<String, Object>) child.getValue();
                                    assert map != null;
                                    Object[] keys = map.keySet().toArray();
                                    // there will only be one child so we only take [0]
                                    Object value = Objects.requireNonNull(map.get(keys[0]));

                                    sum = sum + Integer.parseInt(value.toString());
                                    count = count + keys.length;

                                } catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        }

                        averageRating = (double) sum/(count-1);

                    DatabaseReference ratedUserRef = db.getReference("Users").child(rating.getUserUID());
                    ValueEventListener userListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listenerCode(ratedUserRef, snapshot, rating, averageRating);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    };

                    ratedUserRef.addValueEventListener(userListener);

                } else  {
                    Log.e("Rating FireBase DB table", "something went wrong and the rating hasn't been saved");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        currentUserRatingRef.addValueEventListener(postListener);
    }

    private void listenerCode(DatabaseReference currentRef, DataSnapshot snapshot, UserRating rating, double newValue) {
        int newInt = 1;
        if (newValue < 2.5 && newValue >= 1.5){
            newInt = 2;
        } else if (newValue <3.5){
            newInt = 3;
        } else if (newValue < 4.5){
            newInt = 4;
        } else if(newValue >= 4.5){
            newInt = 5;
        }

        User user = snapshot.getValue(User.class);
        if (user != null) {
            if (rating.getRatingType() == 1) {
                user.setAuthorRating(newInt );
                currentRef.child("authorRating").setValue(newInt);
            } else {
                user.setVolunteerRating(newInt);
                currentRef.child("volunteerRating").setValue(newInt);
            }
        }
    }
}
