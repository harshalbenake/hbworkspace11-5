package com.elitetechnologies.locktaskmode;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserManager;
import android.widget.Toast;


public class AdminReceiver extends DeviceAdminReceiver {
    private static final String KIOSK_PACKAGE = "com.facebook.katana";
    private static final String CURRENT_PACKAGE = "com.elitetechnologies.locktaskmode";
    private static final String[] APP_PACKAGES = {KIOSK_PACKAGE,CURRENT_PACKAGE};

    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        super.onProfileProvisioningComplete(context, intent);
        // Enable the profile
        DevicePolicyManager manager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdmin = getWho(context);
        manager.setProfileName(deviceAdmin, context.getString(R.string.app_name));
        // Open the main screen
        Intent launch = new Intent(context, MainActivity.class);
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launch);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, context.getString(R.string.device_admin_enabled), Toast.LENGTH_SHORT).show();

       /* DevicePolicyManager dpm = getManager(context);
        ComponentName deviceAdmin = getWho(context);
        dpm.setLockTaskPackages(deviceAdmin, APP_PACKAGES);*/
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return context.getString(R.string.device_admin_warning);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, context.getString(R.string.device_admin_disabled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
        Toast.makeText(context, context.getString(R.string.kiosk_mode_enabled), Toast.LENGTH_SHORT).show();
        /*DevicePolicyManager dpm = getManager(context);
        ComponentName admin = getWho(context);

        dpm.addUserRestriction(admin, UserManager.DISALLOW_CREATE_WINDOWS);*/
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        Toast.makeText(context, context.getString(R.string.kiosk_mode_disabled), Toast.LENGTH_SHORT).show();
    }
}
