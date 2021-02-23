package com.android_group10.needy.ui.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android_group10.needy.ImageHelper;
import com.android_group10.needy.R;
import com.android_group10.needy.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.android_group10.needy.ImageHelper.decodeSampledBitmapFromPath;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //.child(uid)
                for (DataSnapshot ds : snapshot.getChildren()){
                }
                firstNameText.setText(snapshot.child("firstName").getValue(String.class));
                lastNameText.setText(snapshot.child("lastName").getValue(String.class));
                int zipCode = snapshot.child("zipCode").getValue(Integer.class);
                zipcodeText.setText(Integer.toString(zipCode));
                emailIdText.setText(snapshot.child("email").getValue(String.class));
                phNumText.setText(snapshot.child("phone").getValue(String.class));
                cityText.setText(snapshot.child("city").getValue(String.class));
                int profilePic = snapshot.child("image").getValue(Integer.class);
                if(profilePic != 0){
                    decodeSampledBitmapFromPath("", profilePicture.getWidth(), profilePicture.getHeight());
                    //profilePicture.setImageDrawable(profilePic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.i(TAG, "User name: " + user.getDisplayName());
            firstNameText.setText(user.getDisplayName());
            emailIdText.setText(user.getEmail());
            phNumText.setText(user.getPhoneNumber());
            //zipcodeText.setText(user.);
        }
        else {
            Toast.makeText(this.getContext(), "Could not find profile.", Toast.LENGTH_SHORT).show();
        }
         */

        return view;
    }
}
