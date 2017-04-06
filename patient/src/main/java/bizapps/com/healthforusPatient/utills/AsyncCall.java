package bizapps.com.healthforusPatient.utills;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by venkatat on 2/12/2016.
 */
public class AsyncCall extends AsyncTask<String, Void, Boolean> {

    public String url;

   public AsyncCall(String url){
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... urls) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            //------------------>>
            HttpGet httppost = new HttpGet(url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                //String data = EntityUtils.toString(entity);


                //JSONObject jsono = new JSONObject(data);

                return true;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void onPostExecute(Boolean result) {

    }
}
