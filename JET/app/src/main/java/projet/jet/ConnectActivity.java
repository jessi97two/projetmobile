package projet.jet;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
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
import java.util.List;

/**
 * Created by Jess on 22/12/2016.
 */
public class ConnectActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String rech;
    GlobalApp ga;
    private ArrayList<String> listFollowers = new ArrayList<String>();
    private String resResearchFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ga = (GlobalApp) getApplication();

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    public void onListItemClick(ListView l,View v, int position, long id) {
        // call detail activity for clicked entry
    }


    private void doSearch(String querySearch) {
        // get a Cursor, prepare the ListAdapter
        // and set it
        if(querySearch != null) {
            rech = querySearch;
            new NetCheck().execute();
        }

    }

    private void displayResult(String jsonobj, List<String> listFriends) {
        ListFriendsSearchFragment listFriendsSearchFragment = new ListFriendsSearchFragment();
        Bundle bdl = new Bundle();
        bdl.putString("json_data", jsonobj.toString());
        bdl.putString("json_list_data", listFriends.toString());
        listFriendsSearchFragment.setArguments(bdl);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameconnect,listFriendsSearchFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_connect, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    private class NetCheck extends AsyncTask<String, Integer, Boolean> {

        String txtReponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    String urlData = "http://192.168.1.12/2i/APP2/projetmobile/data.php";
                    String qs = "action=recherchefriends" + "&search=" + rech;
                    URL url = new URL(urlData + "?" + qs );
                    Log.i("DEBUG CONNEXION","url utilisée : " + url.toString());
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream in = null;
                        in = new BufferedInputStream(urlConnection.getInputStream());
                        txtReponse = CommonsFunctions.convertStreamToString(in);
                        urlConnection.disconnect();
                        return Boolean.TRUE;
                    }
                }
                catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return Boolean.FALSE;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if(th == true){
                if(txtReponse != "") {
                    try {
                        Log.i("DEBUG TEST REP", txtReponse);
                        JSONObject json = new JSONObject(txtReponse);
                        Log.i("DEBUG TEST",json.get("resrecherchefriend").toString());
                        resResearchFriends = json.get("resrecherchefriend").toString();
                    //    displayResult(json.get("resrecherchefriend").toString());
                        new CheckFriend().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class CheckFriend extends AsyncTask<String, Integer, Boolean> {

        String txtReponse;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    String urlData = "http://192.168.1.12/2i/APP2/projetmobile/data.php";
                    String qs = "action=getFriends&iduser=" + ga.prefs.getString("id","");
                    URL url = new URL(urlData + "?" + qs );
                    Log.i("DEBUG CONNEXION","url utilisée : " + url.toString());
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream in = null;
                        in = new BufferedInputStream(urlConnection.getInputStream());
                        txtReponse = CommonsFunctions.convertStreamToString(in);
                        urlConnection.disconnect();
                        return Boolean.TRUE;
                    }
                }
                catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return Boolean.FALSE;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if(th == true){
                if(txtReponse != "") {
                    try {
                        Log.i("DEBUG TEST REP", txtReponse);
                        JSONObject jsonobj = new JSONObject(txtReponse);
                        String val = jsonobj.getString("followers");
                        if(!(val.equals("0"))) {
                            JSONArray json = new JSONArray(val);
                            for(int i=0; i<json.length(); i++) {
                                JSONObject obj = new JSONObject(json.get(i).toString());
                                listFollowers.add(obj.get("nom") + " " + obj.get("prenom") + " : " + obj.get("login"));
                            }
                        }
                        displayResult(resResearchFriends,listFollowers);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
