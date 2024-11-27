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
import com.termix.shared.termux.TermuxConstants;
import com.termix.shared.termux.TermuxConstants.TERMUX_APP.RUN_COMMAND_SERVICE;

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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import android.os.Handler;

import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class AppHomeHtmlLoader extends AppCompatActivity {
    private BroadcastReceiver destroyReceiver;
    WebView webView;
    
    private Context mContext;

    // Default constructor (required by Android system)
    public AppHomeHtmlLoader() {
        // Empty constructor, required for system instantiation
    }
    
    // Constructor, pass the context when initializing this class
    public AppHomeHtmlLoader(Context context) {
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   startNodeJsServer();
     
     // Check if port 3000 is in use
if (!isPortInUse(3000)) {
	showLoadingMessage("Starting server please wait..");
    // Port is free, start the Node.js server
    startNodeJsServer1();
} 

   requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide title
    getSupportActionBar().hide(); // Hide action bar        
        setContentView(R.layout.app_home_html_loader);

        webView = findViewById(R.id.webView);

        // Set up the WebView
        webView.setWebViewClient(new CustomWebViewClient()); // Use custom WebViewClient
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setJavaScriptEnabled(true);
        
    // Enable cookies
    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.setAcceptCookie(true);
    cookieManager.setAcceptThirdPartyCookies(webView, true); // Optional for third-party cookies
    
// Enable on-device site data
webSettings.setDomStorageEnabled(true); // Enable DOM storage (HTML5 local storage)
webSettings.setDatabaseEnabled(true); // Enable database storage

// Set cache mode (use LOAD_DEFAULT or LOAD_CACHE_ELSE_NETWORK as needed)
webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

WebView.setWebContentsDebuggingEnabled(true);

            loadAnyHtmlFile();

      
            // Load any HTML file from the app directory
    //    loadAnyHtmlFile5();
        
        // Add JavaScript Interface
        webView.addJavascriptInterface(new WebAppInterface(), "AndroidInterface");
    }


// CustomWebViewClient to handle URL loading
private class CustomWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // Load the URL in the WebView instead of launching a new browser
        view.loadUrl(url);
        return true; // Indicates that the host application wants to handle the URL
    }
}

    // Inner class to expose to JavaScript
    private class WebAppInterface {
    	    
       //     @android.webkit.JavascriptInterface
                // Example usage for Node.js
  //  public void stopNodeJsServer() {
   //     stopCommand1();
         //    }

                

@android.webkit.JavascriptInterface
public void startCommandStart(String commandPath, String[] commandArgs, String workingDir, String commandLabel) {
    // Now commandArgs is correctly passed as a String array
    startCommand3(commandPath, commandArgs, workingDir, commandLabel);
}

/**
@android.webkit.JavascriptInterface
    public void termixFileManager() {
        if (mContext != null) {
            // Use the passed context to start the activity
            Intent intent = new Intent(mContext, TermixFileManager.class);
            mContext.startActivity(intent);
        }
    }
  **/

@android.webkit.JavascriptInterface
public void termixFileManager() {
    // Directly start the activity without using mContext
    Intent intent = new Intent(getApplicationContext(), TermixFileManager.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // Add flag to start activity from a non-Activity class
    getApplicationContext().startActivity(intent);
}
      
        @android.webkit.JavascriptInterface        
    // Example usage for Node.js
    /**
    public void stopNodeJsServer() {
startCommand1("/data/data/com.termix/files/usr/bin/sh", 
              new String[]{"-c", "pkill node"}, 
              "/data/data/com.termix/files/home/app", 
              "Stop Node JS Server");
    }            
    **/
    public void runCommand(String commandPath, String commandArgs, String workingDir, String commandLabel) {
        runCommand1(commandPath, 
                      new String[]{"-c", commandArgs}, 
                      workingDir, 
                      commandLabel);
    }
    


        @android.webkit.JavascriptInterface        
    // Example usage for Node.js
    public void startNodeJsServer1() {
        runCommand1("/data/data/com.termix/files/usr/bin/node", 
                      new String[]{"home.js"}, 
                      "/data/data/com.termix/files/home/app", 
                      "Node.js Server");
    }        
        
                @android.webkit.JavascriptInterface
                
        public void startNodeJsCommand() {
            Intent intent = new Intent();
            intent.setClassName("com.termix", "com.termix.app.RunCommandService");
            intent.setAction("com.termix.RUN_COMMAND");
            intent.putExtra("com.termix.RUN_COMMAND_PATH", "/data/data/com.termix/files/usr/bin/top");
            intent.putExtra("com.termix.RUN_COMMAND_ARGUMENTS", new String[]{"-n", "20000"});
            intent.putExtra("com.termix.RUN_COMMAND_WORKDIR", "/data/data/com.termix/files/home");
            intent.putExtra("com.termix.RUN_COMMAND_BACKGROUND", true);
            intent.putExtra("com.termix.RUN_COMMAND_LABEL", "top command");

            Intent pluginResultsServiceIntent = new Intent(AppHomeHtmlLoader.this, PluginResultsService.class);
            int executionId = PluginResultsService.getNextExecutionId();
            pluginResultsServiceIntent.putExtra(PluginResultsService.EXTRA_EXECUTION_ID, executionId);

            PendingIntent pendingIntent = PendingIntent.getService(AppHomeHtmlLoader.this, executionId,
                    pluginResultsServiceIntent,
                    PendingIntent.FLAG_ONE_SHOT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0));
            intent.putExtra("com.termix.RUN_COMMAND_PENDING_INTENT", pendingIntent);

            try {
                startService(intent);
                Toast.makeText(AppHomeHtmlLoader.this, "Node.js command started", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AppHomeHtmlLoader.this, "Error starting command", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
   /**
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Get the result from the service
        String commandResult = intent.getStringExtra("commandResult");
        if (commandResult != null) {
            // Show the result in a Toast
            Toast.makeText(this, commandResult, Toast.LENGTH_LONG).show();
        }
    }    
    **/
    

    
        public void startNodeJsServer() {
            Intent intent = new Intent();
            intent.setClassName("com.termix", "com.termix.app.RunCommandService");
            intent.setAction("com.termix.RUN_COMMAND");
            intent.putExtra("com.termix.RUN_COMMAND_PATH", "/data/data/com.termix/files/usr/bin/node");
            intent.putExtra("com.termix.RUN_COMMAND_ARGUMENTS", new String[]{
            	
"home.js"

}

);
            intent.putExtra("com.termix.RUN_COMMAND_WORKDIR", "/data/data/com.termix/files/home/app");
            intent.putExtra("com.termix.RUN_COMMAND_BACKGROUND", true);
            intent.putExtra("com.termix.RUN_COMMAND_LABEL", "top command");

            Intent pluginResultsServiceIntent = new Intent(AppHomeHtmlLoader.this, PluginResultsService.class);
            int executionId = PluginResultsService.getNextExecutionId();
            pluginResultsServiceIntent.putExtra(PluginResultsService.EXTRA_EXECUTION_ID, executionId);

            PendingIntent pendingIntent = PendingIntent.getService(AppHomeHtmlLoader.this, executionId,
                    pluginResultsServiceIntent,
                    PendingIntent.FLAG_ONE_SHOT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0));
            intent.putExtra("com.termix.RUN_COMMAND_PENDING_INTENT", pendingIntent);

            try {
                startService(intent);
                Toast.makeText(AppHomeHtmlLoader.this, "Node.js Server started", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AppHomeHtmlLoader.this, "Error starting NodeJS Server", Toast.LENGTH_SHORT).show();
            }
        }    
    

    
public void runCommand1(String commandPath, String[] commandArgs, String workingDir, String commandLabel) {
    Intent intent = new Intent();
    intent.setClassName("com.termix", "com.termix.app.RunCommandService");
    intent.setAction("com.termix.RUN_COMMAND");
    intent.putExtra("com.termix.RUN_COMMAND_PATH", commandPath);
    intent.putExtra("com.termix.RUN_COMMAND_ARGUMENTS", commandArgs);
    intent.putExtra("com.termix.RUN_COMMAND_WORKDIR", workingDir);
    intent.putExtra("com.termix.RUN_COMMAND_BACKGROUND", true);
    intent.putExtra("com.termix.RUN_COMMAND_LABEL", commandLabel);

    // Creating intent to handle result
    Intent pluginResultsServiceIntent = new Intent(AppHomeHtmlLoader.this, PluginResultsService.class);
    int executionId = PluginResultsService.getNextExecutionId();
    pluginResultsServiceIntent.putExtra(PluginResultsService.EXTRA_EXECUTION_ID, executionId);

    // Create a PendingIntent to send back the result
    PendingIntent pendingIntent = PendingIntent.getService(AppHomeHtmlLoader.this, executionId,
            pluginResultsServiceIntent,
            PendingIntent.FLAG_ONE_SHOT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0));
    intent.putExtra("com.termix.RUN_COMMAND_PENDING_INTENT", pendingIntent);

    try {
        boolean serviceStarted = startService(intent) != null;

        // Inform that the command has started
        if (serviceStarted) {
            Toast.makeText(AppHomeHtmlLoader.this, commandLabel + " started", Toast.LENGTH_SHORT).show();
        } else {
            // Failed to start service
            Toast.makeText(AppHomeHtmlLoader.this, "Failed to start " + commandLabel, Toast.LENGTH_SHORT).show();
            return;
        }
    } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(AppHomeHtmlLoader.this, "Error starting " + commandLabel + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        return;
    }
    
        Log.d("runCommand1", "Command Path: " + commandPath);
Log.d("runCommand1", "Command Args: " + commandArgs);
Log.d("runCommand1", "Working Directory: " + workingDir);
Log.d("runCommand1", "Command Label: " + commandLabel);
    
    
}
  
    // Example usage for Node.js
    public void startNodeJsServer1() {
        runCommand1("/data/data/com.termix/files/usr/bin/node", 
                      new String[]{"/data/data/com.termix/files/home/app/home.js"}, 
                      "/data/data/com.termix/files/home/app", 
                      "Node.js Server");
    }
    
    // Example usage for Node.js
    public void stopNodeJsServer() {
 //       stopCommand();
    }
        
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // Go back to the previous page
        } else {
            super.onBackPressed(); // Exit the activity
        }
    }    
    
    
