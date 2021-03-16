package com.android_group10.needy.ui.InNeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.ServiceType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class InNeedFragment extends Fragment {
    private InNeedViewModel inNeedViewModel;
    private View root;
    public static ArrayList<Post> dataList = new ArrayList<>();
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private RecyclerView recyclerView;
    private TextView textView;
    private Spinner spinner;
    private TextView hiddenText;
    private EditText filterText;
    private ImageButton applyFilters;
    private ImageButton clearFilters;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inNeedViewModel =
                new ViewModelProvider(this).get(InNeedViewModel.class);
        root = inflater.inflate(R.layout.fragment_in_need, container, false);

        recyclerView = root.findViewById(R.id.postRecyclerView_in_need);
        textView = root.findViewById(R.id.text_default2);
        filterText = root.findViewById(R.id.address_filter);
        applyFilters = root.findViewById(R.id.btn_set_filter);
        clearFilters = root.findViewById(R.id.btn_clear_filter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        PostAdapter myPostAdapter = new PostAdapter(requireContext(), dataList, position -> {
            Post clickedItem = dataList.get(position);

            //Create action (from generated code by safeargs plugin) and navigate using it while passing the clicked post
            InNeedFragmentDirections.ActionInNeedToOpenPost action = InNeedFragmentDirections.actionInNeedToOpenPost(clickedItem);
            Navigation.findNavController(root).navigate(action);
        });
        recyclerView.setAdapter(myPostAdapter);

        textView.setText(inNeedViewModel.getText().getValue());

        hiddenText = root.findViewById(R.id.hidden_textView1);
        spinner = root.findViewById(R.id.spinner1);
        String[] items = {"Select a Service type here (optional)", ServiceType.WALK_A_DOG.toString(), ServiceType.SHOPPING.toString(), ServiceType.TRANSPORTATION.toString(), ServiceType.CLEANING.toString(), ServiceType.OTHER.toString()};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                hiddenText.setText(String.valueOf(parent.getItemIdAtPosition(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ValueEventListener listListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addData(snapshot, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.getReference().child("Posts").addValueEventListener(listListener);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedServiceType = Integer.parseInt(hiddenText.getText().toString());
                String addressFilter = filterText.getText().toString();
                if (!(addressFilter.isEmpty() && selectedServiceType == 0)) {
                    ValueEventListener listListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            addData(snapshot, true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    db.getReference().child("Posts").addValueEventListener(listListener);
                }
            }
        });
        clearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        filterText.setText("");
                        spinner.setSelection(0);
                        addData(snapshot, false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                db.getReference().child("Posts").addValueEventListener(listListener);
            }
        });
    }

    private void addData(DataSnapshot snapshot, boolean filters) {
        if (snapshot.hasChildren()) {
            int count = 0;
            if (snapshot.getChildrenCount() != count) {
                dataList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Post object = child.getValue(Post.class);
                    assert object != null;
                    if (object.getPostStatus() == 1) {
                        object.setAuthorUID(String.valueOf(child.child("author").getValue()));

                        if (!filters) {
                            dataList.add(object);
                        } else {
                            int selectedServiceType = Integer.parseInt(hiddenText.getText().toString());
                            String addressFilter = filterText.getText().toString();
                            if (!addressFilter.isEmpty()) {
                                if (object.getCity().contains(addressFilter) || object.getZipCode().equals(addressFilter)) {
                                    if (selectedServiceType > 0) {
                                        if (object.getServiceType() == (selectedServiceType - 1)) {
                                            dataList.add(object);
                                        }
                                    } else dataList.add(object);
                                }
                            } else if (selectedServiceType > 0) {
                                if (object.getServiceType() == (selectedServiceType - 1)) {
                                    if (!addressFilter.isEmpty()) {
                                        if (object.getCity().contains(addressFilter) || object.getZipCode().equals(addressFilter)) {
                                            dataList.add(object);
                                        }
                                    } else dataList.add(object);
                                }
                            }
                        }
                    }
                    count++;
                }
                if (dataList.size() != 0) {
                    textView.setText("");
                } else textView.setText(inNeedViewModel.getText().getValue());

                //Update recyclerview adapter
                if (recyclerView != null) {
                    PostAdapter postAdapter = (PostAdapter) recyclerView.getAdapter();
                    if (postAdapter != null) {
                        postAdapter.updateData(dataList);
                    }
                }
            }
        }
    }
}