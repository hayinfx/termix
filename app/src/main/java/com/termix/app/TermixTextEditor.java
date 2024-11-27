package com.termix.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.termix.R;

import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TermixTextEditor extends Activity {

    private CodeEditor codeEditor;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termix_text_editor);


    // Set filePath (adjust as needed)
    filePath = getIntent().getStringExtra("filePath");
    if (filePath == null) {
        Toast.makeText(this, "No file path provided", Toast.LENGTH_SHORT).show();
        return;
    }

 
        // Initialize CodeEditor and configure it
        codeEditor = findViewById(R.id.editor); // Make sure R.id.editor is a CodeEditor view in XML
        codeEditor.setEditorLanguage(new JavaLanguage()); // Set JavaScript syntax highlighting

        // Optional: Apply a dark theme to the editor
        codeEditor.setColorScheme(new EditorColorScheme()); // Adjust colors as needed

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            if (filePath != null) {
                saveFileContent(new File(filePath));
            }
        });

        // Load file content if filePath is provided
        if (filePath != null) {
            loadFileContent(new File(filePath));
        }
               
    }

@Override
protected void onDestroy() {
    super.onDestroy();
    if (codeEditor != null) {
        codeEditor.release();
    }
}        

    private void loadFileContent(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);

            // Set the text content in the editor
            codeEditor.setText(new String(buffer));

            // Clear undo/redo history after loading new content
         //   codeEditor.getUndoManager().clearHistory();

        } catch (IOException e) {
            Toast.makeText(this, "Error loading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFileContent(File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(codeEditor.getText().toString().getBytes()); // Save editor text to file
            Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }
    
}

