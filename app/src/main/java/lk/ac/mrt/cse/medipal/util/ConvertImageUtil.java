package lk.ac.mrt.cse.medipal.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.loopj.android.http.*;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yasiru on 10/21/17.
 */

public class ConvertImageUtil {
    public static void encodeImagetoString (){
        String imgPath = "";
        final Bitmap[] bitmap = new Bitmap[1];
        final String[] encodedString = new String[1];
        final String finalImgPath = imgPath;
        RequestParams params = new RequestParams();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap[0] = BitmapFactory.decodeFile(finalImgPath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap[0].compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString[0] = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                RequestParams params = new RequestParams();
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString[0]);
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public static void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Java server
    public static void makeHTTPCall() {
        RequestParams params = new RequestParams();
        //prgDialog.setMessage("Invoking JSP");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://192.168.2.5:9999/ImageUploadWebApp/uploadimg.jsp",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // When Http response code is '404'
                        if (statusCode == 404) {

                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                        }
                        // When Http response code other than 404, 500
                        else {
                        }
                    }
                });
    }

}
