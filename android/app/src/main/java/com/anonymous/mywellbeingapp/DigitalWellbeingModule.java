package com.mywellbeingapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DigitalWellbeingModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    public DigitalWellbeingModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "DigitalWellbeing";
    }

    @ReactMethod
    public void openUsageAccessSettings() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reactContext.startActivity(intent);
    }

    @ReactMethod
    public void getUsageStats(Promise promise) {
        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) reactContext.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long startTime = endTime - (1000 * 60 * 60 * 24); // Last 24 hours

            List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
            List<Map<String, Object>> usageList = new ArrayList<>();

            if (usageStatsList != null) {
                for (UsageStats usageStats : usageStatsList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("packageName", usageStats.getPackageName());
                    map.put("totalTimeInForeground", usageStats.getTotalTimeInForeground());
                    usageList.add(map);
                }
            }

            promise.resolve(usageList);
        } catch (Exception e) {
            promise.reject("USAGE_STATS_ERROR", e);
        }
    }
}
