package com.android_group10.needy.ui.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android_group10.needy.R;
import com.android_group10.needy.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = "AdminActivity";
    private Activity thisActivity = this;
    private RecyclerView reportsRecyclerView;
    private AdminViewModel adminViewModel;
    private ArrayList<Report> reports = new ArrayList<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        reportsRecyclerView = findViewById(R.id.reportsRecyclerView);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Reports");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    for (DataSnapshot reportSnapshot: postSnapshot.getChildren()){
                        Report report = reportSnapshot.getValue(Report.class);
                        assert report != null;
                        Log.i(TAG, "Reason: " + report.getDescription());
                        reports.add(report); //<--------------SIZE IS ALWAYS 0
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(thisActivity, "Could not fetch reported users", Toast.LENGTH_SHORT).show();
            }
        });


        ReportsAdapter reportsAdapter = new ReportsAdapter(this, reports);
        reportsRecyclerView.setAdapter(reportsAdapter);

        Log.i(TAG, "reports size = " + reports.size());
        for (Report report: reports){
            Log.i(TAG, "Report uid: " + report.getBlamedUserUID());
        }
    }
}