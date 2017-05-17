package projet.jet;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

/**
 * Created by Jess on 22/12/2016.
 */
public class UserFriendsActivity extends Activity {

    ImageButton imgBtnBackToGeneral;
    ImageButton imgBtnGoToSearch;
    GlobalApp ga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friends_list);
        ga = (GlobalApp) getApplication();

        imgBtnBackToGeneral = (ImageButton) findViewById((R.id.imageButtonBackToGeneral));
        imgBtnGoToSearch = (ImageButton) findViewById(R.id.imageButtonSearchAmongFriends);

        imgBtnBackToGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoGeneralPage = new Intent(UserFriendsActivity.this, GeneralActivity.class);
                startActivity(gotoGeneralPage);
                finish();
            }
        });

        imgBtnGoToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        new CheckFriend().execute();
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
                    String urlData = "http://192.168.43.120/2i/APP2/projetmobile/data.php";
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
                        JSONArray json = new JSONArray(val);
                        displayResult(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void displayResult(JSONArray jsonarray) {

        ArrayList<String> result = new ArrayList<String>();

        try {
                for (int l=0; l < jsonarray.length(); l++) {
                    result.add(jsonarray.getJSONObject(l).getString("nom") + " " + jsonarray.getJSONObject(l).getString("prenom")
                        + " : " + jsonarray.getJSONObject(l).getString("login") );
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_user_friends_listview, result); // simple textview for list item
        ListView listView = (ListView) findViewById(R.id.listViewFriends);
        listView.setAdapter(adapter);

    }

}
