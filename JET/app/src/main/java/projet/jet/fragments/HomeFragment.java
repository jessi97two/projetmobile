package projet.jet.fragments;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.activity.RestActivity;
import projet.jet.adapter.CustomListviewAdapter;

/**
 * Created by Jess on 03/04/2017.
 */
public class HomeFragment extends Fragment {

    protected GlobalApp ga;
    RestActivity restAct;
    private ListView mListViewSondages, mListViewInvitations, mListViewEvents;
    private TextView txtSondages, txtInvitations, txtEvents;

    @Override
    public void onStart() {
        super.onStart();

        restAct = new RestActivity();
        Activity a = getActivity();

        ga = (GlobalApp) a.getApplication();

        String qs = "action=getSondagesReceived&iduser=";
        restAct.envoiRequete(qs+ ga.prefs.getString("id",""),"getSondagesReceived",ga,this.getFragmentManager(),null);

        String qs2 = "action=getInvitationsReceived&iduser=";
        restAct.envoiRequete(qs2+ ga.prefs.getString("id",""),"getInvitationsReceived",ga,this.getFragmentManager(),null);

        String qs3 = "action=getEventsProche&iduser=";
        restAct.envoiRequete(qs3+ ga.prefs.getString("id",""),"getEventsProche",ga,this.getFragmentManager(),null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_home_fragment,container,false);

        Activity a = getActivity();

        mListViewSondages = (ListView)v.findViewById(R.id.listViewSondages);
        mListViewInvitations = (ListView)v.findViewById(R.id.listViewInvitations);
        mListViewEvents = (ListView)v.findViewById(R.id.listViewEvents);

        txtSondages = (TextView) v.findViewById(R.id.txtSondagesVides);
        txtInvitations = (TextView) v.findViewById(R.id.txtInvitationVide);
        txtEvents = (TextView) v.findViewById(R.id.txtEventVide);

        return v;
    }

    public void displayResult(String type , JSONArray jsonarray) {

        Activity a = getActivity();
        ArrayList<String> result = new ArrayList<String>();

        if(type.equals("sondages")) {
            if(jsonarray.length() > 0) {
                txtSondages.setVisibility(View.INVISIBLE);
                try {
                    for (int l=0; l < jsonarray.length(); l++) {
                        result.add(jsonarray.getJSONObject(l).getString("titre") + "_" + jsonarray.getJSONObject(l).getString("id") );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(a, R.layout.home_fragment_listview, result); // simple textview for list item
                CustomListviewAdapter customListviewAdapterad = new CustomListviewAdapter("sondage",this.getActivity(), result,a.getApplication(),a);
                mListViewSondages.setAdapter(customListviewAdapterad);
                ListUtils.setDynamicHeight(mListViewSondages);
            }
            else {
                txtSondages.setVisibility(View.VISIBLE);
            }


        }
        else if(type.equals("invitations")) {
            if(jsonarray.length() > 0) {
                txtInvitations.setVisibility(View.INVISIBLE);
                try {
                    for (int l=0; l < jsonarray.length(); l++) {
                        result.add(jsonarray.getJSONObject(l).getString("nom") + "_" + jsonarray.getJSONObject(l).getString("id") );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(a, R.layout.home_fragment_listview, result); // simple textview for list item
                CustomListviewAdapter customListviewAdapterad = new CustomListviewAdapter("invitation",this.getActivity(), result,a.getApplication(),a);
                mListViewInvitations.setAdapter(customListviewAdapterad);
                ListUtils.setDynamicHeight(mListViewInvitations);
            }
            else {
                txtInvitations.setVisibility(View.VISIBLE);
            }
        }
        else if(type.equals("events")) {
            if(jsonarray.length() > 0) {
                txtEvents.setVisibility(View.INVISIBLE);

                try {
                    for (int l=0; l < jsonarray.length(); l++) {
                        result.add(jsonarray.getJSONObject(l).getString("nom") + "_" + jsonarray.getJSONObject(l).getString("id") );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(a, android.R.layout.simple_list_item_1, result); // simple textview for list item
                CustomListviewAdapter customListviewAdapterad = new CustomListviewAdapter("event",this.getActivity(), result,a.getApplication(),a);
                mListViewEvents.setAdapter(customListviewAdapterad);
                ListUtils.setDynamicHeight(mListViewEvents);
            }
            else {
                txtEvents.setVisibility(View.VISIBLE);
            }

        }

    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
