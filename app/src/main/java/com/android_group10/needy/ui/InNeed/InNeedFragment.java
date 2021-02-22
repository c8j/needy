package com.android_group10.needy.ui.InNeed;


import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class InNeedFragment extends Fragment implements PostAdapter.OnItemClickListener {
    View root;
    ArrayList<Post> dataList = new ArrayList<>();
    PostAdapter myPostAdapter;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
 //   private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //   private InNeedViewModel inNeedViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPostAdapter = new PostAdapter(generateData(), this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //     inNeedViewModel =
        //             new ViewModelProvider(this).get(InNeedViewModel.class);


        root = inflater.inflate(R.layout.fragment_in_need, container, false);
        loadData();


      /*  final TextView textView = root.findViewById(R.id.text_in_need);
        inNeedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText("This is In need Fragment");

            }
        });*/
        return root;
    }



    private void loadData() {
        RecyclerView recycler = root.findViewById(R.id.postRecyclerView_in_need);
        recycler.setAdapter(myPostAdapter);
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
        Post p1 = new Post(firebaseAuth.getCurrentUser().getEmail(),"buy me food", ServiceType.SHOPPING.getValue(), "Kristianstad", "23001", "50 kr.");
        Post p2 = new Post(firebaseAuth.getCurrentUser().getEmail(), "clean my apartment", ServiceType.CLEANING.getValue(), "Kristianstad", "23951", "");
        Post p3 = new Post(firebaseAuth.getCurrentUser().getEmail(), "give me a break", ServiceType.OTHER.getValue(), "Malmö", "24834", "");
        Post p4 = new Post(firebaseAuth.getCurrentUser().getEmail(), "a ride to the hospital", ServiceType.TRANSPORTATION.getValue(), "Jävle", "20634", "cost of gas");

        dataList.add(p1);
        dataList.add(p2);
        dataList.add(p3);
        dataList.add(p4);

    //    DAO db_ = new DAO();
    //    db_.writeNewPost(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getEmail(), "buy me food", ServiceType.SHOPPING.getValue(), "Kristianstad", "23001", "50 kr.");
    //    db_.writeNewPost(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getEmail(), "clean my apartment", ServiceType.CLEANING.getValue(), "Kristianstad", "23951", "");
    //    db_.writeNewPost(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getEmail(), "give me a break", ServiceType.OTHER.getValue(), "Malmö", "24834", "");
    //    db_.writeNewPost(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getEmail(), "a ride to the hospital", ServiceType.TRANSPORTATION.getValue(), "Jävle", "20634", "cost of gas");

        return dataList;
    }
}