// Method to check if port 3000 is in use
private boolean isPortInUse(int port) {
    ServerSocket socket = null;
    try {
        socket = new ServerSocket(port);
        socket.setReuseAddress(true);
        return false;  // Port is available
    } catch (IOException e) {
        return true;  // Port is already in use
    } finally {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}    

/**
private void loadAnyHtmlFile() {
    // Define the URL to load from your local server
    String url = "http://127.0.0.1:3000/home"; // Change this to the desired file or logic to choose a file

    // Load the URL in the WebView
    webView.loadUrl(url);
}

**/


private static final int MAX_RETRIES = 5;
private int retryCount = 0;

private void loadAnyHtmlFile() {
    String url = "http://127.0.0.1:3000/home";
    
    
    
    webView.setWebViewClient(new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Reset retry count on success
            retryCount = 0;
        //    Toast.makeText(AppHomeHtmlLoader.this, "Page loaded successfully", Toast.LENGTH_SHORT).show();
        
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            // Retry if the retry count is less than the maximum
            if (retryCount < MAX_RETRIES) {
                retryCount++;
                Toast.makeText(AppHomeHtmlLoader.this, "Retrying... (" + retryCount + "/" + MAX_RETRIES + ")", Toast.LENGTH_SHORT).show();
                
                // Retry with a delay
                new Handler().postDelayed(() -> webView.loadUrl(url), 1000 * retryCount); // Exponential delay
            } else {
                // Max retries reached, show failure message
                Toast.makeText(AppHomeHtmlLoader.this, "Failed to load page after " + MAX_RETRIES + " attempts", Toast.LENGTH_SHORT).show();
                retryCount = 0; // Reset retry count for future attempts
            }
        }
    });

    // Start loading the URL
    webView.loadUrl(url);
}

