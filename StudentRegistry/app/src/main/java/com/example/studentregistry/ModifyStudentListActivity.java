package com.example.studentregistry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
    private final static String OPERATION_ERROR = "Error: operation unsuccessful; please see logs";
    private final static String DUPLICATE_ENTRY_ERROR = "Error: ID already exists";
    private final static String ADD_SUCCESS = "Successfully added student";
    private final static String DELETE_SUCCESS = "Successfully deleted student";
    private final static String NOT_FOUND_ERROR = "Error: ID not found";

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
                    Toast.makeText(getApplicationContext(), JSON_COMPILE_ERROR, Toast.LENGTH_SHORT).show();
                }

                // TODO: Add functionality to perform addition
                // (1) Make POST request to /add_student with inputJson
                // (2) Get reply JSON
                // (3) Check response code: is it 200 (success) or 400 (id already exists) or otherwise?

                // Make POST request to /add_student with student data as a string in inputJson
                RequestHelper requestHelper = new RequestHelper("/add_student", inputJson, "POST", new RequestHelper.PostRequestTask() {
                    @Override
                    public void postRequestExecute(String jsonOutput, int responseCode) {
                        // Branch based on response code
                        // If an entry with the same ID already exists:
                        if (responseCode==400) {
                            Toast.makeText(getApplicationContext(), DUPLICATE_ENTRY_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Otherwise, if request is not OK (eg. error 500 or 422)
                        if (responseCode!=200) {
                            // Print error message and log response code
                            Toast.makeText(getApplicationContext(), OPERATION_ERROR, Toast.LENGTH_SHORT).show();
                            Log.e("ModifyStudentListActivity","Error when attempting to add student");
                            Log.e("ModifyStudentListActivity","Response code: " + Integer.toString(responseCode));
                            return;
                        }
                        Toast.makeText(getApplicationContext(), ADD_SUCCESS, Toast.LENGTH_SHORT).show();
                    }
                });
                // Start HTTP request thread
                requestHelper.start();

                // TODO: Remove this filler code
                // -----START OF CODE TO BE REMOVED-----

                // Show Toast indicating that function is not implemented
                /*String toastMessage = ADD_NOT_IMPLEMENTED_TEMP_BASE
                        + "\tID: " + addId + "\n"
                        + "\tName: " + addName + "\n"
                        + "\tMatriculation year: " + addYear + "\n"
                        + "\tFully vaccinated: " + (isVaccinated ? "Y" : "N") + "\n"
                        + "\tUndergraduate: " + (isUndergrad ? "Y" : "N");
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();*/

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
                // (1) Make DELETE request to /student?id=<student_id>
                // (2) Get reply
                // (3) Check if response code is 200 or 400 (ID doesn't exist) or other

                // Make DELETE request to /student?id=<student_id> where our student_id is the String deleteId
                // We do not pass any request body to be sent
                RequestHelper requestHelper = new RequestHelper("/student?id=" + deleteId,
                        "", "DELETE", new RequestHelper.PostRequestTask() {
                    
                    @Override
                    public void postRequestExecute(String jsonOutput, int responseCode) {

                        // Branch based on the response code
                        // If ID does not exist:
                        if (responseCode==400) {
                            Toast.makeText(getApplicationContext(), NOT_FOUND_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // If some other error occurs (eg. code 500)
                        if (responseCode!=200) {
                            Toast.makeText(getApplicationContext(), OPERATION_ERROR, Toast.LENGTH_SHORT).show();
                            Log.e("ModifyStudentListActivity","Could not delete student");
                            Log.e("ModifyStudentListActivity","Response code: " + Integer.toString(responseCode));
                            return;
                        }
                        // Otherwise, if successful
                        Toast.makeText(getApplicationContext(), DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
                    }
                });
                // Start request thread
                requestHelper.start();

                // TODO: Remove this filler code
                // -----START OF CODE TO BE REMOVED-----

                // Show Toast indicating that function is not implemented
                //String toastMessage = DELETE_NOT_IMPLEMENTED_TEMP_BASE + "\tID: " + deleteId + "\n";
                //Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();

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

    // Compile JSON string with student details for adding new student
    // Returns a JSON object string with id, name, year, is_vaccinated, is_undergraduate keys
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