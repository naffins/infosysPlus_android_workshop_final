package com.example.ttsapp;

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
public class PostHelper extends Thread {

    public static interface PostRequestTask {

        // TO REMOVE IN STARTER CODE
        public void postRequestExecute(String jsonOutput);
    }

    // =====START OF CODE TO BE REMOVED IN STARTER VERSION=====
    private static String ipAddress = null;

    private final String path;
    private final String jsonInput;
    private volatile String jsonOutput;
    private PostRequestTask postRequestTask;
    private Handler handler;

    public PostHelper(String path, String jsonInput, PostRequestTask postRequestTask) {
        this.path = path;
        this.jsonInput = jsonInput;
        this.postRequestTask = postRequestTask;
        this.handler = new Handler();
    }

    public static void setIpAddress(String ipAddress) {
        PostHelper.ipAddress = ipAddress;
    }

    // Thread's main program flow
    public void run() {

        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter writer = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {

            // Setup connection to URL
            HttpURLConnection connection = (HttpURLConnection) new URL("http://"+ipAddress+":9000"+path).openConnection();

            // Indicate method as POST
            connection.setRequestMethod("POST");

            // Indicate content type as JSON
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            // Indicate that we accept JSON as reply
            connection.setRequestProperty("Accept", "application/json");

            // Allow sending of content/receipt of content
            connection.setDoOutput(true);
            connection.setDoInput(true);


            outputStream = new BufferedOutputStream(connection.getOutputStream());
            outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");

            writer = new BufferedWriter(outputStreamWriter);
            writer.write(jsonInput);
            writer.flush();
            writer.close();
            writer = null;
            outputStreamWriter.close();
            outputStreamWriter = null;
            outputStream.close();
            outputStream = null;

            connection.connect();

            inputStream = connection.getInputStream();
            String output = "";

            if (inputStream != null) {
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                String read = reader.readLine();
                while (read != null) {
                    output += read;
                    read = reader.readLine();
                }
                reader.close();
                reader = null;
                inputStreamReader.close();
                inputStreamReader = null;
                inputStream.close();
                inputStream = null;
            }
            jsonOutput = output;
        }
        catch (Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("PostHelper","Error: could not execute request");
                    e.printStackTrace();
                }
            });
            jsonOutput = null;

            try {
                if (outputStream!=null) outputStream.close();
                if (outputStreamWriter!=null) outputStreamWriter.close();
                if (writer!=null) writer.close();
                if (inputStream!=null) inputStream.close();
                if (inputStreamReader!=null) inputStreamReader.close();
                if (reader!=null) reader.close();
            }
            catch (Exception f) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        f.printStackTrace();
                        //Log.e("PostHelper", Arrays.toString(f.getStackTrace()));
                    }
                });
            }
        }

        handler.post(new Runnable() {
            public void run() {
                postRequestTask.postRequestExecute(jsonOutput);
            }
        });

        // =====END OF CODE TO BE REMOVED IN STARTER VERSION=====
    }
}

