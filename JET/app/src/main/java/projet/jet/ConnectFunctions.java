package projet.jet;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import projet.jet.fragments.HomeFragment;

/**
 * Created by Jess on 03/04/2017.
 */
public class ConnectFunctions extends Activity{

    GlobalApp ga;
    HomeFragment homeFragment;

    private class CheckSondages extends AsyncTask<String, Integer, Boolean> {

        String txtReponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    String urlData = "http://192.168.43.120/2i/APP2/projetmobile/data.php";
                    String qs = "action=getSondages&iduser=" + ga.prefs.getString("id","");
                    URL url = new URL(urlData + "?" + qs );
                    Log.i("DEBUG CONNEXION","url utilisée : " + url.toString());
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream in = null;
                        in = new BufferedInputStream(urlConnection.getInputStream());
                        txtReponse = CommonsFunctions.convertStreamToString(in);
                        urlConnection.disconnect();
                        return Boolean.TRUE;
                    }
                }
                catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return Boolean.FALSE;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if(th == true){
                if(txtReponse != "") {
                    try {
                        Log.i("DEBUG TEST REP", txtReponse);
                        JSONObject jsonobj = new JSONObject(txtReponse);
                        String val = jsonobj.getString("followers");
                        JSONArray json = new JSONArray(val);
                        homeFragment.displayResult("sondage",json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
