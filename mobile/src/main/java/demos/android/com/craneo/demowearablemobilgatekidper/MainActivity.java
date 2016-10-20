package demos.android.com.craneo.demowearablemobilgatekidper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

/**
 * This activity is used to config the wearable message, it has three different flavors:
 * Normal Message: The student arrives or lefts the school, and just inform about it.
 * Multiple chooses: The system ask why the student don't arrive jet at school.
 * Alert Message: The system trigger an alert that alert the parent about some unusual situation.
 *
 */
public class MainActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static TextView textView;
    private static final String TAG = "MainActivity";
    private Node mNode;
    private GoogleApiClient googleApiClient;
    private Kid kid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        List<Kid> kids = DataProvider.kidsList;
        kid = kids.get(0);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    private void initializeComponents() {
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void sentNotification(View view) {
        //Defined the image of the kid for the notification
        int kidImageId = getResources().getIdentifier(
                kid.getImage(), "drawable", getPackageName());
        Bitmap kidImage = BitmapFactory.decodeResource(getResources(), kidImageId);

        //Defined the small icon for the notification
        int backgroundId = getResources().getIdentifier(
                "ic_background", "drawable", getPackageName());

        String[] choices = this.getResources().getStringArray(R.array.notification_reply_choices);

        RemoteInput remoteInput = new RemoteInput.Builder(Intent.EXTRA_TEXT)
                .setLabel(getText(R.string.notification_prompt_reply))
                .setChoices(choices)
                .build();

        Intent replyIntent = new Intent(this, ReplyActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(this, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder(
                R.drawable.ic_reply1, getText(R.string.notification_reply), replyPendingIntent);

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
        String message = getText(R.string.no_assistance)+" "+kid.getName();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(getText(R.string.app_name))
                        .setContentText(message)
                        .setSmallIcon(backgroundId)
                        .setLargeIcon(kidImage)
                        .extend(extender);

        int notificationId = 1;
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        Log.d(TAG, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static void putOption(String message) {
        textView.setText(message);
    }

    public void createNotification(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createNormalNotification:
                break;
            case R.id.createMultiChooseNotification:
                break;
            case R.id.createAlertNotification:
                break;
            case R.id.sentNotification:
                break;
        }
    }
}