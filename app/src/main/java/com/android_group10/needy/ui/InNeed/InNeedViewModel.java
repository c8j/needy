package com.android_group10.needy.ui.InNeed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android_group10.needy.Post;

public class InNeedViewModel extends ViewModel {


    private MutableLiveData<String> mText;

    public InNeedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is in need fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}