package com.elitetechnologies.locktaskmode;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {

    public ImageButton ib_startkioske;

    private View mDecorView;
    private DevicePolicyManager mDpm;
    private boolean mIsKioskEnabled = false;
    // Whitelist two apps.
    private static final String KIOSK_PACKAGE = "com.facebook.katana";
    private static final String CURRENT_PACKAGE = "com.elitetechnologies.locktaskmode";
    private static final String[] APP_PACKAGES = {KIOSK_PACKAGE, CURRENT_PACKAGE};
    private ComponentName deviceAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* deviceAdmin = new ComponentName(this, AdminReceiver.class);
        mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (!mDpm.isAdminActive(deviceAdmin)) {
            Toast.makeText(this, getString(R.string.not_device_admin), Toast.LENGTH_SHORT).show();
        }

        if (mDpm.isDeviceOwnerApp(getPackageName())) {
            mDpm.setLockTaskPackages(deviceAdmin, APP_PACKAGES);
        } else {
            Toast.makeText(this, getString(R.string.not_device_owner), Toast.LENGTH_SHORT).show();
        }

        // Set an option to turn on lock task mode when starting the activity.
        ActivityOptions options = ActivityOptions.makeBasic();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            options.setLockTaskEnabled(true);
        }

// Start our kiosk app's main activity with our lock task mode option.
        PackageManager packageManager = getPackageManager();
        Intent launchIntent = packageManager.getLaunchIntentForPackage(KIOSK_PACKAGE);
        if (launchIntent != null) {
            startActivity(launchIntent, options.toBundle());
        }

        mDecorView = getWindow().getDecorView();

        // Enable the Home and Overview buttons so that our custom launcher can respond
// using our custom activities. Implicitly disables all other features.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mDpm.setLockTaskFeatures(deviceAdmin,
                    DevicePolicyManager.LOCK_TASK_FEATURE_HOME |
                            DevicePolicyManager.LOCK_TASK_FEATURE_OVERVIEW);
        }
*/

        ib_startkioske = (ImageButton) findViewById(R.id.ib_startkioske);
        ib_startkioske.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // enableKioskMode(!mIsKioskEnabled);
//                if (mDpm.isLockTaskPermitted(getPackageName())) {
                //startLockTask();
                    startService(new Intent(MainActivity.this, BackgroundService.class));
                try {
                    SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                    editor.putInt("password", 0);
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
                Intent i = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                startActivity(i);
//                }
            }
        });


        boolean usage_granted = false;
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            usage_granted = (checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            usage_granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        if (usage_granted == false) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        Intent it = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        it.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(this, AdminReceiver.class));
        startActivityForResult(it, 0);
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();


        // First, confirm that this package is whitelisted to run in lock task mode.
        if (mDpm.isLockTaskPermitted(getPackageName())) {
            startLockTask();
        } else {
            // Because the package isn't whitelisted, calling startLockTask() here
            // would put the activity into screen pinning mode.
        }
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void enableKioskMode(boolean enabled) {
        try {
            if (enabled) {
                // if (mDpm.isLockTaskPermitted(this.getPackageName())) {

                startLockTask();
                mIsKioskEnabled = true;
                mButton.setText(getString(R.string.exit_kiosk_mode));
                startService(new Intent(MainActivity.this, BackgroundService.class));
                finish();
                Intent i = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                startActivity(i);
//                } else {
//                    Toast.makeText(this, getString(R.string.kiosk_not_permitted), Toast.LENGTH_SHORT).show();
//                }
            } else {
                stopLockTask();
                mIsKioskEnabled = false;
                mButton.setText(getString(R.string.enter_kiosk_mode));
            }
        } catch (Exception e) {
            // TODO: Log and handle appropriately
        }
    }*/
}