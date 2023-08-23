package com.myappcompany.rob.employeemanagementapplication20.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.Entities.TimeEntries;
import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;
import com.myappcompany.rob.employeemanagementapplication20.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportPayPeriod extends AppCompatActivity {

    private Repository repository;
    private TextView reportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pay_period);

        repository = new Repository(getApplication());
        reportText = findViewById(R.id.reportText);

        generateReport();
    }

    private void generateReport() {
        LiveData<List<Users>> allUsersLiveData = repository.getAllUsers();
        allUsersLiveData.observe(this, users -> {
            if (users != null && users.size() > 0) {
                StringBuilder reportBuilder = new StringBuilder();

                LiveData<List<TimeEntries>> timeEntriesLiveData = repository.getAllTimeEntriesForNonAdminEmployees();
                timeEntriesLiveData.observe(this, timeEntriesList -> {
                    if (timeEntriesList != null && timeEntriesList.size() > 0) {
                        Map<Integer, Map<Date, Double>> userTimeEntriesMap = new HashMap<>();

                        for (TimeEntries entry : timeEntriesList) {
                            int employeeID = entry.getEmployeeID();
                            Map<Date, Double> dateTotalHoursMap = userTimeEntriesMap.getOrDefault(employeeID, new HashMap<>());

                            Date entryDate = entry.getDate();
                            double totalHours = dateTotalHoursMap.getOrDefault(entryDate, 0.0);
                            totalHours += entry.getTotalHours();
                            dateTotalHoursMap.put(entryDate, totalHours);

                            userTimeEntriesMap.put(employeeID, dateTotalHoursMap);
                        }

                        for (Map.Entry<Integer, Map<Date, Double>> userEntry : userTimeEntriesMap.entrySet()) {
                            int employeeID = userEntry.getKey();
                            LiveData<Users> userLiveData = repository.getUserById(employeeID);
                            userLiveData.observe(this, user -> {
                                if (user != null) {
                                    Map<Date, Double> dateTotalHoursMap = userEntry.getValue();

                                    reportBuilder.append("EmployeeID ").append(employeeID)
                                            .append(" - ").append(user.getUsername()).append("\n");

                                    for (Map.Entry<Date, Double> entry : dateTotalHoursMap.entrySet()) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        String formattedDate = dateFormat.format(entry.getKey());
                                        DecimalFormat decimalFormat = new DecimalFormat("0.000");
                                        String formattedTotalHours = decimalFormat.format(entry.getValue());

                                        reportBuilder.append(formattedDate)
                                                .append("            ").append(formattedTotalHours).append(" hours\n");
                                    }

                                    // Update the report text after processing
                                    reportText.setText(reportBuilder.toString());
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
