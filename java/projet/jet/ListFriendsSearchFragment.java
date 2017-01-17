package projet.jet;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Jess on 22/12/2016.
 */
public class ListFriendsSearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_user_friends_list,container,false);

        Activity a = getActivity();
        String json = getArguments().getString("json_data");
        String listjson = getArguments().getString("json_list_data");

        JSONArray listresult;
        ArrayList<String> result = new ArrayList<String>();

        try {
            listresult = new JSONArray(json);
            for (int l=0; l < listresult.length(); l++) {
                result.add(listresult.getJSONObject(l).getString("nom") + " " + listresult.getJSONObject(l).getString("prenom")
                + " : " + listresult.getJSONObject(l).getString("login") );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(a, R.layout.activity_listview, result); // simple textview for list item
        ListView listView = (ListView) v.findViewById(R.id.listViewFriends);
        Log.i("DEBUG TEST REP", listjson);
        CustomAdapter ad = new CustomAdapter(this.getActivity(), result,a.getApplication(),listjson);
        listView.setAdapter(ad);

        return v;
    }

}
