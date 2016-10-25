package demos.android.com.craneo.demowearablemobilgatekidper.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crane on 10/24/2016.
 */

public class MultipleChoiceNotification {
    public static final String WEARABLE_CHOICE_MESSAGE = "wearable_choice_message";
    public static final String WEARABLE_OPTION_A = "wearable_option_a";
    public static final String WEARABLE_OPTION_B = "wearable_option_b";
    public static final String WEARABLE_OPTION_C = "wearable_option_c";
    private SharedPreferences settings;
    private static List<String> prefMessages;
    private static String prefMessage;

    public MultipleChoiceNotification(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        prefMessages = new ArrayList<>();
        prefMessages.add(settings.getString(WEARABLE_OPTION_A, "Not Found"));
        prefMessages.add(settings.getString(WEARABLE_OPTION_B, "Not Found"));
        prefMessages.add(settings.getString(WEARABLE_OPTION_C, "Not Found"));

        prefMessage = settings.getString(WEARABLE_CHOICE_MESSAGE, "Not Found");

    }

    public static List<String> getMessages(){
        return prefMessages;
    }

    public static String getMessage(){
        return prefMessage;
    }
}
