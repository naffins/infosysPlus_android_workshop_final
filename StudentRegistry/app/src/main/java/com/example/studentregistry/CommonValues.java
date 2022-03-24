package com.example.studentregistry;


public class CommonValues {
    // Patterns for validation
    // Source for name regex: https://stackoverflow.com/questions/15145659/what-do-i-and-i-in-regex-mean
    public final static String NAME_REGEX = "^[a-zA-Z ,.'-]+$";
    public final static String ID_REGEX = "^[1-9][0-9]{6}$";
    public final static String YEAR_REGEX = "^[1-2][0-9]{3}$";

    // Toast strings
    public final static String INVALID_NAME_ERROR = "Error: invalid name";
    public final static String INVALID_ID_ERROR = "Error: invalid student ID";
    public final static String INVALID_YEAR_ERROR = "Error: invalid matriculation year";
    public final static String MALFORMED_URL_ERROR = "Error: Malformed URL";
}
