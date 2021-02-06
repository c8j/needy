package com.android_group10.needy.ui.InNeed;

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

public class InNeedFragment extends Fragment {

    private InNeedViewModel inNeedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inNeedViewModel =
                new ViewModelProvider(this).get(InNeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_in_need, container, false);
        final TextView textView = root.findViewById(R.id.text_in_need);
        inNeedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("This is In need Fragment");
            }
        });
        return root;
    }
}