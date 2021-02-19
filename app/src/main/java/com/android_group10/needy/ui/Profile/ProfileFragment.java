package com.android_group10.needy.ui.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android_group10.needy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView nameText = view.findViewById(R.id.profileNameTextView);
        TextView addressText = view.findViewById(R.id.profileAddressTextView);
        TextView zipcodeText = view.findViewById(R.id.profileZipcodeTextView);
        TextView phNumText = view.findViewById(R.id.profilePhoneNumTextView);
        TextView emailIdText = view.findViewById(R.id.profileEmailTextView);
        ImageView profilePicture = view.findViewById(R.id.profilePicimageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.i(TAG, "User name: " + user.getDisplayName());
            nameText.setText(user.getDisplayName());
            emailIdText.setText(user.getEmail());
            phNumText.setText(user.getPhoneNumber());
            //zipcodeText.setText(user.);
        }
        else {
            Toast.makeText(this.getContext(), "Could not find profile.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
