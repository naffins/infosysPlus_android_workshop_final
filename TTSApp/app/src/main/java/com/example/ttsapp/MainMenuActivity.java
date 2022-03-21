package com.example.ttsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    // Variables for buttons
    Button viewListButton, modifyListButton, exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Assign exit button
        exitButton = findViewById(R.id.activity_main_menu_exit_button);

        // Add onClick listener for exit button
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Return to connect activity
                finish();
            }
        });
    }
}