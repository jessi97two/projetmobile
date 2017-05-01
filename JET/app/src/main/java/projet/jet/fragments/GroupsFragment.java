package projet.jet.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import projet.jet.R;
import projet.jet.adapter.CustomListviewAdapter;
import projet.jet.classe.Contact;
import projet.jet.classe.Group;

/**
 * Created by Jess on 03/04/2017.
 */
public class GroupsFragment extends Fragment {

    Activity a;
    private ListView listviewGroups;
    private ArrayList<Group> groupslistReceived  = new ArrayList<Group>();
    private ArrayList<String> groupslist = new ArrayList<String>();

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onStart() {
        super.onStart();

        a = getActivity();

        groupslistReceived.clear();
        groupslist.clear();

        groupslistReceived = fetchGroups(a);

        for(Group group : groupslistReceived) {
            groupslist.add(group.name+"_"+group.id);
        }

        affichage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_groups_fragment,container,false);

        listviewGroups = (ListView)v.findViewById(R.id.listViewGroups);

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                fetchGroups(a);
            } else {
                Toast.makeText(a, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public ArrayList<Group> fetchGroups(Activity act){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(act,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.

        }

        ArrayList<Group> groupList = new ArrayList<Group>();
        String[] projection = new String[]{ContactsContract.Groups._ID,ContactsContract.Groups.TITLE};


        Cursor cursor=null;
        ContentResolver contentResolver = act.getContentResolver();
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
            String groupName =      cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));

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

    public void affichage() {
        CustomListviewAdapter customListviewAdapterad = new CustomListviewAdapter("groups",this.getActivity(), groupslist,a.getApplication(),a);
        listviewGroups.setAdapter(customListviewAdapterad);
    }


}