private void showLoadingMessage(String message) {
    // Display a loading message (e.g., update a TextView or show a ProgressBar with the message)
    Toast.makeText(AppHomeHtmlLoader.this, message , Toast.LENGTH_SHORT).show();
}

// Use a method to load any HTML file dynamically if needed
private void loadHtmlFile(String fileName) {
    String url = "http://127.0.0.1:3000/" + fileName; // Assume the file is accessible via this URL
    webView.loadUrl(url);
}

//@android.webkit.JavascriptInterface
     // Updated startCommand1 method to accept String[] for commandArgs
    public void startCommand3(String commandPath, String[] commandArgs, String workingDir, String commandLabel) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.termix", "com.termix.app.RunCommandService");
            intent.setAction("com.termix.RUN_COMMAND");
            intent.putExtra("com.termix.RUN_COMMAND_PATH", commandPath);
            intent.putExtra("com.termix.RUN_COMMAND_ARGUMENTS", commandArgs);  // Use String[] directly
            intent.putExtra("com.termix.RUN_COMMAND_WORKDIR", workingDir);
            intent.putExtra("com.termix.RUN_COMMAND_BACKGROUND", true);
            intent.putExtra("com.termix.RUN_COMMAND_LABEL", commandLabel);

            // Creating intent to handle result
            Intent pluginResultsServiceIntent = new Intent(AppHomeHtmlLoader.this, PluginResultsService.class);
            int executionId = PluginResultsService.getNextExecutionId();
            pluginResultsServiceIntent.putExtra(PluginResultsService.EXTRA_EXECUTION_ID, executionId);

            // Create a PendingIntent to send back the result
            PendingIntent pendingIntent = PendingIntent.getService(AppHomeHtmlLoader.this, executionId,
                    pluginResultsServiceIntent,
                    PendingIntent.FLAG_ONE_SHOT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0));
            intent.putExtra("com.termix.RUN_COMMAND_PENDING_INTENT", pendingIntent);

            // Start the service
            startService(intent);

            Toast.makeText(AppHomeHtmlLoader.this, commandLabel + " started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AppHomeHtmlLoader.this, "Error starting " + commandLabel + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }    
    
    
    
    
}