package com.xayup.toolpad;


import android.app.*;
import android.content.*;
import java.io.*;

 public class TopExceptionHandler implements Thread.UncaughtExceptionHandler
 {
    private Thread.UncaughtExceptionHandler defaultUEH;
    private static Activity app = null;

    public TopExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    public static String getErrorLog(Activity context){
        app = context;
        if (app.getFileStreamPath("stack.trace").exists()) {
			String traceLog = null;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(app.openFileInput("stack.trace")));
				String line = null;
				while ((line = reader.readLine()) != null) {
					traceLog += line + "\n";
				}

			} catch (FileNotFoundException fnfe) {
				// ...
			} catch (IOException ioe) {
				// ...
			}
			app.deleteFile("stack.trace");
			return traceLog;
		}
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler());
		return null;
        
    }
    
    public void uncaughtException(Thread t, Throwable e) {
        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString()+"\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i=0; i<arr.length; i++) {
            report += "    "+arr[i].toString()+"\n";
        }
        report += "-------------------------------\n\n";

        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause

        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if(cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++) {
                report += "    "+arr[i].toString()+"\n";
            }
        }
        report += "-------------------------------\n\n";

        try {
            FileOutputStream trace = app.openFileOutput("stack.trace", 
                                                        Context.MODE_PRIVATE);
            trace.write(report.getBytes());
            trace.close();
        } catch(IOException ioe) {
			// ...
        }

        defaultUEH.uncaughtException(t, e);
    }
}
