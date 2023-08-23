package com.myappcompany.rob.employeemanagementapplication20.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.myappcompany.rob.employeemanagementapplication20.Adapter.SingleEmployeeAdapter;
import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.R;


public class SingleEmployeeHours extends AppCompatActivity {

    private int employeeID;
    private Repository repository;
    private SingleEmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_employee_hours);

        repository = new Repository(getApplication());

        if (getIntent().hasExtra("employeeID")) {
            employeeID = getIntent().getIntExtra("employeeID", 0);
        } else {
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.employeehours);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SingleEmployeeAdapter(this);
        recyclerView.setAdapter(adapter);
        repository.getTimeEntriesForEmployee(employeeID).observe(this, timeEntries -> {
            if (timeEntries != null) {
                adapter.setTimeEntriesList(timeEntries);
            }
        });
    }
}
