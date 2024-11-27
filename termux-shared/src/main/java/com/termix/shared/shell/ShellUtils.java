package com.termix.shared.shell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.termix.shared.file.FileUtils;
import com.termix.terminal.TerminalBuffer;
import com.termix.terminal.TerminalEmulator;
import com.termix.terminal.TerminalSession;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShellUtils {

    /** Get process id of {@link Process}. */
    public static int getPid(Process p) {
        try {
            Field f = p.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            try {
                return f.getInt(p);
            } finally {
                f.setAccessible(false);
            }
        } catch (Throwable e) {
            return -1;
        }
    }

    /** Setup shell command arguments for the execute. */
    @NonNull
    public static String[] setupShellCommandArguments(@NonNull String executable, @Nullable String[] arguments) {
        List<String> result = new ArrayList<>();
        result.add(executable);
        if (arguments != null) Collections.addAll(result, arguments);
        return result.toArray(new String[0]);
    }


/** 
 * Setup shell command arguments for the execute.
 * This method initializes the executable and arguments with the required proot command.
 */
 /**
@NonNull
public static String[] setupShellCommandArguments(@NonNull String executable, @Nullable String[] arguments) {
    List<String> result = new ArrayList<>();

    // Include proot in the executable path and add initial required arguments
    result.add("/data/data/com.termix/files/proot");
    result.add("-0");
    result.add("-r");
    result.add("/data/data/com.termix/files/rootfs");
    result.add("--link2symlink");
    result.add("-b");
    result.add("/data/data/com.termix/files");
    result.add("-b");
    result.add("/dev");
    result.add("-b");
    result.add("/proc");
    result.add("-b");
    result.add("/sdcard");
    result.add("-w");
    result.add("/root");

    // Add the original executable as the command to run within proot
    result.add(executable);

    // If additional arguments are provided, add them as well
    if (arguments != null) {
        Collections.addAll(result, arguments);
    }

    return result.toArray(new String[0]);
}
**/

    /** Get basename for executable. */
    @Nullable
    public static String getExecutableBasename(@Nullable String executable) {
        return FileUtils.getFileBasename(executable);
    }



    /** Get transcript for {@link TerminalSession}. */
    public static String getTerminalSessionTranscriptText(TerminalSession terminalSession, boolean linesJoined, boolean trim) {
        if (terminalSession == null) return null;

        TerminalEmulator terminalEmulator = terminalSession.getEmulator();
        if (terminalEmulator == null) return null;

        TerminalBuffer terminalBuffer = terminalEmulator.getScreen();
        if (terminalBuffer == null) return null;

        String transcriptText;

        if (linesJoined)
            transcriptText = terminalBuffer.getTranscriptTextWithFullLinesJoined();
        else
            transcriptText = terminalBuffer.getTranscriptTextWithoutJoinedLines();

        if (transcriptText == null) return null;

        if (trim)
            transcriptText = transcriptText.trim();

        return transcriptText;
    }

}
