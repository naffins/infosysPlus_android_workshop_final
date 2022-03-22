package com.example.ttsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class ViewStudentListActivity extends AppCompatActivity {

    // Class-specific constants for Spinner checking
    private final static String vacSpinnerDefault = "Fully vaccinated";
    private final static String undergradSpinnerDefault = "Undergraduate";

    // Toast messages
    private final static String FILTER_COMPILE_ERROR = "Error: could not compile JSON search filter";
    private final static String LIST_NOT_IMPLEMENTED_TEMP_BASE = "List function not implemented; attempted to list students with filter";

    // Variables for interactive components
    private EditText nameEditText, idEditText, yearEditText;
    private CheckBox nameCheckBox, idCheckBox, yearCheckBox, vacCheckBox, undergradCheckBox;
    private Spinner vacSpinner, undergradSpinner;
    private Button loadButton, returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_list);

        // Set variables for components
        nameEditText = findViewById(R.id.activity_view_student_list_name_edittext);
        idEditText = findViewById(R.id.activity_view_student_list_id_edittext);
        yearEditText = findViewById(R.id.activity_view_student_list_year_edittext);

        nameCheckBox = findViewById(R.id.activity_view_student_list_name_checkbox);
        idCheckBox = findViewById(R.id.activity_view_student_list_id_checkbox);
        yearCheckBox = findViewById(R.id.activity_view_student_list_year_checkbox);
        vacCheckBox = findViewById(R.id.activity_view_student_list_vac_checkbox);
        undergradCheckBox = findViewById(R.id.activity_view_student_list_undergrad_checkbox);

        vacSpinner = findViewById(R.id.activity_view_student_list_vac_spinner);
        undergradSpinner = findViewById(R.id.activity_view_student_list_undergrad_spinner);

        loadButton = findViewById(R.id.activity_view_student_list_load_button);
        returnButton = findViewById(R.id.activity_view_student_list_return_button);

        // Load Spinner values and set spinner layouts
        ArrayAdapter<CharSequence> vacSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.activity_view_student_list_vac_spinner_values,android.R.layout.simple_spinner_item);
        vacSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vacSpinner.setAdapter(vacSpinnerAdapter);

        ArrayAdapter<CharSequence> undergradSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.activity_view_student_list_undergrad_spinner_values,android.R.layout.simple_spinner_item);
        undergradSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        undergradSpinner.setAdapter(undergradSpinnerAdapter);

        // Set listeners for buttons
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validate inputs
                if (!validateFields()) return;

                // Get filter JSON
                String filter = compileFilter();
                if (filter==null) {
                    Toast.makeText(getApplicationContext(),FILTER_COMPILE_ERROR,Toast.LENGTH_SHORT);
                    return;
                }

                // TODO: Add functionality to retrieve user list

                // TODO: Remove this filler code
                // -----START OF CODE TO BE REMOVED-----

                // Show Toast indicating that function is not implemented
                String toastMessage = LIST_NOT_IMPLEMENTED_TEMP_BASE + "\n"
                        + filter;
                Toast.makeText(getApplicationContext(),toastMessage,Toast.LENGTH_LONG).show();

                // -----END OF CODE TO BE REMOVED-----
            }
        });

        // Return to main menu
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Validate editText inputs if the corresponding checkboxes are checked
    // Return true only if all selected fields are valid
    private boolean validateFields() {

        if (nameCheckBox.isChecked()) {
            if (!Pattern.matches(CommonValues.NAME_REGEX,nameEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(),CommonValues.INVALID_NAME_ERROR,Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (idCheckBox.isChecked()) {
            if (!Pattern.matches(CommonValues.ID_REGEX,idEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(),CommonValues.INVALID_ID_ERROR,Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (yearCheckBox.isChecked()) {
            if (!Pattern.matches(CommonValues.YEAR_REGEX,yearEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(),CommonValues.INVALID_YEAR_ERROR,Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private String compileFilter() {
        JSONObject filterJson = new JSONObject();
        try {
            if (nameCheckBox.isChecked()) filterJson.put("name",nameEditText.getText().toString());
            if (idCheckBox.isChecked()) filterJson.put("id",Integer.valueOf(idEditText.getText().toString()));
            if (yearCheckBox.isChecked()) filterJson.put("year",Integer.valueOf(yearEditText.getText().toString()));
            if (vacCheckBox.isChecked()) filterJson.put("vaccinated",vacSpinner.getSelectedItem().toString().equals(vacSpinnerDefault));
            if (undergradCheckBox.isChecked()) filterJson.put("undergraduate",undergradSpinner.getSelectedItem().toString().equals(undergradSpinnerDefault));
        } catch (JSONException e) {
            return null;
        }
        return filterJson.toString();
    }

}