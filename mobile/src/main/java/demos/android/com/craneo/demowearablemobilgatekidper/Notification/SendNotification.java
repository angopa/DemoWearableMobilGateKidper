package demos.android.com.craneo.demowearablemobilgatekidper.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import demos.android.com.craneo.demowearablemobilgatekidper.model.Kid;
import demos.android.com.craneo.demowearablemobilgatekidper.R;
import demos.android.com.craneo.demowearablemobilgatekidper.broadcast.ReplyActivity;

/**
 * Created by crane on 10/20/2016.
 */

public class SendNotification
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private static final String TAG = "SendNotification";
    private Context context;
    private View view;
    private Node mNode;
    private GoogleApiClient googleApiClient;
    private String message;
    private int backgroundId;

    //Define the kid image, for the wearable
    private int kidImageId;
    private Bitmap kidImage;

    public SendNotification(Context context,
                             View view, Kid student, String message, boolean isNormal){
        this.context = context;
        this.view = view;
        this.message = message;

        //Defined the small icon for the notification
        if (isNormal){
            //The notification is normal
            backgroundId = context.getResources().getIdentifier(
                    "ic_simple_notification", "drawable", context.getPackageName());
        }else{
            //The notification is alert
            backgroundId = context.getResources().getIdentifier(
                    "ic_alert_notification", "drawable", context.getPackageName());
        }

        defineCommonComponents(student);
        send(student);
    }

    public SendNotification(Context context,
                            View view, Kid student, String message, List<String> options){
        this.context = context;
        this.view = view;

        backgroundId = context.getResources().getIdentifier(
                "ic_choice_notification", "drawable", context.getPackageName());

        sendMultipleChoices(student, message, options);
    }

    private void defineCommonComponents(Kid student) {
        //Defined the image of the kid for the notification
        kidImageId = context.getResources().getIdentifier(
                student.getImage(), "drawable", context.getPackageName());
        kidImage = BitmapFactory.decodeResource(context.getResources(), kidImageId);

        startGoogleApi();
    }

    private void startGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this.context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }


    public void send(Kid kid) {
        // Build the notification and add the action via WearableExtender
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(context.getText(R.string.app_name))
                        .setContentText(message+" "+kid.getName())
                        .setSmallIcon(backgroundId)
                        .setLargeIcon(kidImage);

        builder.setDefaults(Notification.VISIBILITY_PUBLIC);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        int notificationId = 1;
        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // Issue the notification with notification manager.
        notificationManager.notify(notificationId, builder.build());

        disconnet();
    }


    public void sendMultipleChoices(Kid kid, String message, List<String> options) {
        String[] choices = new String[options.size()];
        this.message = message;
        choices = options.toArray(choices);

        RemoteInput remoteInput = new RemoteInput.Builder(Intent.EXTRA_TEXT)
                .setLabel(context.getText(R.string.notification_prompt_reply))
                .setChoices(choices)
                .build();

        // Create an intent for the reply action
        Intent replyIntent = new Intent(context, ReplyActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(context, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the action
        NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder(
                R.drawable.ic_reply1, context.getText(R.string.notification_reply), replyPendingIntent);

        actionBuilder.addRemoteInput(remoteInput);
        actionBuilder.setAllowGeneratedReplies(true);

        NotificationCompat.Action.WearableExtender actionExtender =
                new NotificationCompat.Action.WearableExtender()
                        .setHintDisplayActionInline(true)
                        .setHintLaunchesActivity(true);

        NotificationCompat.WearableExtender extender =
                new NotificationCompat.WearableExtender()
                        .setBackground(kidImage);

        extender.addAction(actionBuilder.extend(actionExtender).build());

        // Build the notification and add the action via WearableExtender
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(context.getText(R.string.app_name))
                        .setContentText(this.message+" "+kid.getName())
                        .setSmallIcon(backgroundId)
                        .setLargeIcon(kidImage)
                        .extend(extender);

        builder.setDefaults(Notification.VISIBILITY_PUBLIC);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        int notificationId = 1;
        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // Issue the notification with notification manager.
        notificationManager.notify(notificationId, builder.build());
        //disconnet();
    }

    private void disconnet() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.NodeApi.getConnectedNodes(googleApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(@NonNull NodeApi.GetConnectedNodesResult nodes) {
                        for (Node node : nodes.getNodes()){
                            if (node != null && node.isNearby()){
                                mNode = node;
                                Log.d(TAG, "Connected to "+node.getDisplayName());
                            }
                        }
                        if (mNode == null){
                            Log.d(TAG, "Not connected ");
                        }
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
