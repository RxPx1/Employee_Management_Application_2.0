package com.myappcompany.rob.employeemanagementapplication20.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.myappcompany.rob.employeemanagementapplication20.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button click listener
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            // Go to LoginActivity
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
        });
    }
}