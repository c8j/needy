package com.android_group10.needy.ui.Admin;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android_group10.needy.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminViewModel extends ViewModel {
    private static final String TAG = "AdminViewModel";
    private MutableLiveData<ArrayList<Report>> reportList;

    /*
    public AdminViewModel() {
        reportList = new MutableLiveData<>();
        reportList.setValue(new ArrayList<>());
    }

     */

    public MutableLiveData<ArrayList<Report>> getReportList() {
        if(reportList == null){
            reportList = new MutableLiveData<ArrayList<Report>>();
            loadReportList();
        }
        return reportList;
    }

    private void loadReportList(){
        //fetch users.----HOW??
    }
}
