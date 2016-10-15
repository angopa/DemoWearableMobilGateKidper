package demos.android.com.craneo.demowearablemobilgatekidper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by crane on 10/15/2016.
 */

public class ReplyActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("Delay");
        if(message == null){
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            if (remoteInput != null){
                message = remoteInput.getCharSequence(Intent.EXTRA_TEXT).toString();
                Log.d("ReplyActivity", message);
                MainActivity.putOption(message);
            }
        }
    }
}
