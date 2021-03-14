package com.android_group10.needy.ui.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.Report;
import com.android_group10.needy.ui.LogInAndRegistration.LogIn;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
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

        ReportAdapter reportAdapter = new ReportAdapter(this, reports);
        reportsRecyclerView.setAdapter(reportAdapter);

        getReportsFromDb();
        //Update recyclerview adapter
        reportAdapter.updateReports(reports);

        Log.i(TAG, "reports size = " + reports.size());
        for (Report report: reports){
            Log.i(TAG, "Report uid: " + report.getBlamedUserUID());
        }

        Button logOutButton = findViewById(R.id.admin_log_out_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void getReportsFromDb(){
        final Report[] report = new Report[1];
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Reports");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    for (DataSnapshot reportSnapshot: postSnapshot.getChildren()){
                        report[0] = (Report) reportSnapshot.getValue(Report.class);
                        assert report[0] != null;
                        Log.i(TAG, "Reason: " + report[0].getDescription());
                        reports.add(report[0]); //<--------------SIZE IS ALWAYS 0
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(thisActivity, "Could not fetch reported users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, LogIn.class));
        finish();
    }
}