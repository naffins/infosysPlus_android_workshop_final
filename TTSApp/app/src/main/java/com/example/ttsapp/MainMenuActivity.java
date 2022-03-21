package com.example.ttsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        // Assign buttons
        viewListButton = findViewById(R.id.activity_main_menu_student_list_button);
        modifyListButton = findViewById(R.id.activity_main_menu_modify_student_list_button);
        exitButton = findViewById(R.id.activity_main_menu_exit_button);

        // Add onClick listener for view list button
        viewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start view student list activity to list students
                startActivity(new Intent(MainMenuActivity.this,ViewStudentListActivity.class));
            }
        });

        // Add onClick listener for modify list button
        modifyListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start modify student list activity to modify student list
                startActivity(new Intent(MainMenuActivity.this,ModifyStudentListActivity.class));
            }
        });

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