package projet.jet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import projet.jet.GlobalApp;

/**
 * Created by Jess on 03/04/2017.
 */
public class RestActivity extends AppCompatActivity {

    protected GlobalApp ga;
    protected FragmentManager frag;
    protected Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void envoiRequete(String qs, String action, GlobalApp gapp,FragmentManager fragment, Activity activity) {

        ga = gapp;
        frag = fragment;
        act = activity;

        RestRequest req = new RestRequest(this);
        req.execute(qs,action);
    }


}
