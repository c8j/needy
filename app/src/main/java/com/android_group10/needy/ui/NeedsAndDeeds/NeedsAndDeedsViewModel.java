package com.android_group10.needy.ui.NeedsAndDeeds;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android_group10.needy.Post;

import java.util.ArrayList;

public class NeedsAndDeedsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Post>> _list;
    private final MutableLiveData<String> _defaultText;

    public NeedsAndDeedsViewModel() {
        _list = new MutableLiveData<>();
        _list.setValue(new ArrayList<>());

        _defaultText = new MutableLiveData<>();
        _defaultText.setValue("There are no posts to view");

    }

    public MutableLiveData<ArrayList<Post>> getList() {
        return _list;
    }

    public LiveData<String> getText() {
        return _defaultText;
    }
}