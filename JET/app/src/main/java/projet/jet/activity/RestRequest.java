package projet.jet.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import projet.jet.GeneralActivity;
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
        else if(action.equals("getInvitationsReceived")) {
            try {
                String val = result.getString("invitations");
                JSONArray json = new JSONArray(val);
                homeFragment = (HomeFragment) mAct.frag.findFragmentByTag("home");
                homeFragment.displayResult("invitations",json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getEventsProche")) {
            try {
                String val = result.getString("events");
                JSONArray json = new JSONArray(val);
                homeFragment = (HomeFragment) mAct.frag.findFragmentByTag("home");
                homeFragment.displayResult("events",json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getInfosSondage")) {
            try {
                String val = result.getString("sondage");
                String valdates = result.getString("dates");
                String valrestos = result.getString("restaurants");

                JSONArray json = new JSONArray(val);
                JSONArray jsondate = new JSONArray(valdates);
                JSONArray jsonrestos = new JSONArray(valrestos);

                SondageInformationsActivity sondageInformationsActivity = (SondageInformationsActivity) mAct.act;
                sondageInformationsActivity.displayResult("infossondage",json, jsondate, jsonrestos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("putReponsesSondage")) {
            try {
                String res  = result.getString("statutreponsesondage");
                if(res.equals("1")) {
                    Intent gotohomepage = new Intent(mAct.act, GeneralActivity.class);
                    mAct.act.startActivity(gotohomepage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getInfosInvitation")) {
            try {
                String val = result.getString("invitation");
                JSONArray json = new JSONArray(val);

                InvitationInformationsActivity invitationInformationsActivity = (InvitationInformationsActivity) mAct.act;
                invitationInformationsActivity.displayResult(json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("putReponsesInvitation")) {
            try {
                String res  = result.getString("statutreponseinvitation");
                if(res.equals("1")) {
                    Intent gotohomepage = new Intent(mAct.act, GeneralActivity.class);
                    mAct.act.startActivity(gotohomepage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
