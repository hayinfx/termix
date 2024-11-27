package com.termix.app;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.app.PendingIntent;
import android.os.Build;
import androidx.annotation.Nullable; // If you’re using annotations
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AlertDialog;
import com.termix.R;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import android.view.Window;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.termix.app.RunCommandService;
import android.content.Intent;


import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface;
import androidx.core.app.NotificationCompat;
import android.os.Build;
import android.widget.Toast;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termix_activity_dialog); // Set your layout here

        // Retrieve the message passed in the intent
        String message = getIntent().getStringExtra("toastMessage");

        // Display the dialog with the message
        if (message != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish(); // Close the activity after dismissing the dialog
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}