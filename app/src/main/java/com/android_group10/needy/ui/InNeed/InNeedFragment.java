package com.android_group10.needy.ui.InNeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.ServiceType;
import com.android_group10.needy.User;

import java.util.ArrayList;
import java.util.List;

public class InNeedFragment extends Fragment {
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // InNeedViewModel inNeedViewModel = new ViewModelProvider(this).get(InNeedViewModel.class);
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

    private void loadData() {
        RecyclerView recycler = root.findViewById(R.id.postRecyclerView_in_need);
        PostAdapter myPostAdapter = new PostAdapter(generateData());
        recycler.setAdapter(myPostAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
    }
}