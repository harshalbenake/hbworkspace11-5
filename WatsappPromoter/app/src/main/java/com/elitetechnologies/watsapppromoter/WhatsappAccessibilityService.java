package com.elitetechnologies.watsapppromoter;

import android.accessibilityservice.AccessibilityService;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class WhatsappAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent (AccessibilityEvent event) {
        try {
            if (getRootInActiveWindow () == null) {
                return;
            }

            AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap (getRootInActiveWindow ());

            // Whatsapp send button id
            List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
            if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty()) {
                return;
            }

            AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get(0);
            if (!sendMessageButton.isVisibleToUser()) {
                return;
            }

            // Now fire a click on the send button
            sendMessageButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            // Now go back to your app by clicking on the Android back button twice:
            // First one to leave the conversation screen
            // Second one to leave whatsapp

            Thread.sleep(1000); // hack for certain devices in which the immediate back click is too fast to handle
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(1000);  // same hack as above
            performGlobalAction(GLOBAL_ACTION_BACK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onInterrupt() {
        System.out.println("Whatsapp Accessibility Service onInterrupt");
    }
}