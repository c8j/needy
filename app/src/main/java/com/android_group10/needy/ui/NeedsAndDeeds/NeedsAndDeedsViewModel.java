package com.android_group10.needy.ui.NeedsAndDeeds;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NeedsAndDeedsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NeedsAndDeedsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}