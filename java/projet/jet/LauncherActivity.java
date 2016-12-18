package projet.jet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Created by Jess on 18/12/2016.
 */
public class LauncherActivity extends Activity {

    GlobalApp ga;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ga = (GlobalApp) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(ga.prefs.contains("login") && ga.prefs.contains("password")) {
            String login = ga.prefs.getString("login","");
            String pwd = ga.prefs.getString("password","");
            if(!(login.equals("")) && !(pwd.equals(""))) {
                Intent gotoGeneral = new Intent(LauncherActivity.this, GeneralActivity.class);
                startActivity(gotoGeneral);
            }
            else {
                Intent gotoLogin = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(gotoLogin);
            }
        }
        else {
            Intent gotoLogin = new Intent(LauncherActivity.this, LoginActivity.class);
            startActivity(gotoLogin);
        }
    }
}
