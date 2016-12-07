package edu.sjsu.cmpe.partyon.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.activities.MessageBoxActivity;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Ticket;

/**
 * Created by Ming on 12/5/16.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private final static String TAG = "AlarmReceiver.class";
    private boolean isUnNotifiedMsg = false;
    private String partyNamesStr = "";
    private int mInvitationAmount = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        checkMessage(context,intent);
    }
    public void checkMessage(final Context context, final Intent intent){
        AppData.messageList = new ArrayList<>();
        ParseQuery query = ParseQuery.getQuery(AppData.OBJ_NAME_TICKET);
        query.orderByDescending("createdAt");
        //query.whereEqualTo("msgState",Ticket.STATE_MSG_UNNOTIFIED);
        query.whereEqualTo("receiverID",AppData.getUser().getObjectId());
        query.findInBackground(new FindCallback<Ticket>() {
            @Override
            public void done(List<Ticket> objects, ParseException e) {
                if (e == null) {
                    Log.v(TAG,"get message from server:" + objects.size());
                    for(Ticket t : objects){
                        AppData.messageList.add(t);
                        if(t.getMsgState() == Ticket.STATE_MSG_UNNOTIFIED){
                            mInvitationAmount++;
                        }
                    }
                    if(mInvitationAmount>0){
                        createNotification(context,intent);
                    }
                } else {
                    // something went wrong
                    Log.e(TAG,e.getMessage());
                }
            }
        });

    }
    private void createNotification(Context context, Intent intent){
        Intent notificationIntent = new Intent(context, MessageBoxActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MessageBoxActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.setContentTitle("New Invitation")
                .setContentText("You have new party "+mInvitationAmount+" invitation(s)")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.drawable.ic_local_bar_black_24dp)
                .setContentIntent(pendingIntent).build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
