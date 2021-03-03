package com.android_group10.needy;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
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
        String key = db.getReference().child("Ratings").child(rating.getUserUID()).child(String.valueOf(rating.getRatingType())).push().getKey();
        Map<String, Object> ratingValue = rating.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
      //  childUpdates.put("/Reports/" + rating.getBlamedUserUID() + "/" + key, postValues);
        db.getReference().updateChildren(childUpdates);
    }
}
