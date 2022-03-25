package com.example.studentregistry;


import android.os.Handler;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

// TODO: Complete this class (to be guided)
public class RequestHelper extends Thread {

    // Interface which we will use to indicate what to do after a request is done,
    // on the main UI thread (hence why we need a Handler)
    public static interface PostRequestTask {

        // TO REMOVE IN STARTER CODE
        public void postRequestExecute(String jsonOutput, int responseCode);
    }

    private static String ipAddress = null;

    // Save IP address so that we consistently include it in our URL to be accessed
    public static void setIpAddress(String ipAddress) {
        RequestHelper.ipAddress = ipAddress;
    }

    // =====START OF CODE TO BE REMOVED IN STARTER VERSION=====

    private final String path;
    private final String jsonInput;
    private final String method;

    // 'volatile' means that latest values of the variables are always visible to all threads
    // Normally, given threads executing in separate CPU cores, each thread caches its own version
    // of variables which may not be up to date (you will learn more about this in Term 5 ESC)
    private volatile String jsonOutput;
    // We set this to -1 as an indicator of error (if an exception happens such that the status code
    // is not retrieved)
    private volatile int responseCode = -1;
    private PostRequestTask postRequestTask;
    private Handler handler;

    // Note that jsonInput should, especially when method is not "GET", not be null
    public RequestHelper(String path, String jsonInput, String method, PostRequestTask postRequestTask) {
        assert jsonInput!=null;
        this.path = path;
        this.jsonInput = jsonInput;
        this.method = method;
        this.postRequestTask = postRequestTask;

        // We write this code, which is to execute in the main thread, so that the handler
        // sets the main thread as the destination for tasks (see Handler notes around end of run() code)
        this.handler = new Handler();
    }

    // Thread's main program flow
    public void run() {

        // We define our variables here so that, in the case of exceptions,
        // we can close these handles/buffers easily
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter writer = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {

            // Setup connection to URL with indicated path
            HttpURLConnection connection = (HttpURLConnection) new URL("http://"+ipAddress+":8000"+path).openConnection();

            // Indicate HTTP request method eg. GET
            connection.setRequestMethod(method);

            // Indicate that we accept JSON as reply
            connection.setRequestProperty("Accept", "application/json");
            
            // Indicate that we intend to read the response body
            connection.setDoInput(true);

            // If not a GET request, include a request body
            if (!method.equals("GET")) {

                // Indicate content type as JSON
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                // Allow sending of content/receipt of content
                connection.setDoOutput(true);

                // Get output stream and writers
                outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                writer = new BufferedWriter(outputStreamWriter);

                // Write the request body content and flush to connection
                // The flushing ensures that the content is sent out
                // from the buffer
                writer.write(jsonInput);
                writer.flush();

                // Close our request body-related handles/buffers
                writer.close();
                writer = null;
                outputStreamWriter.close();
                outputStreamWriter = null;
                outputStream.close();
                outputStream = null;
            }


            // Perform connection
            connection.connect();

            // Get response code
            responseCode = connection.getResponseCode();

            // Get input stream if no error, or error stream if response code is an error code
            inputStream = responseCode<400? connection.getInputStream() : connection.getErrorStream();

            // Get a StringBuilder which we will use to incrementally store our response body
            StringBuilder output = new StringBuilder("");

            // Get reader for input stream (with reply from server)
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            // Keep reading from input stream (ignoring line-terminating characters like '\n')
            // Once we reach the end of the stream, trying to read more lines returns null
            String read = reader.readLine();
            while (read != null) {
                output.append(read);
                read = reader.readLine();
            }

            // Close reader
            // Close response body-related handles and buffers
            reader.close();
            reader = null;
            inputStreamReader.close();
            inputStreamReader = null;
            inputStream.close();
            inputStream = null;
            
            // Assign string to be processed after connection is done
            jsonOutput = output.toString();

            // Close connection
            connection.disconnect();
        }
        catch (Exception e) {

            // Post runnable to print errors (see below on notes for Handler)
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("PostHelper","Error: could not execute request");
                    Log.e("PostHelper","Response code: " + Integer.toString(responseCode));
                    e.printStackTrace();
                }
            });
            jsonOutput = null;

            try {
                // Close existing streams/handles to avoid memory leaks
                // This may not be comprehensive enough though
                if (outputStream!=null) outputStream.close();
                if (outputStreamWriter!=null) outputStreamWriter.close();
                if (writer!=null) writer.close();
                if (inputStream!=null) inputStream.close();
                if (inputStreamReader!=null) inputStreamReader.close();
                if (reader!=null) reader.close();
            }
            catch (Exception f) {
                // Attempt to print error trace in main thread, though
                // we are unlikely to ever reach this part
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        f.printStackTrace();
                    }
                });
            }
        }

        // Do some post-request stuff
        // We post a Runnable to a Handler object initialized in the main thread,
        // so that instead of executing code in the connection thread
        // the code inside run() is done in main thread instead
        handler.post(new Runnable() {

            // Abstract method to be implemented when extending Runnable
            public void run() {

                // Process the response body and response code
                postRequestTask.postRequestExecute(jsonOutput,responseCode);
            }
        });

        // =====END OF CODE TO BE REMOVED IN STARTER VERSION=====
    }
}

