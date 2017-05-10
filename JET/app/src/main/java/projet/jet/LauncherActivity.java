package projet.jet;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import projet.jet.activity.RestActivity;
import projet.jet.classe.Contact;
import projet.jet.classe.Group;

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

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Toast.makeText(this, "OURRAAAAHHH", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void majGroups() {

        ArrayList<String> groupTitle = new ArrayList<String>();

        ArrayList<Group> listGroupsOnThePhone =  chargerGroups();

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

    public ArrayList<Group> chargerGroups() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
        }

        ArrayList<Group> groupList = new ArrayList<Group>();
        String[] projection = new String[]{ContactsContract.Groups._ID,ContactsContract.Groups.TITLE};


        Cursor cursor=null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, null,null, null,null);
        }
        catch (Exception ex)
        {
            Log.e("Content.Cursor",ex.getMessage() );
        }

        ArrayList<String> groupTitle = new ArrayList<String>();


        Log.i("Ola","Test");
        while(cursor.moveToNext()){
            Group item = new Group();
            item.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
            String groupName =     cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));

            if(groupName.contains("Group:"))
                groupName = groupName.substring(groupName.indexOf("Group:")+"Group:".length()).trim();

            if(groupName.contains("Favorite_"))
                groupName = "Favorite";

            if(groupName.contains("Starred in Android") || groupName.contains("My Contacts"))
                continue;

            if(groupTitle.contains(groupName)){
                for(Group group:groupList){
                    if(group.name.equals(groupName)){
                        group.id += ","+item.id;
                        break;
                    }
                }
            }else{
                groupTitle.add(groupName);
                item.name = groupName;
                groupList.add(item);
            }

        }

        cursor.close();
        Collections.sort(groupList,new Comparator<Group>() {
            public int compare(Group item1, Group item2) {
                return item2.name.compareTo(item1.name)<0
                        ?0:-1;
            }
        });
        return groupList;


    }
}
