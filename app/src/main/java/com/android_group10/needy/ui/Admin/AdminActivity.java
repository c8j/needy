package com.android_group10.needy.ui.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.android_group10.needy.Post;
import com.android_group10.needy.PostAdapter;
import com.android_group10.needy.R;
import com.android_group10.needy.Report;
import com.android_group10.needy.ui.LogInAndRegistration.LogIn;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = "AdminActivity";
    private final Activity thisActivity = this;
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
        boolean dealtWith = false;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Report> _reports = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot report : userSnapshot.getChildren()) {
                        Report object = report.getValue(Report.class);
                        if(dealtWith)
                            object.setReacted(true);
                        else {
                            assert object != null;
                            Log.i(TAG, "Reason: " + object.toString());
                            _reports.add(object);
                            System.out.println(_reports.size() + "   - inside");
                        }
                    }
                }

                reports = _reports;
                System.out.println(reports.size() + "   - outside for-loop");
                reportAdapter.updateReports(reports);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(thisActivity, "Could not fetch reported users", Toast.LENGTH_SHORT).show();
            }
        });
        System.out.println(reports.size() + "   - outside listener");

        Button logOutButton = findViewById(R.id.admin_log_out_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, LogIn.class));
        finish();
    }
}