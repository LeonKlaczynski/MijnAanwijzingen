package org.treinchauffeur.mijnaanwijzingen;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class MijnAanwijzingenApplication extends Application {
    @Override
    public void onCreate() {
        DynamicColors.applyToActivitiesIfAvailable(this); //Solely for pretty android 12 Material You color implementation
        super.onCreate();
    }
}
