package com.example.healthapp.backend;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.ProgressBar;

import com.example.healthapp.HealthApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTClient implements Runnable {

    private final String baseURL;
    private Queue<Object[]> pendingTasks = new ArrayDeque<>();

    static {
        CookieHandler.setDefault(new CookieManager());
    }

    public RESTClient(String baseURL) {
        this(baseURL, Executors.newSingleThreadExecutor());
    }

    public RESTClient(String baseURL, Executor runner) {
        this.baseURL = baseURL;

        if(runner != null) {
            runner.execute(this);
        }
    }

    public <Type> void submitTask(RESTTask<Type> task, Consumer<Type> onComplete, Runnable onFailed) {
        synchronized(pendingTasks) {
            pendingTasks.add(new Object[] { task, onComplete, onFailed });
        }
    }

    public void run() {
        while(true) {
            Object[] taskStuff;

            synchronized(pendingTasks) {
                taskStuff = pendingTasks.poll();
            }

            if(taskStuff != null) {
                RESTTask<?> task = (RESTTask<?>)taskStuff[0];
                Consumer<? super Object> onComplete = (Consumer<? super Object>)taskStuff[1];
                Runnable onFailed = (Runnable)   taskStuff[2];

                AlertDialog[] msgbox = new AlertDialog[1];
                Activity act = HealthApplication.getInstance().getCurrentActivity();

                act.runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(act);
                    builder.setTitle("Working...");
                    builder.setMessage(task.getMessage());
                    builder.setView(new ProgressBar(act));

                    msgbox[0] = builder.create();
                    msgbox[0].show();
                });

                try {
                    Object result = runTaskNow(task);
                    act.runOnUiThread(() -> onComplete.accept(result));
                } catch(Exception e) {
                    System.err.println("Failed to run REST task \"" + task + "\" (message: \"" + task.getMessage() + "\")!");
                    e.printStackTrace();

                    act.runOnUiThread(onFailed);
                }

                msgbox[0].dismiss();
            }

            try {
                Thread.sleep(20);
            } catch(InterruptedException e) {}
        }
    }

    private Object runTaskNow(RESTTask<?> task) throws IOException, JSONException {
        String url = "https://" + baseURL + "/" + task.getEndpoint();
        HttpsURLConnection connection = (HttpsURLConnection)new URL(url).openConnection();

        connection.setRequestMethod(task.getMethod());
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.getOutputStream().write(task.getParameters().toString().getBytes(StandardCharsets.UTF_8));

        Map<String, List<String>> headers = connection.getHeaderFields();
        int code = connection.getResponseCode();

        InputStream stream;
        try {
            stream = connection.getInputStream();
        } catch(IOException e) {
            stream = connection.getErrorStream();
        }


        String line, response = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        while((line = reader.readLine()) != null) {
            response += line.trim();
        }

        reader.close();
        connection.disconnect();
        JSONObject json = null;

        if(response.length() > 0) {
            json = new JSONObject(response);
        }

        return task.process(code, json, headers);
    }
}