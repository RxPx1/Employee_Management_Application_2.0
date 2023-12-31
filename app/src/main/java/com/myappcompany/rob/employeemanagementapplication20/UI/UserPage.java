package com.myappcompany.rob.employeemanagementapplication20.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.Entities.TimeEntries;
import com.myappcompany.rob.employeemanagementapplication20.R;
import java.text.DecimalFormat;
import java.util.Date;

public class UserPage extends AppCompatActivity {

    private Repository repository;
    private int currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        repository = new Repository(getApplication());

        Intent intent = getIntent();
        if (intent.hasExtra("employeeID")) {
            currentUserID = intent.getIntExtra("employeeID", 0);
        } else {
            Toast.makeText(this, "Error: employeeID not provided.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Button clockInButton = findViewById(R.id.button2);
        Button clockOutButton = findViewById(R.id.button3);
        Button viewHoursButton = findViewById(R.id.button4);

        clockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockIn();
            }
        });

        clockOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockOut();
            }
        });

        viewHoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHours();
            }
        });
    }

    private void clockIn() {
        long currentTime = System.currentTimeMillis();
        TimeEntries timeEntry = new TimeEntries();
        timeEntry.setClockInTime(currentTime);
        timeEntry.setDate(new Date(currentTime));
        timeEntry.setEmployeeID(currentUserID);
        repository.insert(timeEntry);
        Toast.makeText(this, "Clocked in successfully.", Toast.LENGTH_SHORT).show();
    }

    private void clockOut() {
        long currentTime = System.currentTimeMillis();

        LiveData<TimeEntries> lastTimeEntryLiveData = repository.getLastTimeEntry(currentUserID);
        lastTimeEntryLiveData.observe(this, new Observer<TimeEntries>() {
            @Override
            public void onChanged(TimeEntries timeEntry) {
                lastTimeEntryLiveData.removeObserver(this);

                if (timeEntry != null && timeEntry.getClockOutTime() == null) {
                    timeEntry.setClockOutTime(currentTime);
                    double totalHours = Double.parseDouble(calculateTotalHours(timeEntry.getClockInTime(), currentTime));
                    timeEntry.setTotalHours(totalHours);
                    timeEntry.setDate(new Date(currentTime));
                    repository.update(timeEntry);

                    DecimalFormat decimalFormat = new DecimalFormat("0.000");
                    String formattedTotalHours = decimalFormat.format(totalHours);

                    Toast.makeText(UserPage.this, "Clocked out successfully. Total hours: " + formattedTotalHours, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserPage.this, "No active clock-in found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String calculateTotalHours(long clockInTime, long clockOutTime) {
        long elapsedTimeInMillis = clockOutTime - clockInTime;
        double hours = (double) elapsedTimeInMillis / (1000 * 60 * 60);
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        return decimalFormat.format(hours);
    }

    private void viewHours() {
        Intent intent = new Intent(UserPage.this, SingleEmployeeHours.class);
        intent.putExtra("employeeID", currentUserID);
        startActivity(intent);
    }
}
