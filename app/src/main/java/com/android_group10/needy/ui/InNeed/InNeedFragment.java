package com.android_group10.needy.ui.InNeed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.ui.NeedsAndDeeds.NeedsAndDeedsViewModel;
import com.android_group10.needy.ui.NeedsAndDeeds.OtherStatusPostRecordFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InNeedFragment extends Fragment {
    private InNeedViewModel inNeedViewModel;
    private View root;
    public static ArrayList<Post> dataList = new ArrayList<>();
    private PostAdapter myPostAdapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ValueEventListener listListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    int count = 0;
                    if (snapshot.getChildrenCount() != count) {
                        dataList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Post object = child.getValue(Post.class);
                            assert object != null;
                            if (object.getPostStatus() == 1) {
                                object.setAuthorUID(String.valueOf(child.child("author").getValue()));
                                dataList.add(object);
                            }
                            count++;
                        }
                        myPostAdapter.notifyDataSetChanged();
                        if (dataList.size() != 0) {
                            textView.setText("");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.getReference().child("Posts").addValueEventListener(listListener);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inNeedViewModel =
                new ViewModelProvider(this).get(InNeedViewModel.class);
        root = inflater.inflate(R.layout.fragment_in_need, container, false);

        RecyclerView recycler = root.findViewById(R.id.postRecyclerView_in_need);
        textView = root.findViewById(R.id.text_default2);

        inNeedViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList>() {
            @Override
            public void onChanged(@Nullable ArrayList s) {
                recycler.setHasFixedSize(true);

                myPostAdapter = new PostAdapter(dataList, new PostAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Post clickedItem = dataList.get(position);
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.layout_in_need_fragment, new OpenPostRecordFragment(clickedItem));
                        fragmentTransaction.commit();
                    }
                });
                recycler.setAdapter(myPostAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                recycler.setItemAnimator(new DefaultItemAnimator());

                if (dataList.size() == 0) {
                    textView.setText(inNeedViewModel.getText().getValue());
                } else textView.setText("");
            }
        });
        return root;
    }
}