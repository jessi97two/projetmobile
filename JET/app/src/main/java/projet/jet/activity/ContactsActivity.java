package projet.jet.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import projet.jet.R;
import projet.jet.classe.Contact;

/**
 * Created by Jess on 01/05/2017.
 */
public class ContactsActivity  extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Contact> contactsList;
    private ArrayList<String> namecontactsList;
    private ListView listviewContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);

        Intent intent = getIntent();
        final String groupenameid = intent.getStringExtra("groupe");
        final String groupename = groupenameid.split("_")[0];

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupename + " : Participants");

        listviewContacts = (ListView) findViewById(R.id.listViewContacts);

        contactsList = new ArrayList<Contact>();
        namecontactsList = new ArrayList<String>();

        for(String id : groupenameid.split("_")[1].split(",")) {
            contactsList = fetchGroupMembers(id,this);
        }



        for(Contact contact : contactsList) {
            namecontactsList.add(contact.name);
        }

        if(namecontactsList.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namecontactsList); // simple textview for list item

            listviewContacts.setAdapter(adapter);
        }


    }

    public ArrayList<Contact> fetchGroupMembers(String groupId, Activity activity){
        ArrayList<Contact> groupMembers = new ArrayList<Contact>();
        String where =  ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID +"='"+groupId
                +"' AND "
                + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE+"='"
                + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'";
        String[] projection = new String[]{ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME};

        Cursor cursor=null;
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, where,null,
                    ContactsContract.Data.DISPLAY_NAME+" COLLATE LOCALIZED ASC");
        }
        catch (Exception ex)
        {
            Log.e("Content.Cursor",ex.getMessage() );
        }


    /*    Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, where,null,
                ContactsContract.Data.DISPLAY_NAME+" COLLATE LOCALIZED ASC");
        */
        while(cursor.moveToNext()){
            Contact contact = new Contact();
            contact.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            contact.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));

            Cursor phoneFetchCursor = null;

            try {
                phoneFetchCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"='"+contact.id+"'",null,null);

                //int phoneNumberIndex = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            catch (Exception ex)
            {
                Log.e("Content.Cursor",ex.getMessage() );
            }

            while(phoneFetchCursor.moveToNext()){
                contact.phNo = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contact.phDisplayName = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contact.phType = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            }
            phoneFetchCursor.close();
            groupMembers.add(contact);
        }
        cursor.close();
        return groupMembers;
    }
}
