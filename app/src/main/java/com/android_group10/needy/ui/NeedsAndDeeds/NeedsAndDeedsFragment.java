package com.android_group10.needy.ui.NeedsAndDeeds;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class NeedsAndDeedsFragment extends Fragment {

    private NeedsAndDeedsViewModel needsAndDeedsViewModel;
    public static ArrayList<Post> dataList2 = new ArrayList<>();
    private PostAdapter myPostAdapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView textView;
    private RelativeLayout postLayout;

    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        needsAndDeedsViewModel =
                new ViewModelProvider(this).get(NeedsAndDeedsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_needs_and_deeds, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.postRecyclerView_needs_and_deeds);
        textView = root.findViewById(R.id.text_default);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myPostAdapter = new PostAdapter(requireContext(), dataList2, position -> {
            Post clickedItem = dataList2.get(position);

            NeedsAndDeedsFragmentDirections.ActionNeedsAndDeedsToPostStatus action = NeedsAndDeedsFragmentDirections.actionNeedsAndDeedsToPostStatus(clickedItem);
            Navigation.findNavController(root).navigate(action);
        });
        recyclerView.setAdapter(myPostAdapter);

        textView.setText(needsAndDeedsViewModel.getText().getValue());

        int statusNew = 1;
        int statusGone = 100;
        int statusComplete = 3;
        int statusRatedByVolunteer = 5;
        int statusRatedByAuthor = 4;

        ValueEventListener listListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    int count = 0;
                    if (snapshot.getChildrenCount() != count) {
                        dataList2.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Post object = child.getValue(Post.class);
                            assert object != null;
                            if ((object.getPostStatus() > statusNew) && (object.getPostStatus() < statusGone)) {
                                object.setAuthorUID(String.valueOf(child.child("author").getValue()));

                                if (object.getAuthorUID().equals(firebaseUser)) {
                                    if (object.getPostStatus() == statusRatedByVolunteer || object.getPostStatus() <= statusComplete) {
                                        dataList2.add(object);
                                    }
                                } else if (object.getVolunteer().equals(firebaseUser)) {
                                    if (object.getPostStatus() <= statusRatedByAuthor) {
                                        dataList2.add(object);
                                    }
                                }
                            }
                            count++;
                        }

                        if (dataList2.size() != 0) {
                            textView.setText("");
                        }

                        //Update recyclerview adapter
                        PostAdapter postAdapter = (PostAdapter) recyclerView.getAdapter();
                        if (postAdapter != null) {
                            postAdapter.updateData(dataList2);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.getReference().child("Posts").addValueEventListener(listListener);

        return root;
    }
}