package com.myappcompany.rob.employeemanagementapplication20.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myappcompany.rob.employeemanagementapplication20.Database.CryptoUtils;
import com.myappcompany.rob.employeemanagementapplication20.Database.Repository;
import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;
import com.myappcompany.rob.employeemanagementapplication20.R;


public class LoginPage extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passcodeEditText;
    private LoginViewModel viewModel;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        repository = new Repository(getApplication());
        repository.initializeDatabase();
        repository = new Repository(getApplication());
        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username_edit_text);
        passcodeEditText = findViewById(R.id.passcode_edit_text);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String passcode = passcodeEditText.getText().toString();

        if (!username.isEmpty() && !passcode.isEmpty()) {
            String encryptedPasscode = CryptoUtils.encryptToBase64(passcode);
            if (encryptedPasscode != null) {
                viewModel.getUserByUsernameAndPassword(username, encryptedPasscode).observe(this, new Observer<Users>() {
                    @Override
                    public void onChanged(Users user) {
                        handleUserResult(user);
                    }
                });
            } else {
                Toast.makeText(this, "Error encrypting passcode.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Username and passcode are required.", Toast.LENGTH_SHORT).show();
        }
    }

    private class ViewModelFactory implements ViewModelProvider.Factory {
        private Application application;
        public ViewModelFactory(Application application) {
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new LoginViewModel(application);
        }
    }

    public class LoginViewModel extends ViewModel {
        private Repository repository;
        public LoginViewModel(Application application) {
            repository = new Repository(application);
        }
        public LiveData<Users> getUserByUsernameAndPassword(String username, String encryptedPasscode) {
            return repository.getUserByUsernameAndPassword(username, encryptedPasscode);
        }
    }

    private void handleUserResult(Users user) {
        if (user != null) {
            Intent intent;
            if (user.getEmployeeID() == 1) {
                intent = new Intent(LoginPage.this, AdminPage.class);
            } else if (user.getEmployeeID() >= 2) {
                intent = new Intent(LoginPage.this, UserPage.class);
                intent.putExtra("employeeID", user.getEmployeeID());
            } else {
                Toast.makeText(LoginPage.this, "Invalid username or passcode.", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(intent);
        } else {
            Toast.makeText(LoginPage.this, "Invalid username or passcode.", Toast.LENGTH_SHORT).show();
        }
    }
}
