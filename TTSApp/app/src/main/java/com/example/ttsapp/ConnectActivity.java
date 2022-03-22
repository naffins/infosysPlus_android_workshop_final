package com.example.ttsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class ConnectActivity extends AppCompatActivity {

    // Regex pattern to check if given string is an IP address
    // Source: https://www.oreilly.com/library/view/regular-expressions-cookbook/9780596802837/ch07s16.html
    private final static String IP_ADDRESS_REGEX = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private final static String INVALID_IP = "Error: Invalid IP address";
    private final static String CONNECT_NOT_IMPLEMENTED_TEMP = "Connect function not implemented; skipping";

    // Variables for IP address field and connect button
    private EditText ipAddressEditText;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        // Get references to IP address field
        ipAddressEditText = findViewById(R.id.activity_connect_ip_field);

        // Get reference to connect button
        connectButton = findViewById(R.id.activity_connect_button);

        // Set listener for connect button using anonymous class
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Get input IP address
                String inputIpAddress = ipAddressEditText.getText().toString();

                // Test if input string is valid IP address
                if (Pattern.matches(IP_ADDRESS_REGEX,inputIpAddress)) {

                    // TODO: Implement code which connects to REST API

                    // TODO: Remove this filler code
                    // -----START OF CODE TO BE REMOVED-----

                    // Show Toast indicating that function is not implemented
                    Toast.makeText(getApplicationContext(), CONNECT_NOT_IMPLEMENTED_TEMP,Toast.LENGTH_SHORT).show();

                    // Move to main menu activity
                    startActivity(new Intent(ConnectActivity.this,MainMenuActivity.class));

                    // -----END OF CODE TO BE REMOVED-----

                }
                else {
                    // Show Toast indicating invalid IP address
                    Toast.makeText(getApplicationContext(),INVALID_IP,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}