package projet.jet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import projet.jet.activity.ContactsActivity;
import projet.jet.activity.RestActivity;
import projet.jet.classe.Contact;
import projet.jet.classe.Group;
import projet.jet.fragments.GroupsFragment;

/**
 * Created by Jess on 18/12/2016.
 */
public class LauncherActivity extends Activity {

    GlobalApp ga;
    RestActivity restAct;
    List<String> listGroupsReceived = new ArrayList<String>();
    List<Contact> contactsList = new ArrayList<Contact>();

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
            String id = ga.prefs.getString("id","");
            if(!(login.equals("")) && !(pwd.equals(""))) {
                // check groups

                restAct = new RestActivity();

                String qs1 = "action=getGroups&iduser=";
                restAct.envoiRequete(qs1+ ga.prefs.getString("id",""),"getGroups",ga,null,this);


            }
            else {
                Intent gotoLogin = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(gotoLogin);
                finish();
            }
        }
        else {
            Intent gotoLogin = new Intent(LauncherActivity.this, LoginActivity.class);
            startActivity(gotoLogin);
        }
    }

    private void majGroups() {


        GroupsFragment groupsFragment = new GroupsFragment();
        ArrayList<Group> listGroupsOnThePhone =  groupsFragment.fetchGroups(this);

        ArrayList<String> listGroupsToAdd = new ArrayList<String>();
        ArrayList<String> listGroupsToRemove = new ArrayList<String>();


        if(listGroupsOnThePhone.size() != listGroupsReceived.size()) {
            // test if group is already in the database , if not put the group into the listGroupsToAdd
            for(Group group : listGroupsOnThePhone) {
                if(!(listGroupsReceived.contains(group.name))) {
                    listGroupsToAdd.add(group.name + "_" + group.id);
                }
            }

            // test if group in th database is still existing on the phone, if not put the group into the listGroupsToRemove
            if(listGroupsReceived.size() > 0) {
                boolean found = false;
                for(String group : listGroupsReceived) {
                    for(Group groupPhone : listGroupsOnThePhone) {
                        if(groupPhone.name.equals(group)) {
                            found = true;
                        }
                    }
                    if(found == false){
                        listGroupsToRemove.add(group);
                    }
                }
            }

            for(String group : listGroupsToAdd) {
                String req = "action=addGroup&iduser=" + ga.prefs.getString("id","") + "&groupName=" + group.split("_")[0]
                        + "&groupidphone=" + group.split("_")[1];
                restAct.envoiRequete(req,"addGroup",ga,null,this);
            }

            for(String group : listGroupsToRemove) {
                String req = "action=removeGroup&iduser=" + ga.prefs.getString("id","") + "&groupName=" + group;
                restAct.envoiRequete(req,"removeGroup",ga,null,this);
            }
        }


        // go to general activity
        Intent gotoGeneral = new Intent(LauncherActivity.this, GeneralActivity.class);
        startActivity(gotoGeneral);
        finish();

    }

    public void displayResult(JSONArray json, String type) {
        if(type.equals("groupsReceived")) {
            try {
                if(json != null) {
                    for (int l=0; l < json.length(); l++) {
                        listGroupsReceived.add(json.getJSONObject(l).getString("nom"));
                    }
                }

                majGroups();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
