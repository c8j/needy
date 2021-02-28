package com.android_group10.needy.ui.InNeed;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.android_group10.needy.DAO;
import com.android_group10.needy.Post;
import com.android_group10.needy.R;
import com.android_group10.needy.ServiceType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Objects;

public class AddPostRecordFragment extends Fragment{

        View root;
        private Spinner spinner;
        private EditText zipCode;
        private EditText city;
        private EditText description;
        private EditText incentive;
        private TextView hiddenText;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        private final FirebaseDatabase db = FirebaseDatabase.getInstance();

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            root = inflater.inflate(R.layout.add_post, container, false);

            spinner = root.findViewById(R.id.spinner);
            zipCode = root.findViewById(R.id.post_zip1);
            city = root.findViewById(R.id.post_city1);
            description = root.findViewById(R.id.post_description1);
            incentive = root.findViewById(R.id.post_incentive1);
            hiddenText = root.findViewById(R.id.hidden_textView);
            Button savePost = root.findViewById(R.id.save1);


            String[] items = {ServiceType.WALK_A_DOG.toString(), ServiceType.SHOPPING.toString(), ServiceType.TRANSPORTATION.toString(), ServiceType.CLEANING.toString(), ServiceType.OTHER.toString()};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    hiddenText.setText(String.valueOf(parent.getItemIdAtPosition(position)));
                    Log.v("item id", String.valueOf(parent.getItemIdAtPosition(position)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });



            description.setOnFocusChangeListener((v, hasFocus) -> {
                hideKeyboard(v, description, hasFocus);
            });

            zipCode.setOnFocusChangeListener((v, hasFocus) -> {
                hideKeyboard(v, zipCode, hasFocus);
            });

            city.setOnFocusChangeListener((v, hasFocus) -> {
                hideKeyboard(v, city, hasFocus);
            });

            incentive.setOnFocusChangeListener((v, hasFocus) -> {
                hideKeyboard(v, incentive, hasFocus);
            });


            String authorUID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

            savePost.setOnClickListener(v -> {
                String descr = description.getText().toString();
                if (!descr.equals("")) {
                    String cityStr = city.getText().toString();
                    String zipStr = zipCode.getText().toString();
                    if (cityStr.equals("") || zipStr.equals("")){
                        db.getReference().child("Users").child(authorUID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                String c = cityStr;
                                String z = zipStr;
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else {
                                    @SuppressWarnings("unchecked") HashMap<String, Object> authorObject = (HashMap<String, Object>) Objects.requireNonNull(task.getResult()).getValue();
                                    assert authorObject != null;
                                    if (c.equals("")) {
                                        c = Objects.requireNonNull(authorObject.get("city")).toString();
                                    }
                                    if (z.equals("")) {
                                        z = Objects.requireNonNull(authorObject.get("zipCode")).toString();
                                    }
                                }
                                DAO dataStore = new DAO();
                                dataStore.writeNewPost(new Post(authorUID, 1, descr, Integer.parseInt(hiddenText.getText().toString()), c, z, incentive.getText().toString()));
                                requireActivity().onBackPressed();
                            }
                        });
                    } else {
                        DAO dataStore = new DAO();
                        dataStore.writeNewPost(new Post(authorUID, 1, descr, Integer.parseInt(hiddenText.getText().toString()), cityStr, zipStr, incentive.getText().toString()));
                        requireActivity().onBackPressed();
                    }
                } else {
                    Toast.makeText(getContext(), "Description field cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            });

            return root;
        }

        // this will make sure Soft Keyboard disappears
        private void hideKeyboard(View v, EditText field, boolean hasFocus){
            if (v == field) {
                if (hasFocus) {
                    // Open keyboard
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(field, InputMethodManager.SHOW_FORCED);
                } else {
                    // Close keyboard
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(field.getWindowToken(), 0);
                }
            }
        }
}
