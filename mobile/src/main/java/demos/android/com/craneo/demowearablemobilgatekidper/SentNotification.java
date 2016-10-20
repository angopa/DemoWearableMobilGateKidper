package demos.android.com.craneo.demowearablemobilgatekidper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.view.View;

/**
 * Created by crane on 10/20/2016.
 */

public class SentNotification {
    Context context;
    View view;

    public SentNotification(Context context, View view, Kid student){
        this.context = context;
        this.view = view;
        sentNotification(this.context, this.view, student);
    }

    public void sentNotification(Context context, View view, Kid kid) {
        //Defined the image of the kid for the notification
        int kidImageId = context.getResources().getIdentifier(
                kid.getImage(), "drawable", context.getPackageName());
        Bitmap kidImage = BitmapFactory.decodeResource(context.getResources(), kidImageId);

        //Defined the small icon for the notification
        int backgroundId = context.getResources().getIdentifier(
                "ic_background", "drawable", context.getPackageName());

        String[] choices = context.getResources().getStringArray(R.array.notification_reply_choices);

        RemoteInput remoteInput = new RemoteInput.Builder(Intent.EXTRA_TEXT)
                .setLabel(context.getText(R.string.notification_prompt_reply))
                .setChoices(choices)
                .build();

        Intent replyIntent = new Intent(context, ReplyActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(context, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

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

        //Created the message with the student name
        String message = context.getText(R.string.no_assistance)+" "+kid.getName();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(context.getText(R.string.app_name))
                        .setContentText(message)
                        .setSmallIcon(backgroundId)
                        .setLargeIcon(kidImage)
                        .extend(extender);

        int notificationId = 1;
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
