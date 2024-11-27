package com.termix.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.termix.R;
import android.view.ViewGroup;
import java.util.Arrays;
import android.widget.ImageView;
public class TermixFileManager extends AppCompatActivity {

    private TextView textCurrentDirectory;
    private Button buttonUpDirectory, buttonCreateFile, buttonCreateDir;
    private EditText editTextFileName;
    private RecyclerView recyclerViewFiles;
    private File currentDirectory;
    private FileAdapter fileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termix_file_manager);

        textCurrentDirectory = findViewById(R.id.text_current_directory);
        buttonUpDirectory = findViewById(R.id.button_up_directory);
        recyclerViewFiles = findViewById(R.id.recycler_view_files);
        buttonCreateFile = findViewById(R.id.button_create_file);
        buttonCreateDir = findViewById(R.id.button_create_directory);
        editTextFileName = findViewById(R.id.edit_text_file_name);

        recyclerViewFiles.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter(new ArrayList<>());
        recyclerViewFiles.setAdapter(fileAdapter);

    //    currentDirectory = getFilesDir();
      //  listFiles(currentDirectory);

// Set currentDirectory to the "home" folder inside the app's files directory
File currentDirectory = new File(getFilesDir(), "home/app");
// List the files in the directory
listFiles(currentDirectory);

        buttonUpDirectory.setOnClickListener(v -> navigateUp());
        buttonCreateFile.setOnClickListener(v -> createNewFile());
        buttonCreateDir.setOnClickListener(v -> createNewDirectory());
    }

    private void listFiles(File directory) {
        textCurrentDirectory.setText("Current Directory: " + directory.getPath());
        buttonUpDirectory.setEnabled(!directory.equals(getFilesDir()));

        File[] files = directory.listFiles();
        if (files != null) {
            List<File> fileList = new ArrayList<>(Arrays.asList(files));
            fileAdapter.setFiles(fileList);
        } else {
            Toast.makeText(this, "Cannot access files in this directory.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateUp() {
        if (!currentDirectory.equals(getFilesDir())) {
            currentDirectory = currentDirectory.getParentFile();
            listFiles(currentDirectory);
        }
    }

    private void openDirectory(File directory) {
        if (directory.isDirectory()) {
            currentDirectory = directory;
            listFiles(directory);
        }
    }

    private void createNewFile() {
        String fileName = editTextFileName.getText().toString().trim();
        if (!fileName.isEmpty()) {
            File newFile = new File(currentDirectory, fileName);
            try {
                if (newFile.createNewFile()) {
                    Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();
                    listFiles(currentDirectory);
                } else {
                    Toast.makeText(this, "File already exists", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Failed to create file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "File name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewDirectory() {
        String dirName = editTextFileName.getText().toString().trim();
        if (!dirName.isEmpty()) {
            File newDir = new File(currentDirectory, dirName);
            if (newDir.mkdir()) {
                Toast.makeText(this, "Directory created successfully", Toast.LENGTH_SHORT).show();
                listFiles(currentDirectory);
            } else {
                Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Directory name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFile(File file) {
        if (file.exists() && file.delete()) {
            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
            listFiles(currentDirectory);
        } else {
            Toast.makeText(this, "Failed to delete file", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null && files.length == 0 && directory.delete()) {
                Toast.makeText(this, "Directory deleted", Toast.LENGTH_SHORT).show();
                listFiles(currentDirectory);
            } else {
                Toast.makeText(this, "Directory is not empty or could not be deleted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Directory does not exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void renameFileOrDirectory(File oldFile, String newName) {
        File newFile = new File(oldFile.getParent(), newName);
        if (oldFile.renameTo(newFile)) {
            Toast.makeText(this, "Renamed successfully", Toast.LENGTH_SHORT).show();
            listFiles(currentDirectory);
        } else {
            Toast.makeText(this, "Failed to rename", Toast.LENGTH_SHORT).show();
        }
    }

    private class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

        private List<File> files;

        public FileAdapter(List<File> files) {
            this.files = files;
        }

        public void setFiles(List<File> files) {
            this.files = files;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new FileViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
    File file = files.get(position);
    holder.textView.setText(file.getName());

    // Set the folder icon if it's a directory
    if (file.isDirectory()) {
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.open_black_folder, 0, 0, 0);
    } else {
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); // Clear icon for non-directory files
    }

    holder.itemView.setOnClickListener(v -> {
        if (file.isDirectory()) {
            openDirectory(file);
        } else if (isSupportedFile(file)) {
            openTextEditor(file);
        } else {
            Toast.makeText(TermixFileManager.this, "Selected file: " + file.getName(), Toast.LENGTH_SHORT).show();
        }
    });

    holder.itemView.setOnLongClickListener(v -> {
        showOptionsDialog(file);
        return true;
    });
}

        @Override
        public int getItemCount() {
            return files.size();
        }

        class FileViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public FileViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }

    private boolean isSupportedFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".txt") || fileName.endsWith(".js") || fileName.endsWith(".ejs") || fileName.endsWith(".html");
    }
/**
    private void openTextEditor(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(intent, "Open with"));
    }
**/
private void openTextEditor(File file) {
    Intent intent = new Intent(this, TermixTextEditor.class);
    intent.putExtra("filePath", file.getAbsolutePath());
    startActivity(intent);
}

    private void showOptionsDialog(File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File Options");

        String[] options = {"Rename", "Delete", "Create File", "Create Directory"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    showRenameDialog(file);
                    break;
                case 1:
                    showDeleteConfirmationDialog(file);
                    break;
                case 2:
                    createNewFile();
                    break;
                case 3:
                    createNewDirectory();
                    break;
            }
        });

        builder.show();
    }

    private void showRenameDialog(File file) {
        final EditText input = new EditText(this);
        input.setText(file.getName());

        AlertDialog.Builder renameDialog = new AlertDialog.Builder(this);
        renameDialog.setTitle("Rename");
        renameDialog.setMessage("Enter new name:");
        renameDialog.setView(input);

        renameDialog.setPositiveButton("Rename", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                renameFileOrDirectory(file, newName);
            } else {
                Toast.makeText(TermixFileManager.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        renameDialog.setNegativeButton("Cancel", null);
        renameDialog.show();
    }

    private void showDeleteConfirmationDialog(final File file) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Delete File");
        deleteDialog.setMessage("Are you sure you want to delete " + file.getName() + "?");

        deleteDialog.setPositiveButton("Delete", (dialog, which) -> {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                deleteFile(file);
            }
        });

        deleteDialog.setNegativeButton("Cancel", null);
        deleteDialog.show();
    }
}