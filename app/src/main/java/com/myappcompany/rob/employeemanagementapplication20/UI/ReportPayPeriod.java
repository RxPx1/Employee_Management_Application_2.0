package com.myappcompany.rob.employeemanagementapplication20.UI;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.Entities.TimeEntries;
import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;
import com.myappcompany.rob.employeemanagementapplication20.R;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


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

                for (Users user : users) {
                    if (user.getEmployeeID() != 1) {
                        int employeeID = user.getEmployeeID();
                        LiveData<List<TimeEntries>> timeEntriesLiveData = repository.getTimeEntriesForEmployee(employeeID);
                        timeEntriesLiveData.observe(this, timeEntriesList -> {
                            if (timeEntriesList != null && timeEntriesList.size() > 0) {
                                double totalHours = 0.0;

                                reportBuilder.append("EmployeeID ").append(employeeID)
                                        .append(" - ").append(user.getUsername()).append("\n");

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                DecimalFormat decimalFormat = new DecimalFormat("0.000");

                                for (TimeEntries entry : timeEntriesList) {
                                    String formattedDate = dateFormat.format(entry.getDate());
                                    String formattedTotalHours = decimalFormat.format(entry.getTotalHours());
                                    reportBuilder.append(formattedDate)
                                            .append("            ").append(formattedTotalHours).append(" hours\n");

                                    totalHours += entry.getTotalHours();
                                }

                                reportBuilder.append("Total Hours: ").append(decimalFormat.format(totalHours)).append(" hours\n\n");
                                reportText.setText(reportBuilder.toString());
                            }
                        });
                    }
                }
            }
        });
    }
}