package projet.jet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Jess on 20/12/2016.
 */
public class FriendsFacebookActivity  extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_friends_list);

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<String>();

        if(jsondata != null) {
            try {
                friendslist = new JSONArray(jsondata);
                for (int l=0; l < friendslist.length(); l++) {
                    friends.add(friendslist.getJSONObject(l).getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friends); // simple textview for list item
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }


}
