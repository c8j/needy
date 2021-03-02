package com.android_group10.needy.ui.NeedsAndDeeds;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android_group10.needy.Post;

import java.util.ArrayList;

public class NeedsAndDeedsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Post>> _list;

    public NeedsAndDeedsViewModel() {
        _list = new MutableLiveData<>();
        _list.setValue(new ArrayList());
    }

    public MutableLiveData<ArrayList<Post>> getList() {
        return _list;
    }
}