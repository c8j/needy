package com.android_group10.needy.ui.NeedsAndDeeds;

import android.os.Bundle;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.ui.InNeed.OpenPostRecordFragment;

import java.util.ArrayList;

public class NeedsAndDeedsFragment extends Fragment implements PostAdapter.OnItemClickListener{

    private NeedsAndDeedsViewModel needsAndDeedsViewModel;
    private static ArrayList<Post> dataList = new ArrayList<>();
    private PostAdapter myPostAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        needsAndDeedsViewModel =
                new ViewModelProvider(this).get(NeedsAndDeedsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_needs_and_deeds, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.postRecyclerView_needs_and_deeds);

        myPostAdapter = new PostAdapter(dataList, this);

        needsAndDeedsViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList>() {
            @Override
            public void onChanged(@Nullable ArrayList s) {
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(myPostAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });
        return root;
    }

    @Override
    public void onItemClick(int position) {
        Post clickedItem = dataList.get(position);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layout_in_need_fragment, new OpenPostRecordFragment(clickedItem));
        fragmentTransaction.commit();
    }
}