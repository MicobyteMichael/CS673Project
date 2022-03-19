package com.example.healthapp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.example.healthapp.backend.RESTClient;

import java.lang.ref.WeakReference;

public class HealthApplication extends Application {
    private WeakReference<Activity> currActivity = null;
    private RESTClient apiConnection = new RESTClient("cs673projectbackend.herokuapp.com");

    private static HealthApplication app;

    @Override
    public void onCreate() {
        super.onCreate();

        if(app == null) {
            app = this;
        } else {
            System.err.println("Warning: Running the health application twice at the same time, this could cause issues!!");
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                currActivity = new WeakReference<Activity>(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                currActivity.clear();
            }

            @Override public void onActivityStarted(Activity activity) {}
            @Override public void onActivityResumed(Activity activity) {}
            @Override public void onActivityPaused(Activity activity) {}
            @Override public void onActivityStopped(Activity activity) {}
            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
        });
    }

    public WeakReference<Activity> getCurrentActivity() { return currActivity; }
    public RESTClient getAPIClient() { return apiConnection; }

    public static HealthApplication getInstance() {
        return app;
    }
}
