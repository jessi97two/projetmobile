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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import projet.jet.CommonsFunctions;
import projet.jet.ConnectFunctions;
import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.activity.RestActivity;

/**
 * Created by Jess on 03/04/2017.
 */
public class HomeFragment extends Fragment {

    protected GlobalApp ga;
    RestActivity restAct;
    private ListView mListViewSondages, mListView2;

    @Override
    public void onStart() {
        super.onStart();
        restAct = new RestActivity();
        Activity a = getActivity();
        ga = (GlobalApp) a.getApplication();
        String qs = "action=getSondagesReceived&iduser=";
        restAct.envoiRequete(qs+ ga.prefs.getString("id",""),"getSondagesReceived",ga,this.getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_home_fragment,container,false);

        Activity a = getActivity();

        mListViewSondages = (ListView)v.findViewById(R.id.listViewSondages);
        mListView2 = (ListView)v.findViewById(R.id.listViewInvitations);

        return v;
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

    public void displayResult(String type , JSONArray jsonarray) {

        Activity a = getActivity();
        ArrayList<String> result = new ArrayList<String>();

        if(type.equals("sondages")) {
            try {
                for (int l=0; l < jsonarray.length(); l++) {
                    result.add(jsonarray.getJSONObject(l).getString("titre")  );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(a, android.R.layout.simple_list_item_1, result); // simple textview for list item

            mListViewSondages.setAdapter(adapter);
        }


    }
}
