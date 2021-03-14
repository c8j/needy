package com.android_group10.needy.ui.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android_group10.needy.R;
import com.android_group10.needy.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportsViewHolder> {
    Context context;
    ArrayList<Report> reports;
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
            holder.reportedUid.setText(R.string.admin_no_reports);
            holder.deleteUserButton.setVisibility(View.GONE);
            holder.reportAuthorUid.setText(" - ");
            holder.description.setText(" - ");
            holder.postUid.setText(" - ");
        }
        else {
            holder.reportedUid.setText(R.string.admin_reported_uid + reports.get(position).getBlamedUserUID());
            holder.reportAuthorUid.setText(reports.get(position).getReportAuthorUID() );
            holder.description.setText(reports.get(position).getDescription());
            holder.postUid.setText(reports.get(position).getPostUID());
        }
    }

    public void updateReports(ArrayList<Report> reports){
        this.reports = reports;
        notifyDataSetChanged();  //TODO: Use better methods for this
    }

    @Override
    public int getItemCount() {
        int count = reports.size();
        if(count == 0)
            count = 1;
        return count;
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder{
        TextView reportedUid;
        TextView reportAuthorUid;
        TextView description;
        TextView postUid;
        Button deleteUserButton;
        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            reportedUid = itemView.findViewById(R.id.reported_uid_textView);
            reportAuthorUid = itemView.findViewById(R.id.reported_by_uid_textView);
            description = itemView.findViewById(R.id.admin_rep_description_textView);
            postUid = itemView.findViewById(R.id.admin_post_UID_textView);
            deleteUserButton = itemView.findViewById(R.id.remove_user_button);
        }
    }
}
