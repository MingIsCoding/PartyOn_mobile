package edu.sjsu.cmpe.partyon.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ming on 12/4/16.
 */

public class NotificationService extends Service {
    public final static String RECEIVER_ID = "receiver_id";
    final static String TAG = "NotificationService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    @Override
    public void onCreate() {
        Log.d(TAG,"oncreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started"+intent.getStringExtra(RECEIVER_ID), Toast.LENGTH_LONG).show();
        Log.d(TAG,intent.getStringExtra(RECEIVER_ID));
        return START_STICKY;
    }
}
