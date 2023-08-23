package com.myappcompany.rob.employeemanagementapplication20.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.myappcompany.rob.employeemanagementapplication20.Entities.TimeEntries;
import com.myappcompany.rob.employeemanagementapplication20.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SingleEmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ENTRY = 0;
    private static final int VIEW_TYPE_TOTAL_HOURS = 1;

    private List<TimeEntries> timeEntriesList = new ArrayList<>();
    private Context context;

    public SingleEmployeeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_ENTRY) {
            View itemView = inflater.inflate(R.layout.time_entry_item, parent, false);
            return new TimeEntryViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_TOTAL_HOURS) {
            View itemView = inflater.inflate(R.layout.total_hours_item, parent, false);
            return new TotalHoursViewHolder(itemView);
        }
        throw new IllegalArgumentException("Unknown viewType");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TimeEntryViewHolder) {
            TimeEntries timeEntry = timeEntriesList.get(position);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(timeEntry.getDate());
            TimeEntryViewHolder entryViewHolder = (TimeEntryViewHolder) holder;
            entryViewHolder.dateTextView.setText(formattedDate);
            entryViewHolder.totalHoursTextView.setText(String.format(Locale.getDefault(), "%.4f", timeEntry.getTotalHours()));
        } else if (holder instanceof TotalHoursViewHolder) {
            double totalPayPeriodHours = calculateTotalPayPeriodHours();
            TotalHoursViewHolder totalHoursViewHolder = (TotalHoursViewHolder) holder;
            totalHoursViewHolder.totalHoursLabelTextView.setText("Total Hours");
            totalHoursViewHolder.totalHoursValueTextView.setText(String.format(Locale.getDefault(), "%.4f", totalPayPeriodHours));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < timeEntriesList.size()) {
            return VIEW_TYPE_ENTRY;
        } else {
            return VIEW_TYPE_TOTAL_HOURS;
        }
    }

    @Override
    public int getItemCount() {
        return timeEntriesList.size() + 1;
    }

    public void setTimeEntriesList(List<TimeEntries> timeEntriesList) {
        this.timeEntriesList = timeEntriesList;
        notifyDataSetChanged();
    }

    private double calculateTotalPayPeriodHours() {
        double totalPayPeriodHours = 0.0;
        for (TimeEntries timeEntry : timeEntriesList) {
            totalPayPeriodHours += timeEntry.getTotalHours();
        }
        return totalPayPeriodHours;
    }

    private static class TimeEntryViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView totalHoursTextView;

        public TimeEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            totalHoursTextView = itemView.findViewById(R.id.totalHoursTextView);
        }
    }

    private static class TotalHoursViewHolder extends RecyclerView.ViewHolder {
        private TextView totalHoursLabelTextView;
        private TextView totalHoursValueTextView;

        public TotalHoursViewHolder(@NonNull View itemView) {
            super(itemView);
            totalHoursLabelTextView = itemView.findViewById(R.id.totalHoursLabelTextView);
            totalHoursValueTextView = itemView.findViewById(R.id.totalHoursValueTextView);
        }
    }
}
