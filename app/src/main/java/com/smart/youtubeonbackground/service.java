package com.smart.youtubeonbackground;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;

import android.widget.Toast;
import androidx.core.app.NotificationCompat;


public class service extends Service {

    private Looper SLoop;
    private ServiceHandler SHandler;

    public Notification getNotification(String message) {

        return new NotificationCompat.Builder(getApplicationContext(), MainActivity.ServiceID)
                .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
                .setOngoing(true)
                .setChannelId(MainActivity.ServiceID)
                .setContentTitle("Youtube On Background")
                .setContentText(message)
                .setAutoCancel(true)
                .build();
    }


    public service() {}
    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        SLoop = thread.getLooper();
        SHandler = new ServiceHandler(SLoop);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire();
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Message msg = SHandler.obtainMessage();
        msg.arg1 = startId;

        if (intent != null) {
            msg.setData(intent.getExtras());
            SHandler.sendMessage(msg);
        } else {
            Toast.makeText(service.this, "Unknown error happened.", Toast.LENGTH_SHORT).show();
        }

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void onDestroy() {
        stopSelf();

    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            Notification msgDEC = getNotification("");
            startForeground(msg.arg1, msgDEC);
        }
    }

}

