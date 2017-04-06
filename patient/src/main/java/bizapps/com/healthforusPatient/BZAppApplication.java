package bizapps.com.healthforusPatient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.List;

import bizapps.com.healthforusPatient.Model.Login;
import bizapps.com.healthforusPatient.helper.OneSignalNotificationOpenedHandler;
import bizapps.com.healthforusPatient.helper.OneSignalNotificationReceivedHandler;
import bizapps.com.healthforusPatient.utills.ConnectivityReceiver;
import bizapps.com.healthforusPatient.utills.LruBitmapCache;

public class BZAppApplication extends Application {
 
    public static final String TAG = BZAppApplication.class
            .getSimpleName();
    public static String mLocation;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;
    public static String MyPREFERENCES = "doctorpluspatient";
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public static Login myData;
    public static String DocID;
    public static String userID;
    public static boolean isReqGPS;
    private static BZAppApplication mInstance;
    public static boolean hRecord = false;
    public static int selectedSpez=1;
    public static String[] bookedSlots;
    public static boolean isFromBooking;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mSharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mLocation = mSharedPreferences.getString("userLocation"," ");
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler())
                .setNotificationReceivedHandler(new OneSignalNotificationReceivedHandler())
                .init();

    }
 
    public static synchronized BZAppApplication getInstance() {
        return mInstance;
    }
    
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
 
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
 
        return mRequestQueue;
    }
 
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
 
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
