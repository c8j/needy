package com.android_group10.needy.ui.NeedsAndDeeds;

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

public class NeedsAndDeedsFragment extends Fragment {

    private NeedsAndDeedsViewModel needsAndDeedsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        needsAndDeedsViewModel =
                new ViewModelProvider(this).get(NeedsAndDeedsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_needs_and_deeds, container, false);
        final TextView textView = root.findViewById(R.id.text_needs_and_deeds);
        needsAndDeedsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("This is Needs and Deeds Fragment");
            }
        });
        return root;
    }
}