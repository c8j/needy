package com.android_group10.needy.ui.InNeed;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import com.android_group10.needy.DAO;
import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.ServiceType;
import com.android_group10.needy.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseCommonRegistrar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class InNeedFragment extends Fragment implements PostAdapter.OnItemClickListener {
    private View root;
    private static ArrayList<Post> dataList = new ArrayList<>();
    private PostAdapter myPostAdapter;
 //   private final String firebaseAuthUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
 //   private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ValueEventListener listListener = new ValueEventListener(){
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.getReference().child("Posts").addValueEventListener(listListener);
        myPostAdapter = new PostAdapter(dataList, this);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_in_need, container, false);

        loadData();

        return root;
    }

    private void loadData() {
        RecyclerView recycler = root.findViewById(R.id.postRecyclerView_in_need);
        recycler.setAdapter(myPostAdapter);
       // myPostAdapter = new PostAdapter(dataList, this);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onItemClick(int position) {
        Post clickedItem = dataList.get(position);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layout_in_need_fragment, new OpenPostRecordFragment(clickedItem));  // CRASH on clicking 'back'. getID() makes app crash at start
        fragmentTransaction.commit();
    }

    private ArrayList<Post> generateData() {

      /*  Post p1 = new Post(firebaseAuthUser, 1,"buy me food", ServiceType.SHOPPING.getValue(), "Kristianstad", "23001", "50 kr.");
        p1.setPostUID("-MU8QXfc6KgbEMvOBUcK");
        Post p2 = new Post(firebaseAuthUser, 1, "clean my apartment", ServiceType.CLEANING.getValue(), "Kristianstad", "23951", "");
        p2.setPostUID("-MU8QXg06poekGoCQUQI");
        Post p3 = new Post(firebaseAuthUser, 1,"give me a break", ServiceType.OTHER.getValue(), "Malmö", "24834", "");
        p2.setPostUID("-MU8QXg5rzrp9m43iPrq");
        Post p4 = new Post(firebaseAuthUser, 1,"a ride to the hospital", ServiceType.TRANSPORTATION.getValue(), "Jävle", "20634", "cost of gas");
        p4.setPostUID("-MU8QXgAGjcKxr7Cw54G");*/

    /*    dataList.add(p1);
        dataList.add(p2);
        dataList.add(p3);
        dataList.add(p4);*/

     /*   DAO db_ = new DAO();
        db_.writeNewPost(p1);
        db_.writeNewPost(p2);
        db_.writeNewPost(p3);
        db_.writeNewPost(p4);*/
        return dataList;
    }
}