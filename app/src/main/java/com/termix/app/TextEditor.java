package com.termix.app;
import com.termix.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextEditor extends AppCompatActivity {

    private static final String PREFS_NAME = "TextEditorPrefs";
    private static final String KEY_FILE_PATH = "filePath";
//    private ScrollableTextView scrollableTextView;
    
    private SharedPreferences sharedPreferences; // To store the file path permanently
    private EditText editTextFilePath; // EditText for user input file path

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor); // Set the appropriate layout

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize EditText for file path input
        editTextFilePath = findViewById(R.id.editTextFilePath);

        // Load and display the saved file path
        String savedPath = sharedPreferences.getString(KEY_FILE_PATH, "");
        editTextFilePath.setText(savedPath); // Set saved path in EditText

        // Initialize the ScrollableTextView
//        scrollableTextView = findViewById(R.id.scrollableTextView);

        // Add multiple TextView boxes dynamically
    //    for (int i = 0; i < 5; i++) { // Example: Add 5 text views
        //    scrollableTextView.addTextBox("Text Box " + (i + 1));           
     //   }
     
     /**
           scrollableTextView.addTextBox("/data/data/com.hayinfx.terminone/files/");        
           scrollableTextView.addTextBox("/data/data/com.hayinfx.terminone/files/rootfs/");        
          scrollableTextView.addTextBox("/data/data/com.hayinfx.terminone/files/rootfs/etc/ssh/sshd_config");           
          scrollableTextView.addTextBox("");           
          scrollableTextView.addTextBox("");           
          scrollableTextView.addTextBox("");           
          scrollableTextView.addTextBox("");           
          scrollableTextView.addTextBox("");                     
          scrollableTextView.addTextBox("");            
          scrollableTextView.addTextBox("");                    
          
          **/
     
    }

    public void onOpenEditorButtonClick(View view) {
        String currentPath = editTextFilePath.getText().toString().trim();
        if (!currentPath.isEmpty()) {
            openEditDialog(currentPath); // Pass current file path to the dialog
        } else {
            Toast.makeText(this, "Please enter a file path", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSavePathButtonClick(View view) {
        String newPath = editTextFilePath.getText().toString().trim();
        if (!newPath.isEmpty()) {
            saveFilePath(newPath); // Save the path to SharedPreferences
            Toast.makeText(this, "File path saved: " + newPath, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "File path cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void openEditDialog(String currentPath) {
        // Create an EditText to take input for editing file content
        final EditText input = new EditText(this);
        
        // Read existing file content to display in EditText
        String currentContent = readFileContent(currentPath);
        input.setText(currentContent); // Pre-fill with current file content

        // Build the dialog
        new AlertDialog.Builder(this)
            .setTitle("Edit File Content")
            .setView(input)
            .setPositiveButton("Save", (dialog, which) -> {
                String newContent = input.getText().toString().trim();
                if (!newContent.isEmpty()) {
                    saveFileContent(currentPath, newContent); // Save the new content to the file
                } else {
                    Toast.makeText(TextEditor.this, "Content cannot be empty", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void saveFileContent(String path, String content) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(content);
            Toast.makeText(this, "File content saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String readFileContent(String path) {
        StringBuilder content = new StringBuilder();
        try {
            File file = new File(path);
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append("\n");
                }
                scanner.close();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error reading file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return content.toString();
    }

    private void saveFilePath(String path) {
        // Save the file path in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FILE_PATH, path);
        editor.apply(); // Apply changes asynchronously
    }
}