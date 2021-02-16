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

    public void writeNewPost(String userId, String userEmail, String description, int serviceType, String city, String zipCode, String incentive) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously and at
        // /services/$serviceType simultaneously, etc..
        String key = db.getReference().child("posts").push().getKey();
        Post post = new Post(userEmail, description, serviceType, city, zipCode, incentive);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/active_posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        childUpdates.put("/services/" + serviceType + "/" + key, postValues);
        childUpdates.put("/zip-codes/" + zipCode + "/" + key, postValues);
        childUpdates.put("/cities/" + city + "/" + key, postValues);

        db.getReference().updateChildren(childUpdates);
    }
}
