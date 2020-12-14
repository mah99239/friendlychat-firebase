package com.example.friendlychat.date;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.friendlychat.R;
import com.example.friendlychat.ui.MainActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Random;

public class FCMReceiver extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "from" + remoteMessage.getFrom());
        //use data!

        //when you  send notification fron the firebase console (avoid it for now. is still very now)
       /* if(remoteMessage.getNotification()!= null){
            Log.e(TAG , "message Notification body:"+remoteMessage.getNotification().getBody());
            String title =(remoteMessage.getNotification().getTitle()!=null) ? remoteMessage.getNotification().getTitle() : "default title ";
            sendNotifaiction(title , remoteMessage.getNotification().getBody());
        }*/

        if (remoteMessage.getData().isEmpty()) {

        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "message data payload:" + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJop();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
        }
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "message Notification body" + remoteMessage.getNotification().getBody());
                String message = remoteMessage.getNotification().getBody();
                sendNotifaiction(remoteMessage.getNotification().getTitle(), message);
            }



    }
    /**
     * Called if FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve
     * the token.
     */
    @Override
    public void onNewToken(@NonNull String token) {

        Log.e("tokenfirebase", token);

        sendRegistrationToServer(token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    private void scheduleJop() {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();

        WorkManager.getInstance().beginWith(work).enqueue();
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param title       FCM title notifiation received
     **/
    private void sendNotifaiction(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.fcm_message);

        Uri defaultsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifiactionBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultsoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "channel humman readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(new Random().nextInt(), notifiactionBuilder.build());

    }


}
