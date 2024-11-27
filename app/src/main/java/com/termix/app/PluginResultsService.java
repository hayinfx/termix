package com.termix.app;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.termix.shared.termux.TermuxConstants.TERMUX_APP.TERMUX_SERVICE;
import com.termix.app.DialogActivity;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import java.io.IOException;

public class PluginResultsService extends IntentService {

    public static final String EXTRA_EXECUTION_ID = "execution_id";
    private static int EXECUTION_ID = 1000;
    public static final String PLUGIN_SERVICE_LABEL = "PluginResultsService";
    private static final String LOG_TAG = "PluginResultsService";

    public PluginResultsService() {
        super(PLUGIN_SERVICE_LABEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;

        // Log message for debugging
        Log.d(LOG_TAG, PLUGIN_SERVICE_LABEL + " received execution result");

        // Retrieve the result bundle from the intent
        final Bundle resultBundle = intent.getBundleExtra(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE);
        if (resultBundle == null) {
            Log.e(LOG_TAG, "The intent does not contain the result bundle at the \"" + TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE + "\" key.");
            return;
        }

        final int executionId = intent.getIntExtra(EXTRA_EXECUTION_ID, 0);

        // Create log output
        String resultLog = "Execution id " + executionId + " result:\n" +
                "stdout:\n```\n" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT, "") + "\n```\n" +
                "stdout_original_length: `" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT_ORIGINAL_LENGTH, "") + "`\n" +
                "stderr:\n```\n" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_STDERR, "") + "\n```\n" +
                "stderr_original_length: `" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_STDERR_ORIGINAL_LENGTH, "") + "`\n" +
                "exitCode: `" + resultBundle.getInt(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_EXIT_CODE) + "`\n" +
                "errCode: `" + resultBundle.getInt(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_ERR) + "`\n" +
                "errmsg: `" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_ERRMSG, "") + "`";

        // Log to Logcat
        Log.d(LOG_TAG, resultLog);

        /**
        // Send an Intent to launch the DialogActivity and show the dialog
        Intent dialogIntent = new Intent(context, DialogActivity.class);
        dialogIntent.putExtra("toastMessage", resultLog);  // Pass the log message to DialogActivity
        context.startActivity(dialogIntent);  // Start DialogActivity
        **/

        
        Intent dialogIntent = new Intent(getApplicationContext(), DialogActivity.class);
        dialogIntent.putExtra("toastMessage", resultLog);  // Pass the log message
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // Add the NEW_TASK flag
        getApplicationContext().startActivity(dialogIntent);  // Start DialogActivity
        

        // Perform other tasks...
        
    }

    // Utility method to get next execution id
    public static synchronized int getNextExecutionId() {
        return EXECUTION_ID++;
    }

/**
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // Perform any cleanup actions here when the service is destroyed
        Log.d(LOG_TAG, "Service is being destroyed");

        // Example: Stop Node.js server or other cleanup actions
        stopNodeServer();

        // Show a toast message on the main thread
        showToast("Service has been destroyed and Node.js server stopped.");
    }

    private void stopNodeServer() {
        try {
            Runtime.getRuntime().exec("pkill node");  // Example for stopping Node.js server
            Log.d(LOG_TAG, "Node.js server stopped.");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to stop Node.js server", e);
        }
    }

    private void showToast(final String message) {
        // Make sure the Toast runs on the main thread (UI thread)
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PluginResultsService.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    **/
}

/**
public class PluginResultsService extends IntentService {

    public static final String EXTRA_EXECUTION_ID = "execution_id";

    private static int EXECUTION_ID = 1000;

    public static final String PLUGIN_SERVICE_LABEL = "PluginResultsService";

    private static final String LOG_TAG = "PluginResultsService";

    public PluginResultsService(){
        super(PLUGIN_SERVICE_LABEL);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;

        Log.d(LOG_TAG, PLUGIN_SERVICE_LABEL + " received execution result");

        final Bundle resultBundle = intent.getBundleExtra(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE);
        if (resultBundle == null) {
            Log.e(LOG_TAG, "The intent does not contain the result bundle at the \"" + TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE + "\" key.");
            return;
        }

        final int executionId = intent.getIntExtra(EXTRA_EXECUTION_ID, 0);

        Log.d(LOG_TAG, "Execution id " + executionId + " result:\n" +
                "stdout:\n```\n" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT, "") + "\n```\n" +
                "stdout_original_length: `" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT_ORIGINAL_LENGTH) + "`\n" +
                "stderr:\n```\n" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_STDERR, "") + "\n```\n" +
                "stderr_original_length: `" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_STDERR_ORIGINAL_LENGTH) + "`\n" +
                "exitCode: `" + resultBundle.getInt(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_EXIT_CODE) + "`\n" +
                "errCode: `" + resultBundle.getInt(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_ERR) + "`\n" +
                "errmsg: `" + resultBundle.getString(TERMUX_SERVICE.EXTRA_PLUGIN_RESULT_BUNDLE_ERRMSG, "") + "`");
    }

    public static synchronized int getNextExecutionId() {
        return EXECUTION_ID++;
    }

}

**/