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
        childUpdates.put("/User-posts/" + post.getAuthorUID() + "/" + key, postValues);
        childUpdates.put("/Services/" + post.getServiceType() + "/" + key, postValues);
        childUpdates.put("/Zip-codes/" + post.getZipCode() + "/" + key, postValues);
        childUpdates.put("/Cities/" + post.getCity() + "/" + key, postValues);

        db.getReference().updateChildren(childUpdates);
    }
}
