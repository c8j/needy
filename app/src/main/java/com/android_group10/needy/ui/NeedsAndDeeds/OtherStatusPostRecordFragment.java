package com.android_group10.needy.ui.NeedsAndDeeds;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android_group10.needy.DAO;
import com.android_group10.needy.Post;
import com.android_group10.needy.ProfilePictureManager;
import com.android_group10.needy.R;
import com.android_group10.needy.Report;
import com.android_group10.needy.UserRating;
import com.android_group10.needy.messaging.util.FirestoreUtil;
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

public class OtherStatusPostRecordFragment extends Fragment {
    View root;
    private Post currentPositioned;
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
    private Boolean volunteer;

    private final int statusInProgress = 2;
    private final int statusComplete = 3;
    private final int statusRatedByAuthor = 4;
    private final int statusRatedByVolunteer = 5;
    private final int statusGone = 100;
    private int selectedOption;

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

        ProfilePictureManager ppManager = new ProfilePictureManager();

        //Get clickedPost from previous fragment
        if (getArguments() != null) {
            OtherStatusPostRecordFragmentArgs args = OtherStatusPostRecordFragmentArgs.fromBundle(getArguments());
            currentPositioned = args.getClickedPost();

        }

        if (currentPositioned != null) {
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
            ppManager.displayProfilePic(getActivity(), authorPicture, false, authorUID);


            if (authorUID.equals(currentUser)) {
                volunteer = false;
                contact.setText(R.string.button_contact2);
                report.setText(R.string.button_report2);
                authorPhone.setVisibility(View.INVISIBLE);
                textPhone.setVisibility(View.INVISIBLE);
                rate.setText(R.string.button_rate2);
                if (currentPositioned.getPostStatus() >= statusComplete) {
                    if (currentPositioned.getPostStatus() == statusRatedByVolunteer || currentPositioned.getPostStatus() == statusComplete) {
                        rate.setVisibility(View.VISIBLE);
                    } else {
                        rate.setVisibility(View.INVISIBLE);
                    }
                }

            } else if (currentPositioned.getVolunteer().equals(currentUser)) {
                volunteer = true;
                contact.setText(R.string.button_contact);
                report.setText(R.string.button_report1);
                authorPhone.setVisibility(View.VISIBLE);
                textPhone.setVisibility(View.VISIBLE);
                rate.setText(R.string.button_rate1);
                if (currentPositioned.getPostStatus() >= statusComplete) {
                    if (currentPositioned.getPostStatus() == statusRatedByAuthor || currentPositioned.getPostStatus() == statusComplete) {
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

            report.setOnClickListener(v -> {
                String blamedUserUID;
                if (authorUID.equals(currentUser)) {
                    blamedUserUID = currentPositioned.getVolunteer();
                } else {
                    blamedUserUID = currentPositioned.getAuthorUID();
                }

                LayoutInflater dialogInflater = LayoutInflater.from(getContext());
                final View yourCustomView = dialogInflater.inflate(R.layout.report_user, null);

                final TextView etName = (EditText) yourCustomView.findViewById(R.id.report_description);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Report")
                        .setView(yourCustomView)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Report complaint = new Report(key, currentUser, blamedUserUID, etName.getText().toString());
                                DAO saveDB = new DAO();
                                saveDB.writeReport(complaint);
                            }
                        })
                        .setNegativeButton("Cancel", null).create();
                dialog.show();
            });
        }

        return root;
    }

    private void listenerCode(DatabaseReference currentRef, DataSnapshot snapshot) {
        Post post = snapshot.getValue(Post.class);
        if (post != null) {

            if (currentPositioned.getPostStatus() == statusInProgress) {
                completePost.setVisibility(View.VISIBLE);
                rate.setVisibility(View.INVISIBLE);

                completePost.setOnClickListener(v -> {
                    postStatusUpdate(currentRef, post, statusComplete);
                    completePost.setVisibility(View.INVISIBLE);
                    contact.setVisibility(View.INVISIBLE);
                    //Close associated conversation
                    FirestoreUtil.concludeConversationsForPost(currentPositioned);
                    rate.setVisibility(View.VISIBLE);
                });

                contact.setOnClickListener(v ->
                        //Create conversation request associated with this post
                        FirestoreUtil.createRequest(
                                currentPositioned,
                                volunteer,
                                (wasSuccessful, message) ->
                                        Toast.makeText(getContext(),
                                                message,
                                                Toast.LENGTH_SHORT
                                        ).show()
                        )
                );
            } else {
                completePost.setVisibility(View.INVISIBLE);
                contact.setVisibility(View.INVISIBLE);

                rate.setOnClickListener(v -> {
                    String ratedUserUID;
                    int ratingType;
                    if (authorUID.equals(currentUser)) {
                        ratedUserUID = currentPositioned.getVolunteer();
                        ratingType = 2;
                    } else {
                        ratedUserUID = currentPositioned.getAuthorUID();
                        ratingType = 1;
                    }

                    LayoutInflater dialogInflater = LayoutInflater.from(getContext());
                    final View rateView = dialogInflater.inflate(R.layout.rate_user, null);

                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                    dialog2.setTitle("Rate user");

                    rateView.setBackgroundColor(getResources().getColor(R.color.light_yello_background));
                    dialog2.setView(rateView).create();

                    String[] items = getResources().getStringArray(R.array.radio);

                    dialog2.setSingleChoiceItems(items, 5, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int option) {
                            int usersChoice = 5;
                            switch (option) {
                                case 0:
                                    usersChoice = 1;
                                    break;
                                case 1:
                                    usersChoice = 2;
                                    break;
                                case 2:
                                    usersChoice = 3;
                                    break;
                                case 3:
                                    usersChoice = 4;
                                    break;
                                case 4:
                                    usersChoice = 5;
                                    break;
                            }
                            selectedOption = usersChoice;
                        }
                    });

                    dialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            if (selectedOption != 0) {
                                if (authorUID.equals(currentUser) && (post.getPostStatus() == statusComplete || post.getPostStatus() == statusRatedByVolunteer)) {
                                    postStatusUpdate(currentRef, post, statusRatedByAuthor);
                                    rate.setVisibility(View.INVISIBLE);
                                } else if (currentPositioned.getVolunteer().equals(currentUser) && (post.getPostStatus() == statusComplete || post.getPostStatus() == statusRatedByAuthor)) {
                                    postStatusUpdate(currentRef, post, statusRatedByVolunteer);
                                    rate.setVisibility(View.INVISIBLE);
                                }
                                UserRating rating = new UserRating(ratedUserUID, ratingType, selectedOption);
                                DAO saveDB = new DAO();
                                saveDB.writeRating(rating);
                            } else {
                                Toast.makeText(getContext(), "You must select one option!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog2.setNegativeButton("Cancel", null).create();
                    dialog2.create().show();
                });
            }
        }
    }

    private void postStatusUpdate(DatabaseReference currentRef, Post post, int newStatus) {
        assert post != null;
        if ((post.getPostStatus() == statusRatedByAuthor && newStatus == statusRatedByVolunteer) || (post.getPostStatus() == statusRatedByVolunteer && newStatus == statusRatedByAuthor)) {
            currentPositioned.setPostStatus(statusGone);
            post.setPostStatus(statusGone);
            currentRef.child("postStatus").setValue(statusGone);
        } else {
            currentPositioned.setPostStatus(newStatus);
            post.setPostStatus(newStatus);
            currentRef.child("postStatus").setValue(newStatus);
        }

    }
}
