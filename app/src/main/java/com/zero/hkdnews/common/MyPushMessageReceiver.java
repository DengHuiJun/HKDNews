package com.zero.hkdnews.common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.zero.hkdnews.R;
import com.zero.hkdnews.groupmsg.AcceptActivity;

import cn.bmob.push.PushConstants;

/**
 * Created by 邓慧 on 15/4/28.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

            String msg = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);

            int mNotificationId = 0x01;

            Intent resultIntent = new Intent(context, AcceptActivity.class);
            resultIntent.putExtra("msg", msg);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack
            stackBuilder.addParentStack(AcceptActivity.class);
// Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
// Gets a PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.app_icon)
                            .setContentTitle("邀请请求")
                            .setContentText(msg);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId , mBuilder.build());

        }
    }

}
