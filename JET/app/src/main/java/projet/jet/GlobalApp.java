package projet.jet;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jess on 18/12/2016.
 */
public class GlobalApp extends Application{

    public SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

    }

    public String requete(String qs) {
        if (qs != null)
        {
            System.setProperty("http.keepAlive", "false");
            String urlData = "http://192.168.1.12/2i/APP2/projetmobile/data.php";

            try {
                URL url = new URL(urlData + "?" + qs);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = null;
                in = new BufferedInputStream(urlConnection.getInputStream());
                String txtReponse = CommonsFunctions.convertStreamToString(in);
                urlConnection.disconnect();
                return txtReponse;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return "";
    }

}
