package com.android_group10.needy.ui.InNeed;


import android.app.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.ServiceType;
import com.android_group10.needy.User;
import com.android_group10.needy.ui.ToDo.ToDoFragment;


import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class InNeedFragment extends Fragment implements PostAdapter.OnItemClickListener {
    View root;

    ArrayList<Post> dataList = new ArrayList<>();
    //   private InNeedViewModel inNeedViewModel;

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

/*
    private List<Post> generateData() {
        ArrayList<Post> dataList = new ArrayList<>();
        User one = new User("blah-blah@mail.com", "Liliia", "Liliia", "Liliia", "07653248601", "Osby", 29133);
        one.setImage(R.mipmap.ic_launcher);
        dataList.add(new Post(1, one, "buy me food", ServiceType.SHOPPING, "Kristianstad", "23001"));
        dataList.add(new Post(2, one, "clean my apartment", ServiceType.CLEANING, "Kristianstad", "23951"));
        dataList.add(new Post(3, one, "give me a break", ServiceType.OTHER, "Malmö", "24834"));
        dataList.add(new Post(3, one, "a ride to the hospital", ServiceType.TRANSPORTATION, "Jävle", "20634"));
        return dataList;
    }
*/

    private void loadData() {

        RecyclerView recycler = root.findViewById(R.id.postRecyclerView_in_need);
        PostAdapter myPostAdapter = new PostAdapter(generateData(), this);
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
        //User one = new User("Liliia", "07653248601", "blah-blah@mail.com");
        User one = new User("blah-blah@mail.com", "pppppp", "Liliia", "Ana", "07653248601", "Osby", 2911);
        one.setImage(R.mipmap.ic_launcher);
        one.setVolunteerRating(3);

        dataList.add(new Post(one, "buy me food", ServiceType.SHOPPING, "Kristianstad", "23001", "50 kr."));
        dataList.add(new Post(one, "clean my apartment", ServiceType.CLEANING, "Kristianstad", "23951", ""));
        dataList.add(new Post(one, "give me a break", ServiceType.OTHER, "Malmö", "24834", ""));
        dataList.add(new Post(one, "a ride to the hospital", ServiceType.TRANSPORTATION, "Jävle", "20634", "cost of gas"));
        return dataList;
    }

}