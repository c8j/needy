package com.android_group10.needy.ui.Admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android_group10.needy.Post;

import java.util.ArrayList;

public class AdminViewModel extends ViewModel {
    private static final String TAG = "AdminViewModel";
    private final MutableLiveData<Boolean> _reacted;

    public AdminViewModel() {
        _reacted = new MutableLiveData<>();
        _reacted.setValue(false);

    }

    public LiveData<Boolean> isReacted() {
        return _reacted;
    }
}
