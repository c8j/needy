package com.android_group10.needy.ui.ToDo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android_group10.needy.R;

public class ToDoFragment extends Fragment {

    private ToDoViewModel toDoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toDoViewModel =
                new ViewModelProvider(this).get(ToDoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_to_do, container, false);
        final TextView textView = root.findViewById(R.id.text_to_do);
        toDoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("This is To Do fragment");
            }
        });
        return root;
    }
}