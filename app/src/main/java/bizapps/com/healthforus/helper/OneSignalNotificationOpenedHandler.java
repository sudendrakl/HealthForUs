package bizapps.com.healthforus.helper;

import android.content.Intent;
import android.util.Log;
import bizapps.com.healthforus.MainActivity;
import bizapps.com.healthforus.MainApplication;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import org.json.JSONObject;

/**
 * Created by sudendra.kamble on 22/09/16.
 */
//when app is opened
public class OneSignalNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
  static final private String TAG = OneSignalNotificationOpenedHandler.class.getSimpleName();

  // This fires when a notification is opened by tapping on it.
  @Override public void notificationOpened(OSNotificationOpenResult result) {
    OSNotificationAction.ActionType actionType = result.action.type;
    JSONObject data = result.notification.payload.additionalData;
    String customKey;

    Log.i(TAG, "notificationOpened() payload = " + (data != null ? data.toString() : null));
    if (data != null) {
      customKey = data.optString("customkey", null);
      if (customKey != null) Log.i("OneSignalExample", "customkey set with value: " + customKey);
    }

    if (actionType == OSNotificationAction.ActionType.ActionTaken) {
      Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
    }

    //TODO: do something, notification clicked
    // The following can be used to open an Activity of your choice.

    Intent intent = new Intent(MainApplication.getInstance(), MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
    //TODO intent.putExtra("KEY", "VALUE");
    MainApplication.getInstance().startActivity(intent);

    // Follow the instructions in the link below to prevent the launcher Activity from starting.
    // https://documentation.onesignal.com/docs/android-notification-customizations#changing-the-open-action-of-a-notification
  }
}