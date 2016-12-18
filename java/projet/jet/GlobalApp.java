package projet.jet;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.net.CookieHandler;
import java.net.CookieManager;

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


}
