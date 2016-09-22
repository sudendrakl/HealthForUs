package bizapps.com.healthforus.helper;

import android.content.Intent;
import android.util.Log;
import bizapps.com.healthforus.MainActivity;
import bizapps.com.healthforus.MainApplication;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import org.json.JSONObject;

/**
 * Created by sudendra.kamble on 22/09/16.
 */
//when app is in bg
public class OneSignalNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
  static final private String TAG = OneSignalNotificationReceivedHandler.class.getSimpleName();

  @Override
  public void notificationReceived(OSNotification notification) {
    JSONObject data = notification.payload.additionalData;
    String customKey;

    Log.i(TAG, "notificationReceived() payload = " + (data != null ? data.toString() : null));

    if (data != null) {
      customKey = data.optString("customkey", null);
      if (customKey != null)
        Log.i("OneSignalExample", "customkey set with value: " + customKey);
    }

    //TODO: do something, app is already opened, redirect to proper screen or webpage


    Intent intent = new Intent(MainApplication.getInstance(), MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
    //TODO intent.putExtra("KEY", "VALUE");
    MainApplication.getInstance().startActivity(intent);

  }
}