package com.myappcompany.rob.employeemanagementapplication20.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;
import com.myappcompany.rob.employeemanagementapplication20.R;
import com.myappcompany.rob.employeemanagementapplication20.Database.CryptoUtils;

public class ChangeAdminLogin extends AppCompatActivity {

    private EditText newUsernameEditText;
    private EditText newPasscodeEditText;
    private Button saveButton;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_admin_login);

        newUsernameEditText = findViewById(R.id.new_adminusername_edit_text);
        newPasscodeEditText = findViewById(R.id.new_adminpasscode_edit_text);
        saveButton = findViewById(R.id.save_button);
        repository = new Repository(getApplication());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        String newUsername = newUsernameEditText.getText().toString().trim();
        String newPasscode = newPasscodeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newPasscode)) {
            Toast.makeText(this, "Both fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPasscode.length() != 6) {
            Toast.makeText(this, "Passcode must be 6 digits.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Encrypt the new passcode
        String encryptedPasscode = CryptoUtils.encryptToBase64(newPasscode);

        LiveData<Users> adminLiveData = repository.getUsersById(1);
        adminLiveData.observe(this, adminUser -> {
            adminLiveData.removeObservers(this); // Remove the observer after getting the admin user
            if (adminUser != null) {
                adminUser.setUsername(newUsername);
                adminUser.setPassCode(encryptedPasscode);
                repository.update(adminUser);
                Toast.makeText(this, "Admin login updated successfully.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after saving
            } else {
                Toast.makeText(this, "Admin user not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
