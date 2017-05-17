package projet.jet.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import projet.jet.GeneralActivity;
import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.activity.RestActivity;
import projet.jet.activity.SondageCreationActivity;
import projet.jet.adapter.CustomListviewAdapter;

/**
 * Created by Jess on 03/04/2017.
 */
public class EventsFragment extends Fragment {

    ImageButton btnCreatePoll;
    private ListView listViewSondages, listViewEvents;
    private TextView txtSondages, txtEvents;
    RestActivity restAct;
    protected GlobalApp ga;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_events_fragment,container,false);


        v.setFocusableInTouchMode(true);
        v.setOnKeyListener( new View.OnKeyListener(){
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ){
                if( keyCode == KeyEvent.KEYCODE_BACK ){
                    ((GeneralActivity) getActivity()).setNavItemIndex(0);
                    ((GeneralActivity) getActivity()).getHomeFragment();
                    return true;
                }
                return false;
            }
        } );

        final Activity a = getActivity();
        restAct = new RestActivity();
        ga = (GlobalApp) a.getApplication();

        listViewSondages = (ListView)v.findViewById(R.id.listViewSondagesEvents);
        listViewEvents = (ListView)v.findViewById(R.id.listViewEventsEvents);

        btnCreatePoll = (ImageButton) v.findViewById(R.id.btncreatePoll);

        txtSondages = (TextView) v.findViewById(R.id.txtSondagesEventsVide);
        txtEvents = (TextView) v.findViewById(R.id.txtEventsEventsVides);

        String qs = "action=getAllSondagesUser&iduser=";
        restAct.envoiRequete(qs+ ga.prefs.getString("id",""),"getAllSondagesUser",ga,this.getFragmentManager(),null);

        String qs2 = "action=getAllEventsUser&iduser=";
        restAct.envoiRequete(qs2+ ga.prefs.getString("id",""),"getAllEventsUser",ga,this.getFragmentManager(),null);


        btnCreatePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotocreationsondage = new Intent(a, SondageCreationActivity.class);
                a.startActivity(gotocreationsondage);
            }
        });

        return v;
    }

    public void displayResult(String type, JSONArray jsonarray) {

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

                CustomListviewAdapter customListviewAdapterad = new CustomListviewAdapter("sondageevents",this.getActivity(), result,a.getApplication(),a);
                listViewSondages.setAdapter(customListviewAdapterad);
                ListUtils.setDynamicHeight(listViewSondages);
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

                CustomListviewAdapter customListviewAdapterad = new CustomListviewAdapter("eventsevents",this.getActivity(), result,a.getApplication(),a);
                listViewEvents.setAdapter(customListviewAdapterad);
                ListUtils.setDynamicHeight(listViewEvents);
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

    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

}
