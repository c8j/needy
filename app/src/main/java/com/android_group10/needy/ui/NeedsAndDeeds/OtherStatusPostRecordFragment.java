package com.android_group10.needy.ui.NeedsAndDeeds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android_group10.needy.Post;
import com.android_group10.needy.R;
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
import java.util.Objects;

public class OtherStatusPostRecordFragment extends Fragment {
    View root;
    private final Post currentPositioned;
    private ImageView authorPicture;
    private TextView authorName;
    private TextView authorRating;
    private TextView postDescription;
    private TextView postZipCode;
    private TextView postCity;
    private TextView authorPhone;
    private TextView postIncentive;
    private TextView textPhone;
    private Button completePost;
    private Button contact;
    private Button report;
    private Button rate;

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String authorUID;
 //   private int REQ_CODE = 12345;

    public OtherStatusPostRecordFragment(Post currentPositioned) {
        this.currentPositioned = currentPositioned;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.open_post_status2, container, false);

        authorPicture = root.findViewById(R.id.author_image2);
        authorName = root.findViewById(R.id.author_name2);
        authorRating = root.findViewById(R.id.author_rating2);
        authorPhone = root.findViewById(R.id.author_phone2);
        postDescription = root.findViewById(R.id.post_description2);
        postZipCode = root.findViewById(R.id.post_zip2);
        postCity = root.findViewById(R.id.post_city2);
        postIncentive = root.findViewById(R.id.post_incentive2);
        completePost = root.findViewById(R.id.complete);
        contact = root.findViewById(R.id.contact_2);
        report = root.findViewById(R.id.report);
        rate = root.findViewById(R.id.rate);
        textPhone = root.findViewById(R.id.text_phone2);

        String key = currentPositioned.getPostUID();
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


        if (authorUID.equals(currentUser)){
            contact.setText(R.string.button_contact2);
            report.setText(R.string.button_report2);
            authorPhone.setVisibility(View.INVISIBLE);
            textPhone.setVisibility(View.INVISIBLE);
            rate.setText(R.string.button_rate2);
            if (currentPositioned.getPostStatus() >= 3) {
                if (currentPositioned.getPostStatus() == 5 || currentPositioned.getPostStatus() == 3)
                {
                    rate.setVisibility(View.VISIBLE);
                } else {
                    rate.setVisibility(View.INVISIBLE);
                }
            }

        } else if (currentPositioned.getVolunteer().equals(currentUser)){
            contact.setText(R.string.button_contact);
            report.setText(R.string.button_report1);
            authorPhone.setVisibility(View.VISIBLE);
            textPhone.setVisibility(View.VISIBLE);
            rate.setText(R.string.button_rate1);
            if (currentPositioned.getPostStatus() >= 3) {
                if (currentPositioned.getPostStatus() == 4 || currentPositioned.getPostStatus() == 3)
                {
                    rate.setVisibility(View.VISIBLE);
                } else {
                    rate.setVisibility(View.INVISIBLE);
                }
            }
        }


        assert authorUID != null;
        db.getReference().child("Users").child(authorUID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    HashMap<String, Object> authorObject = (HashMap<String, Object>) task.getResult().getValue();
                    //authorPicture.setImageURI(); ;
                    assert authorObject != null;
                    if (authorObject.get("firstName") != null || authorObject.get("lastName") != null) {
                        String fullName = String.valueOf(authorObject.get("firstName")).concat(" ").concat(String.valueOf(authorObject.get("lastName")));
                        authorName.setText(fullName);
                    }
                    if (authorObject.get("authorRating") != null) {
                        authorRating.setText(String.format(Locale.getDefault(), "%s", authorObject.get("authorRating")));
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

      /*  report.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("reportAuthor", currentUser);
            intent.putExtra("postUID", currentPositioned.getPostUID());
            if (authorUID.equals(currentUser)){
                intent.putExtra("blamedUser", currentPositioned.getVolunteer());
            } else{
                intent.putExtra("blamedUser", currentPositioned.getAuthorUID());
            }

            try{
                startActivityForResult(intent, REQ_CODE);
            } catch (Exception ex){
                ex.printStackTrace();
            }


        });

        */

        return root;
    }

    private void listenerCode(DatabaseReference currentRef, DataSnapshot snapshot) {
        Post post = snapshot.getValue(Post.class);
        if (post != null) {

            if(currentPositioned.getPostStatus() == 2){
                completePost.setVisibility(View.VISIBLE);
                rate.setVisibility(View.INVISIBLE);

                completePost.setOnClickListener(v -> {
                    postStatusUpdate(currentRef, post, 3);
                    completePost.setVisibility(View.INVISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    rate.setVisibility(View.VISIBLE);
                });
                contact.setOnClickListener(v -> Toast.makeText(getContext(), "send a message", Toast.LENGTH_SHORT).show());
            } else {
                completePost.setVisibility(View.INVISIBLE);
                contact.setVisibility(View.INVISIBLE);

                rate.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "open a window to select rate and submit", Toast.LENGTH_SHORT).show();
                    if(authorUID.equals(currentUser) && (post.getPostStatus() == 3 || post.getPostStatus() == 5)) {
                        Toast.makeText(getContext(), "you are author and status 3 or 5", Toast.LENGTH_SHORT).show();
                        postStatusUpdate(currentRef, post, 4);
                        rate.setVisibility(View.INVISIBLE);
                    } else if (currentPositioned.getVolunteer().equals(currentUser) && (post.getPostStatus() == 3 || post.getPostStatus() == 4)){
                        Toast.makeText(getContext(), "you are volunteer and status 3 or 4", Toast.LENGTH_SHORT).show();
                        postStatusUpdate(currentRef, post, 5);
                        rate.setVisibility(View.INVISIBLE);
                    }
                });
            }


        }

    }

    private void postStatusUpdate(DatabaseReference currentRef, Post post, int newStatus){
        currentPositioned.setPostStatus(newStatus);
        assert post != null;
        post.setPostStatus(newStatus);
        currentRef.child("postStatus").setValue(newStatus);
    }
}
