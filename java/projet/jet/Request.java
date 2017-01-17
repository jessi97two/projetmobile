package projet.jet;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jess on 22/12/2016.
 */
public class Request extends AsyncTask<String, Integer, Boolean> {

    String txtReponse;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return Boolean.FALSE;
    }

    @Override
    protected void onPostExecute(Boolean th) {
        if(th == true){
            if(txtReponse != "") {
                try {
                    Log.i("DEBUG TEST REP", txtReponse);
                    JSONObject json = new JSONObject(txtReponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
