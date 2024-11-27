package com.termix.app; // Import the DebugGobbler class


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class DebugGobbler extends Thread {
    private InputStream inputStream;
    private String tag;

    public DebugGobbler(String tag, InputStream inputStream) {
        this.inputStream = inputStream;
        this.tag = tag;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(tag + ": " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}