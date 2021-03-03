package com.android_group10.needy.ui.InNeed;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.android_group10.needy.Post;
import com.android_group10.needy.R;
import com.android_group10.needy.ServiceType;
import com.android_group10.needy.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InNeedViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Post>> _list;
    private final MutableLiveData<String> _defaultText;

    public InNeedViewModel() {
        _list = new MutableLiveData<>();
        _list.setValue(new ArrayList<>());

        _defaultText = new MutableLiveData<>();
        _defaultText.setValue("There are no posts to show");

    }

    public MutableLiveData<ArrayList<Post>> getList() {
        return _list;
    }

    public LiveData<String> getText() {
        return _defaultText;
    }
}