package com.android_group10.needy.ui.Admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android_group10.needy.Post;
import com.android_group10.needy.Report;

import java.util.ArrayList;

public class AdminViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Report>> reportList;

    public AdminViewModel() {
        reportList = new MutableLiveData<>();
        reportList.setValue(new ArrayList<>());
    }

    public MutableLiveData<ArrayList<Report>> getReportList() {
        return reportList;
    }

}
