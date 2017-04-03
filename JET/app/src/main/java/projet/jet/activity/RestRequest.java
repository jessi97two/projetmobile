package projet.jet.activity;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.fragments.HomeFragment;

/**
 * Created by Jess on 03/04/2017.
 */
public class RestRequest extends AsyncTask<String, Void, JSONObject> {

    GlobalApp ga;
    RestActivity mAct;
    HomeFragment homeFragment;
    private String action = null;

    public RestRequest(RestActivity act) {
        mAct = act;
        ga = mAct.ga; // On fait normalement des WeakReferences ?
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... qs) {

        action = qs[1];

        String res = ga.requete(qs[0]);

        JSONObject json;

        try {
            json = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();
            json = new JSONObject();
        }

        return json;
    }


    protected void onPostExecute(JSONObject result) {
        if(action.equals("getSondagesReceived")) {
            try {
                String val = result.getString("sondages");
                JSONArray json = new JSONArray(val);
                homeFragment = (HomeFragment) mAct.frag.findFragmentByTag("home");
                homeFragment.displayResult("sondages",json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
