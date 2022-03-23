package com.example.ttsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class ModifyStudentListActivity extends AppCompatActivity {

    // Toast strings
    private final static String ADD_NOT_IMPLEMENTED_TEMP_BASE = "Add function not implemented; attempted to add student with\n";
    private final static String DELETE_NOT_IMPLEMENTED_TEMP_BASE = "Delete function not implemented; attempted to delete student with\n";
    private final static String JSON_COMPILE_ERROR = "Error compiling student details string";

    // Variables for fields
    private EditText addIdEditText, addNameEditText, addYearEditText, deleteIdEditText;

    // Variables for checkboxes
    private CheckBox vacCheckBox, undergradCheckBox;

    // Variables for buttons
    private Button addStudentButton, deleteStudentButton, returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_student_list);

        // Assign all View variables
        addIdEditText = findViewById(R.id.activity_modify_student_list_add_id_edittext);
        addNameEditText = findViewById(R.id.activity_modify_student_list_add_name_edittext);
        addYearEditText = findViewById(R.id.activity_modify_student_list_add_year_edittext);
        deleteIdEditText = findViewById(R.id.activity_modify_student_list_delete_id_edittext);

        vacCheckBox = findViewById(R.id.activity_modify_student_list_add_vac_checkbox);
        undergradCheckBox = findViewById(R.id.activity_modify_student_list_add_undergrad_checkbox);

        addStudentButton = findViewById(R.id.activity_modify_student_list_add_button);
        deleteStudentButton = findViewById(R.id.activity_modify_student_list_delete_button);
        returnButton = findViewById(R.id.activity_modify_student_list_return_button);

        // Listener for addStudentButton: Add student based on given details
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Collect details (no PDPA violations are committed here)
                String addId = addIdEditText.getText().toString();
                String addName = addNameEditText.getText().toString();
                String addYear = addYearEditText.getText().toString();
                boolean isVaccinated = vacCheckBox.isChecked();
                boolean isUndergrad = undergradCheckBox.isChecked();

                // Validate ID, name, year
                if (!(Pattern.matches(CommonValues.NAME_REGEX, addName))) {
                    Toast.makeText(getApplicationContext(), CommonValues.INVALID_NAME_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Pattern.matches(CommonValues.ID_REGEX, addId)) {
                    Toast.makeText(getApplicationContext(), CommonValues.INVALID_ID_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Pattern.matches(CommonValues.YEAR_REGEX, addYear)) {
                    Toast.makeText(getApplicationContext(), CommonValues.INVALID_YEAR_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get JSON string to be passed to server
                String inputJson = compileAddJSONString(addId, addName, addYear, isVaccinated, isUndergrad);
                if (inputJson == null) {
                    Toast.makeText(getApplicationContext(), JSON_COMPILE_ERROR, Toast.LENGTH_SHORT);
                }

                // TODO: Add functionality to perform addition
                // (1) Make POST request to /add_student with inputJson
                // (2) Get reply JSON
                // (3) Check if JSON contains "error" key (in which case duplicate entry exists)
                // (4) If not present, check if JSON contains "success": true to see if addition is successful

                // TODO: Remove this filler code
                // -----START OF CODE TO BE REMOVED-----

                // Show Toast indicating that function is not implemented
                String toastMessage = ADD_NOT_IMPLEMENTED_TEMP_BASE
                        + "\tID: " + addId + "\n"
                        + "\tName: " + addName + "\n"
                        + "\tMatriculation year: " + addYear + "\n"
                        + "\tFully vaccinated: " + (isVaccinated ? "Y" : "N") + "\n"
                        + "\tUndergraduate: " + (isUndergrad ? "Y" : "N");
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();

                // -----END OF CODE TO BE REMOVED-----

            }
        });

        // Listener for deleteStudentButton: Delete student given their ID
        deleteStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get student ID
                String deleteId = deleteIdEditText.getText().toString();

                // Validate student ID
                if (!Pattern.matches(CommonValues.ID_REGEX, deleteId)) {
                    Toast.makeText(getApplicationContext(), CommonValues.INVALID_ID_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }

                // TODO: Add delete student functionality
                // (1) Make POST request with JSON string "{\"id\": ...}"
                // (2) Get JSON reply
                // (3) Check if reply is "{\"success\": true}" to see if deletion is successful

                // TODO: Remove this filler code
                // -----START OF CODE TO BE REMOVED-----

                // Show Toast indicating that function is not implemented
                String toastMessage = DELETE_NOT_IMPLEMENTED_TEMP_BASE
                        + "\tID: " + deleteId + "\n";
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();

                // -----END OF CODE TO BE REMOVED-----

            }
        });

        // Listener for returnButton: Return to main menu
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Return to main menu
                finish();
            }
        });
    }

    // Compile JSON with student details for adding new student
    private String compileAddJSONString(String addId, String addName, String addYear, boolean isVaccinated, boolean isUndergrad) {
        try {
            JSONObject compiledJson = new JSONObject();
            compiledJson.put("id", Integer.valueOf(addId));
            compiledJson.put("name", addName);
            compiledJson.put("year", Integer.valueOf(addYear));
            compiledJson.put("is_vaccinated", isVaccinated);
            compiledJson.put("is_undergraduate", isUndergrad);
            return compiledJson.toString();
        } catch (Exception e) {
            return null;
        }
    }
}