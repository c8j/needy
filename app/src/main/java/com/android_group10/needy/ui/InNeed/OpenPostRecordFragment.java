package com.android_group10.needy.ui.InNeed;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OpenPostRecordFragment extends Fragment{
        View root;
        Post currentPositioned;
        private ImageView authorPicture;
        private TextView authorName;
        private TextView authorRating;
        private TextView postDescription;
        private TextView postZipCode;
        private TextView postCity;
        private TextView authorPhone;
        private TextView postIncentive;
        private TextView textPhone;
        private Button acceptPost;
        private Button contactAuthor;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DataSnapshot snapshot;

        public OpenPostRecordFragment(Post currentPositioned){
            this.currentPositioned = currentPositioned;
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            root = inflater.inflate(R.layout.open_post, container, false);

            authorPicture = root.findViewById(R.id.author_image);
            authorName = root.findViewById(R.id.author_name);
            authorRating = root.findViewById(R.id.author_rating);
            authorPhone = root.findViewById(R.id.author_phone);
            postDescription = root.findViewById(R.id.post_description);
            postZipCode = root.findViewById(R.id.post_zip);
            postCity = root.findViewById(R.id.post_city);
            postIncentive = root.findViewById(R.id.post_incentive);
            acceptPost = root.findViewById(R.id.accept);
            contactAuthor = root.findViewById(R.id.contact_author);
            textPhone = root.findViewById(R.id.text_phone);


            acceptPost.setOnClickListener(v -> {
                Toast.makeText(getContext(), "change Post status to 2, remove from the list of active", Toast.LENGTH_SHORT).show();
                textPhone.setVisibility(View.VISIBLE);
                authorPhone.setVisibility(View.VISIBLE);
               // authorPhone.setText(currentPositioned.getUserEmail().getPhoneNumber());
                currentPositioned.setPostStatus(2);
                acceptPost.setVisibility(View.INVISIBLE);
                //currentPositioned.setVolunteer(current logged in user);
            });
            contactAuthor.setOnClickListener(v ->Toast.makeText(getContext(), "send request to chat to the author", Toast.LENGTH_SHORT).show());

            if (currentPositioned.getUserEmail() != null) {
                authorName.setText(currentPositioned.getUserEmail());
            }
     /*       if (currentPositioned.getUser().getDisplayName() != null) {
                authorName.setText(currentPositioned.getUser().getDisplayName());
            }
            if (currentPositioned.getUser().getPhotoUrl() != null) {
                authorPicture.setImageURI(currentPositioned.getUser().getPhotoUrl());
            }*/
     /*       if(currentPositioned.getUser().getAuthorRating() !=0) {
                authorRating.setText(String.format("%.1f", currentPositioned.getUser().getAuthorRating()));
            }*/
            if(!currentPositioned.getDescription().isEmpty()) {
                postDescription.setText(currentPositioned.getDescription());
            }
            if(!currentPositioned.getIncentive().isEmpty()) {
                postIncentive.setText(currentPositioned.getIncentive());
            } else postIncentive.setText("-");
            if(!currentPositioned.getZipCode().isEmpty()) {
                postZipCode.setText(currentPositioned.getZipCode());
            }
            if(!currentPositioned.getCity().isEmpty()) {
                postCity.setText(currentPositioned.getCity());
            }

            return root;
        }
}
