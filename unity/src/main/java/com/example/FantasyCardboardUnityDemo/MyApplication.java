package com.example.FantasyCardboardUnityDemo;

import android.app.Application;

/**
 * Created by knowroaming on 15-03-17.
 */
public class MyApplication extends Application {

    private boolean shouldRecord;

    public boolean getShouldRecord() {
        return shouldRecord;
    }

    public void setShouldRecord(boolean shouldRecord) {
        this.shouldRecord = shouldRecord;
    }
}
