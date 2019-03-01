package com.elitetechnologies.locktaskmode;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;


public class BackgroundService extends Service {
    private Context context;
    private static Timer timer = new Timer();

    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService();
    }

    /**
     * This method is used to start service backgroundly.
     */
    private void startService() {
        timer.scheduleAtFixedRate(new mainTask(), 0, 500);
        context = this;
    }

    private class mainTask extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    /**
     * This handler is used to check which is top activity.
     */
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            getTopActivity(context);
//            boolean IsMyServiceRunning = isMyServiceRunning(context);
//            System.out.println("IsMyServiceRunning: " + IsMyServiceRunning);
        }
    };


    public boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BackgroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getTopActivity(Context context) {
        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long milliSecs = 60 * 1000;
            Date date = new Date();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime() - milliSecs, date.getTime());
            long recentTime = 0;
            String packageName = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats stats = queryUsageStats.get(i);
                if (stats.getLastTimeStamp() > recentTime) {
                    recentTime = stats.getLastTimeStamp();
                    packageName = stats.getPackageName();
                }
            }
            System.out.println("packageName: "+packageName);

            SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                int password = prefs.getInt("password", 0); //0 is the default value.



            if(!packageName.equalsIgnoreCase("")
                    && !packageName.equalsIgnoreCase(getPackageName())
                    && !"com.facebook.katana".equalsIgnoreCase(packageName)
                    && !packageName.contains("com.google")
                    && password!=1234){
            Intent intent = new Intent(context, LockScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
