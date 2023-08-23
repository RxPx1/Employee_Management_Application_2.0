package com.myappcompany.rob.employeemanagementapplication20.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;
import com.myappcompany.rob.employeemanagementapplication20.R;

import java.util.ArrayList;
import java.util.List;

public class AdminPage extends AppCompatActivity {

    private Repository repository;
    private Spinner employeeHoursSpinner;
    private Spinner deleteUserSpinner;
    private Button deleteUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        repository = new Repository(getApplication());

        // Find the views
        employeeHoursSpinner = findViewById(R.id.hours_spinner);
        deleteUserSpinner = findViewById(R.id.delete_user_spinner);
        deleteUserButton = findViewById(R.id.button6);

        // Set up the delete functionality
        deleteUserButton.setOnClickListener(v -> deleteUser());

        // Get a list of all users (excluding admin)
        LiveData<List<Users>> allUsersLiveData = repository.getAllUsers();
        allUsersLiveData.observe(this, users -> {
            if (users != null && users.size() > 0) {
                List<Users> userList = new ArrayList<>();
                for (Users user : users) {
                    if (user.getEmployeeID() != 1) { // Exclude admin
                        userList.add(user);
                    }
                }

                // Populate the user names in the spinners
                ArrayAdapter<Users> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, userList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                employeeHoursSpinner.setAdapter(adapter);
                deleteUserSpinner.setAdapter(adapter);
            }
        });

        Button addUserButton = findViewById(R.id.button5);
        addUserButton.setOnClickListener(v -> {
            // Start the AddNewUser activity
            Intent intent = new Intent(AdminPage.this, AddNewUser.class);
            startActivity(intent);
        });

        Button employeeHoursButton = findViewById(R.id.button7);
        employeeHoursButton.setOnClickListener(v -> {
            Users selectedUser = (Users) employeeHoursSpinner.getSelectedItem();
            if (selectedUser != null) {
                Intent intent = new Intent(AdminPage.this, SingleEmployeeHours.class);
                intent.putExtra("employeeID", selectedUser.getEmployeeID());
                startActivity(intent);
            } else {
                Toast.makeText(this, "No user selected.", Toast.LENGTH_SHORT).show();
            }
        });

        Button changeAdminLoginButton = findViewById(R.id.button9);
        changeAdminLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPage.this, ChangeAdminLogin.class);
            startActivity(intent);
        });

        Button reportPayPeriodButton = findViewById(R.id.button8);
        reportPayPeriodButton.setOnClickListener(v -> {
            // Log a message to confirm that the button click is being handled
            Log.d("ButtonDebug", "Report Pay Period button clicked");

            // Start the ReportPayPeriod activity
            Intent intent = new Intent(AdminPage.this, ReportPayPeriod.class);
            startActivity(intent);
        });

        Button resetTimeDatabaseButton = findViewById(R.id.reset_time_database);
        resetTimeDatabaseButton.setOnClickListener(v -> resetTimeDatabase());

    }

    private void deleteUser() {
        Users selectedUser = (Users) deleteUserSpinner.getSelectedItem();

        if (selectedUser != null) {
            repository.delete(selectedUser);
            Toast.makeText(this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No user selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetTimeDatabase() {
        // Perform database clearing or resetting logic here
        // For example, you might want to delete all time entries
        repository.deleteAllTimeEntries();

        Toast.makeText(this, "Time database reset successfully.", Toast.LENGTH_SHORT).show();
    }

}
