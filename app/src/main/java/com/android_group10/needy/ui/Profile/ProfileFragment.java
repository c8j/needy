package com.android_group10.needy.ui.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android_group10.needy.ImageHelper;
import com.android_group10.needy.R;
import com.android_group10.needy.User;
import com.android_group10.needy.ui.InNeed.InNeedFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.android_group10.needy.ImageHelper.decodeSampledBitmapFromPath;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    final User[] currentUser = new User[1];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView firstNameText = view.findViewById(R.id.profileFirstNameTextView);
        TextView lastNameText = view.findViewById(R.id.profileLastNameTextView);
        TextView cityText = view.findViewById(R.id.profileCityTextView);
        TextView zipcodeText = view.findViewById(R.id.profileZipcodeTextView);
        TextView phNumText = view.findViewById(R.id.profilePhoneNumTextView);
        TextView emailIdText = view.findViewById(R.id.profileEmailTextView);
        ImageView profilePicture = view.findViewById(R.id.profilePicimageView);
        Button editButton = view.findViewById(R.id.profileEditButton);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser[0] = snapshot.getValue(User.class);
                //firstNameText.setText(currentUser[0].getFirstName());

                firstNameText.setText(snapshot.child("firstName").getValue(String.class));
                lastNameText.setText(snapshot.child("lastName").getValue(String.class));
                int zipCode = snapshot.child("zipCode").getValue(Integer.class);
                zipcodeText.setText(Integer.toString(zipCode));
                emailIdText.setText(snapshot.child("email").getValue(String.class));
                phNumText.setText(snapshot.child("phone").getValue(String.class));
                cityText.setText(snapshot.child("city").getValue(String.class));
                Uri profilePic = currentUser[0].getImgUri();
                if(profilePic != null){
                    //decodeSampledBitmapFromPath("", profilePicture.getWidth(), profilePicture.getHeight());
                    //profilePicture.setImageURI(profilePic);
                    showPicture(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Could not find profile.", Toast.LENGTH_SHORT).show();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void showPicture(ImageView imgView){
        Glide.with(this).load(currentUser[0].getImgUri()).centerCrop().placeholder(R.drawable.anonymous_mask).into(imgView);
    }
}
