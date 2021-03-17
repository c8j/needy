package com.android_group10.needy.ui.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android_group10.needy.R;
import com.android_group10.needy.Report;
import com.android_group10.needy.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportsViewHolder> {
    Context context;
    ArrayList<Report> reports;
    String blamedUID;
    public ReportAdapter(Context context, ArrayList<Report> reports){
        this.context = context;
        this.reports = reports;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.report_row, parent, false);
        return new ReportsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        if(reports.size() == 0){
            holder.reportedEmail.setText(R.string.admin_no_reports);
            holder.reportAuthorUid.setText(" - ");
            holder.description.setText(" - ");
            holder.postUid.setText(" - ");
        }
        else {
            blamedUID = reports.get(position).getBlamedUserUID();
            showBlamedEmailID(holder, position);
            holder.reportAuthorUid.setText(reports.get(position).getReportAuthorUID() );
            holder.description.setText(reports.get(position).getDescription());
            String postUIdInReport = reports.get(position).getPostUID();
            holder.postUid.setText(postUIdInReport);


        }
    }

    public void updateReports(ArrayList<Report> reports){
        this.reports = reports;
        notifyDataSetChanged();
    }

    private void showBlamedEmailID(ReportsViewHolder holder, int position){
        final User[] blamedUser = new User[1];
        DatabaseReference blamedUserRef = FirebaseDatabase.getInstance().getReference("Users").child(blamedUID);
        blamedUserRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                blamedUser[0] = snapshot.getValue(User.class);
                assert blamedUser[0] != null;
                holder.reportedEmail.setText("Reported user: " + blamedUser[0].getEmail());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = reports.size();
        if(count == 0)
            count = 1;
        return count;
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder{
        TextView reportedEmail;
        TextView reportAuthorUid;
        TextView description;
        TextView postUid;
        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            reportedEmail = itemView.findViewById(R.id.reported_uid_textView);
            reportAuthorUid = itemView.findViewById(R.id.reported_by_uid_textView);
            description = itemView.findViewById(R.id.admin_rep_description_textView);
            postUid = itemView.findViewById(R.id.admin_post_UID_textView);
        }
    }
}
