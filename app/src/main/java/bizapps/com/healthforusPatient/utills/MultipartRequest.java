package bizapps.com.healthforusPatient.utills;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import bizapps.com.healthforusPatient.BZAppApplication;

/**
 * Created by venkatat on 4/21/2016.
 */
public class MultipartRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();
    HttpEntity httpentity;

    private static final String FILE_PART_NAME = "document";
//    private static final String FILE_PART_NAME1 = "photo";
private static final String FILE_PART_NAME1 = "picture";
    private static final String FILE_PART_NAME2 = "proimage";
    private static final String FILE_PART_NAME3 = "file_record";
    private static String FILE_NAMES="image";
    private final Response.Listener<String> mListener;
    private final File file;
    private final File file1;
    private final HashMap<String, String> params;
    private boolean signup;
    private List<File> storeImages;

    public MultipartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, File file, File file1, List<File> storeImages, HashMap<String, String> params, boolean signup) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        this.file = file;
        this.file1 = file1;
        this.params = params;
        this.signup = signup;
        this.storeImages = storeImages;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {

        if(signup){
            if(file!=null){
            entity.addPart(FILE_PART_NAME1, new FileBody(file));}
            if(file1!=null){
            entity.addPart(FILE_PART_NAME1, new FileBody(file1));}
            if(storeImages.size()!=0){
                for(int i=0;i<storeImages.size();i++){
                    String temp = FILE_NAMES+(i+1);
                    Log.e("Tempp",""+temp);
                    entity.addPart(temp, new FileBody(storeImages.get(i)));
                }
            }
        }
        else{
            if(BZAppApplication.hRecord){
                entity.addPart(FILE_PART_NAME3, new FileBody(file));
                BZAppApplication.hRecord = false;
            }
            else {
                entity.addPart(FILE_PART_NAME2, new FileBody(file));
            }
        }

//        if(file1!=null) {
//            entity.addPart(FILE_PART_NAME, new FileBody(file));
//            entity.addPart(FILE_PART_NAME1, new FileBody(file1));
//        }
//        else{
//            entity.addPart(FILE_PART_NAME2,new FileBody(file));
//        }
        try {
            for (String key : params.keySet()) {
                Log.e("Key",""+key+" "+params.get(key));
                entity.addPart(key, new StringBody(params.get(key)));
            }
           // Log.e("Entity",""+entity.getContent().toString());
            ByteArrayOutputStream out = new ByteArrayOutputStream((int) entity.getContentLength());

// write content to stream
            entity.writeTo(out);

// either convert stream to string
            String string = out.toString();
            Log.e("Entity",""+entity);
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBodyContentType() {
        Log.e("GETBODYCT",""+entity.getContentType().getValue());
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        Log.e("ByteArray",""+bos.toByteArray());
        return bos.toByteArray();
    }

    /**
     * copied from Android StringRequest class
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
