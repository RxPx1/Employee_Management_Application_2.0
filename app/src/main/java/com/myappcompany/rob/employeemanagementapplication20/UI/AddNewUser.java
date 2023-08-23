package com.myappcompany.rob.employeemanagementapplication20.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myappcompany.rob.employeemanagementapplication20.Database.CryptoUtils;
import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;
import com.myappcompany.rob.employeemanagementapplication20.R;

public class AddNewUser extends AppCompatActivity {

    private Repository repository;
    private EditText newUsernameEditText;
    private EditText newPasscodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        repository = new Repository(getApplication());

        newUsernameEditText = findViewById(R.id.new_username_edit_text);
        newPasscodeEditText = findViewById(R.id.new_passcode_edit_text);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> saveNewUser());
    }

    private void saveNewUser() {
        String newUsername = newUsernameEditText.getText().toString().trim();
        String newPasscode = newPasscodeEditText.getText().toString();

        if (newUsername.isEmpty() || newPasscode.isEmpty()) {
            Toast.makeText(this, "Please enter both username and passcode.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the passcode is exactly 6 digits
        if (newPasscode.length() != 6) {
            Toast.makeText(this, "Passcode must be exactly 6 digits.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Encrypt the passcode if needed
        String encryptedPasscode = CryptoUtils.encryptToBase64(newPasscode);

        Users newUser = new Users();
        newUser.setUsername(newUsername);
        newUser.setPassCode(encryptedPasscode);

        repository.insert(newUser);

        // Show a success message or perform any other necessary actions
        Toast.makeText(this, "New user added successfully.", Toast.LENGTH_SHORT).show();

        // Finish the activity to go back to the AdminPage
        finish();
    }

}
