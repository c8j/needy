package com.android_group10.needy.ui.InNeed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android_group10.needy.Post;
import com.android_group10.needy.ProfilePictureManager;
import com.android_group10.needy.R;
import com.android_group10.needy.messaging.util.FirestoreUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class OpenPostRecordFragment extends Fragment {
    View root;
    private Post currentPositioned;
    private ImageView authorPicture;
    private TextView authorName;
    private TextView postDescription;
    private TextView postZipCode;
    private TextView postCity;
    private TextView authorPhone;
    private TextView postIncentive;
    private TextView textPhone;
    private Button acceptPost;
    private Button contactAuthor;
    private RatingBar authorRatingStars;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.open_post, container, false);

        authorPicture = root.findViewById(R.id.author_image);
        authorName = root.findViewById(R.id.author_name);
        authorPhone = root.findViewById(R.id.author_phone);
        postDescription = root.findViewById(R.id.post_description);
        postZipCode = root.findViewById(R.id.post_zip);
        postCity = root.findViewById(R.id.post_city);
        postIncentive = root.findViewById(R.id.post_incentive);
        acceptPost = root.findViewById(R.id.accept);
        contactAuthor = root.findViewById(R.id.contact_author);
        textPhone = root.findViewById(R.id.text_phone);
        authorRatingStars = root.findViewById(R.id.authorRatingBar2);

        ProfilePictureManager ppManager = new ProfilePictureManager();

        //Get clickedPost from previous fragment
        if (getArguments() != null) {
            OpenPostRecordFragmentArgs args = OpenPostRecordFragmentArgs.fromBundle(getArguments());
            currentPositioned = args.getClickedPost();
        }

        String key = null;
        String authorUID = null;

        if (currentPositioned != null) {
            key = currentPositioned.getPostUID();
            authorUID = currentPositioned.getAuthorUID();
            if (!currentPositioned.getDescription().isEmpty()) {
                postDescription.setText(currentPositioned.getDescription());
            }
            if (!currentPositioned.getIncentive().isEmpty()) {
                postIncentive.setText(currentPositioned.getIncentive());
            } else postIncentive.setText("-");

            if (!currentPositioned.getZipCode().isEmpty()) {
                postZipCode.setText(currentPositioned.getZipCode());
            }
            if (!currentPositioned.getCity().isEmpty()) {
                postCity.setText(currentPositioned.getCity());
            }
            ppManager.displayProfilePic(getActivity(), authorPicture, false, authorUID);
        }

        assert authorUID != null;
        String finalAuthorUID = authorUID;
        db.getReference().child("Users").child(authorUID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    HashMap<String, Object> authorObject = (HashMap<String, Object>) task.getResult().getValue();

                    assert authorObject != null;
                    if (authorObject.get("firstName") != null || authorObject.get("lastName") != null) {
                        String fullName = String.valueOf(authorObject.get("firstName")).concat(" ").concat(String.valueOf(authorObject.get("lastName")));
                        authorName.setText(fullName);
                    }
                    if (authorObject.get("authorRating") != null) {
                        String authRating = String.format(Locale.getDefault(), "%s", authorObject.get("authorRating"));
                        authorRatingStars.setRating(Float.parseFloat(authRating));
                    }
                    if (authorObject.get("phone") != null) {
                        authorPhone.setText(String.valueOf(authorObject.get("phone")));
                    }
                }
            }
        });

        DatabaseReference currentPostRef = db.getReference("Posts").child(key);
        ValueEventListener postListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listenerCode(currentPostRef, snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        currentPostRef.addValueEventListener(postListener1);

        return root;
    }

    private void listenerCode(DatabaseReference currentRef, DataSnapshot snapshot) {
        Post post = snapshot.getValue(Post.class);
        if (post != null && currentPositioned != null) {
            if (!currentPositioned.getAuthorUID().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())) {
                acceptPost.setOnClickListener(v -> {
                    textPhone.setVisibility(View.VISIBLE);
                    authorPhone.setVisibility(View.VISIBLE);
                    currentPositioned.setPostStatus(2);
                    post.setPostStatus(2);
                    currentRef.child("postStatus").setValue(2);
                    currentRef.child("volunteer").setValue(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                    acceptPost.setVisibility(View.INVISIBLE);
                });

                //Create conversation request associated with this post
                CircularProgressIndicator progressIndicator = root.findViewById(R.id.cpiInNeedRequest);
                contactAuthor.setOnClickListener(v -> {
                            progressIndicator.show();
                            FirestoreUtil.createRequest(
                                    currentPositioned,
                                    true,
                                    (wasSuccessful, message) -> {
                                        progressIndicator.hide();
                                        Toast.makeText(
                                                getContext(),
                                                message,
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                            );
                        }
                );
            } else {
                acceptPost.setVisibility(View.INVISIBLE);
                contactAuthor.setVisibility(View.INVISIBLE);
            }
        }
    }
}
