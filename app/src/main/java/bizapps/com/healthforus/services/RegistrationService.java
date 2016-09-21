package bizapps.com.healthforus.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import bizapps.com.healthforus.BuildConfig;
import bizapps.com.healthforus.data.CommonResponse;
import bizapps.com.healthforus.utils.Constants;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

/**
 * Created by sudendra.kamble on 22/09/16.
 */

public class RegistrationService extends IntentService {
  static final private String TAG = RegistrationService.class.getSimpleName();
  public static final MediaType MEDIA_TYPE_MARKDOWN =
      MediaType.parse("text/x-markdown; charset=utf-8");

  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson = new Gson();

  public RegistrationService() {
    super("RegistrationService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    //assuming all are post api's....please handle for get or put
    String formBody = intent.getStringExtra(Constants.IntentExtra.FORM_BODY);
    String url = BuildConfig.BASE_URL + intent.getStringExtra(Constants.IntentExtra.URL);
    String authToken = intent.getStringExtra(Constants.IntentExtra.AUTH_TOKEN);

    try {
      runSync(url, authToken, formBody);
    } catch (Exception e) {
      //TODO: failed to send data
      e.printStackTrace();
    }
  }

  public void runSync(String url, String token, String params) throws Exception {
    Request request = new Request.Builder().url(url)
        .addHeader("Authorization", token)
        .addHeader("Content-Type", "application/json")
        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, params))
        .build();

    Response response = client.newCall(request).execute();
    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

    System.out.println(response.body().string());

    CommonResponse commonResponse =
        gson.fromJson(response.body().charStream(), CommonResponse.class);
    //TODO: updated response
    Log.i(TAG, commonResponse.toString());

    if(commonResponse.isStatus()) {
      //TODO: success
    } else {
      //TODO: failure
    }

  }
}
