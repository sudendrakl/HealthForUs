package bizapps.com.healthforus;

import android.app.Application;
import bizapps.com.healthforus.helper.OneSignalNotificationOpenedHandler;
import bizapps.com.healthforus.helper.OneSignalNotificationReceivedHandler;
import com.onesignal.OneSignal;

/**
 * Created by sudendra.kamble on 22/09/16.
 */

public class MainApplication extends Application {
  private static MainApplication _instance;

  public static MainApplication getInstance() {
    return _instance;
  }

  MainApplication() {
    super();
    _instance = this;
  }

  @Override public void onCreate() {
    super.onCreate();
    OneSignal.startInit(this)
        .setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler())
        .setNotificationReceivedHandler(new OneSignalNotificationReceivedHandler())
        .init();
  }
}
