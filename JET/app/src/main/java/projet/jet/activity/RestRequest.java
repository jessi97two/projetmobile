package projet.jet.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import projet.jet.GeneralActivity;
import projet.jet.GlobalApp;
import projet.jet.LauncherActivity;
import projet.jet.R;
import projet.jet.fragments.EventsFragment;
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
        else if(action.equals("getGroups")) {
            try {
                String res  = result.getString("groupes");
                JSONArray json = null;
                if(!(res.equals("0"))) {
                    json = new JSONArray(res);
                }
                if(mAct.act instanceof LauncherActivity) {
                    LauncherActivity launcherActivity = (LauncherActivity) mAct.act;
                    launcherActivity.displayResult(json,"groupsReceived");
                }
                else if(mAct.act instanceof SondageCreationActivityGroupes) {
                    SondageCreationActivityGroupes sondageCreationActivityGroupes = (SondageCreationActivityGroupes) mAct.act;
                    sondageCreationActivityGroupes.displayResult(json,"groupsReceived");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("addGroup")) {
            try {
                String res  = result.getString("groupe");
                if(res.equals("1")) {
                    LauncherActivity launcherActivity = (LauncherActivity) mAct.act;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("removeGroup")) {
            try {
                String res  = result.getString("groupe");
                if(res.equals("1")) {
                    LauncherActivity launcherActivity = (LauncherActivity) mAct.act;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getRestaurants")) {
            try {
                String res  = result.getString("restaurants");
                JSONArray json = null;
                if(!(res.equals("0"))) {
                    json = new JSONArray(res);
                }
                SondageCreationActivityRestos sondageCreationActivityRestos = (SondageCreationActivityRestos) mAct.act;
                sondageCreationActivityRestos.displayResult(json,"restaurantsReceived");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getGroupById")) {
            try {
                String res  = result.getString("groupe");
                JSONArray json = null;
                if(!(res.equals("0"))) {
                    json = new JSONArray(res);
                }
                SondageCreationActivityRecapitulatif sondageCreationActivityRecapitulatif = (SondageCreationActivityRecapitulatif) mAct.act;
                sondageCreationActivityRecapitulatif.displayResult(json,"groupeNameReceived");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getRestaurantById")) {
            try {
                String res  = result.getString("restaurant");
                JSONArray json = null;
                if(!(res.equals("0"))) {
                    json = new JSONArray(res);
                }
                SondageCreationActivityRecapitulatif sondageCreationActivityRecapitulatif = (SondageCreationActivityRecapitulatif) mAct.act;
                sondageCreationActivityRecapitulatif.displayResult(json,"restaurantNameReceived");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("addSondage")) {
            try {
                String res  = result.getString("sondage");

                SondageCreationActivityRecapitulatif sondageCreationActivityRecapitulatif = (SondageCreationActivityRecapitulatif) mAct.act;
                sondageCreationActivityRecapitulatif.suiteEnregistrementRestaurant(res);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("addProprositionRestaurant")) {
            try {
                String res  = result.getString("propositionrestaurant");

            //    SondageCreationActivityRecapitulatif sondageCreationActivityRecapitulatif = (SondageCreationActivityRecapitulatif) mAct.act;
            //    sondageCreationActivityRecapitulatif.suiteEnregistrementDate();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("addProprositionDate")) {
            try {
                String res  = result.getString("propositiondate");

     /*           Intent gotohomepage = new Intent(mAct.act, GeneralActivity.class);
                mAct.act.startActivity(gotohomepage);
                mAct.act.finish(); */

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getContactByNumber")) {
            try {
                String res  = result.getString("contact");

             //   SondageCreationActivityRecapitulatif sondageCreationActivityRecapitulatif = (SondageCreationActivityRecapitulatif) mAct.act;
               // sondageCreationActivityRecapitulatif.lancerSondage();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getIdOnTelGroup")) {
            try {
                String res  = result.getString("idontelgroupe");

                SondageCreationActivityRecapitulatif sondageCreationActivityRecapitulatif = (SondageCreationActivityRecapitulatif) mAct.act;
                sondageCreationActivityRecapitulatif.majContactsGroupe(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getAllSondagesUser")) {
            try {
                String val = result.getString("sondages");
                JSONArray json = new JSONArray(val);

                EventsFragment eventFragment = (EventsFragment) mAct.frag.findFragmentByTag("events");
                eventFragment.displayResult("sondages",json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getResultatsDateSondage")) {
            try {
                String val = result.getString("resultatsDate");
                JSONArray json = new JSONArray(val);

                ResultatsSondageActivity resultatsSondageActivity = (ResultatsSondageActivity) mAct.act;
                resultatsSondageActivity.loadUsersResultatsDatesSondage(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getUsersResultatsDateSondage")) {
            try {
                String val = result.getString("resultatsUsersDate");
                JSONArray json = new JSONArray(val);

                ResultatsSondageActivity resultatsSondageActivity = (ResultatsSondageActivity) mAct.act;
                resultatsSondageActivity.completeTableChoixDates(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getResultatsRestaurantSondage")) {
            try {
                String val = result.getString("resultatsRestaurant");
                JSONArray json = new JSONArray(val);

                ResultatsSondageActivity resultatsSondageActivity = (ResultatsSondageActivity) mAct.act;
                resultatsSondageActivity.loadUsersResultatsRestaurantsSondage(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getUsersResultatsRestaurantSondage")) {
            try {
                String val = result.getString("resultatsUsersRestaurant");
                JSONArray json = new JSONArray(val);

                ResultatsSondageActivity resultatsSondageActivity = (ResultatsSondageActivity) mAct.act;
                resultatsSondageActivity.completeTableChoixRestaurants(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getGroupByIdSondage")) {
            try {
                String val = result.getString("groupname");
                JSONArray json = new JSONArray(val);

                EventCreationActivityRecapitulatif eventCreationActivityRecapitulatif = (EventCreationActivityRecapitulatif) mAct.act;
                eventCreationActivityRecapitulatif.getGroupName(json);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getRestaurantBySondageName")) {
            try {
                String val = result.getString("idrestaurant");
                JSONArray json = new JSONArray(val);

                EventCreationActivityRecapitulatif eventCreationActivityRecapitulatif = (EventCreationActivityRecapitulatif) mAct.act;
                eventCreationActivityRecapitulatif.getIdRestaurant(json);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("addEvent")) {
            try {
                String val = result.getString("event");

                EventCreationActivityRecapitulatif eventCreationActivityRecapitulatif = (EventCreationActivityRecapitulatif) mAct.act;
                eventCreationActivityRecapitulatif.eventSent(val);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("getAllEventsUser")) {
            try {
                String val = result.getString("events");
                JSONArray json = new JSONArray(val);

                EventsFragment eventFragment = (EventsFragment) mAct.frag.findFragmentByTag("events");
                eventFragment.displayResult("events",json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
