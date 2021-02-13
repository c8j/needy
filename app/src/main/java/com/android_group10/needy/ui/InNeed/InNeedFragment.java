package com.android_group10.needy.ui.InNeed;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.ServiceType;
import com.android_group10.needy.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class InNeedFragment extends Fragment implements PostAdapter.OnItemClickListener {
    View root;
    ArrayList<Post> dataList;
    private InNeedViewModel inNeedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inNeedViewModel =
                new ViewModelProvider(this).get(InNeedViewModel.class);
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

    private List<Post> generateData(){
        dataList = new ArrayList<>();
        User one = new User("Liliia", "07653248601", "blah-blah@mail.com");
        one.setImage(R.mipmap.ic_launcher);
        dataList.add(new Post(1, one, "buy me food", ServiceType.SHOPPING, "Kristianstad", "23001"));
        dataList.add(new Post(2, one, "clean my apartment", ServiceType.CLEANING, "Kristianstad", "23951"));
        dataList.add(new Post(3, one, "give me a break", ServiceType.OTHER, "Malmö", "24834"));
        dataList.add(new Post(3, one, "a ride to the hospital", ServiceType.TRANSPORTATION, "Jävle", "20634"));
        return dataList;
    }

    private void loadData(){
        RecyclerView recycler = root.findViewById(R.id.postRecyclerView_in_need);
        PostAdapter myPostAdapter = new PostAdapter(generateData(), this);
        recycler.setAdapter(myPostAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
        Post clickedItem = dataList.get(position);
        postWindowView(clickedItem);
    }

    public void postWindowView(Object clickedItem) {
        ImageView authorPicture;
        TextView authorName;
        TextView authorRating;
        TextView postDescription;
        TextView authorPhone;

        Button acceptPost;
        Button contactAuthor;

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.open_post, null);

      /*  final AlertDialog alertD = new AlertDialog.Builder(this).create();

        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        send = view.findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        alertD.setView(view);
        alertD.show();*/

    }

}