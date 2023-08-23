package com.myappcompany.rob.employeemanagementapplication20.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.myappcompany.rob.employeemanagementapplication20.Adapter.SingleEmployeeAdapter;
import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.Entities.TimeEntries;
import com.myappcompany.rob.employeemanagementapplication20.R;

import java.util.List;

public class SingleEmployeeHours extends AppCompatActivity {

    private int employeeID;
    private Repository repository;
    private SingleEmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_employee_hours);

        // Initialize the repository
        repository = new Repository(getApplication());

        // Retrieve the employeeID from the intent's extras
        if (getIntent().hasExtra("employeeID")) {
            employeeID = getIntent().getIntExtra("employeeID", 0);
        } else {
            // Handle case where employeeID was not passed
            Log.e("SingleEmployeeHours", "Error: employeeID not provided.");
            finish();
            return;
        }

        // Set up the RecyclerView and its adapter
        RecyclerView recyclerView = findViewById(R.id.employeehours);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SingleEmployeeAdapter(this);
        recyclerView.setAdapter(adapter);

        // Fetch time entries for the current employee using LiveData
        repository.getTimeEntriesForEmployee(employeeID).observe(this, timeEntries -> {
            if (timeEntries != null) {
                adapter.setTimeEntriesList(timeEntries);
            }
        });
    }
}
