package projet.jet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
 * Created by Jess on 09/12/2016.
 */
public class UserFunctions {

    String qs;
    ConnectivityManager cmg;
    String txtReponse;
    GlobalApp glapp;

    public void AddNewLien(String s,ConnectivityManager cm, String iduser, GlobalApp glpapp) {
        String nom = s.split(" ")[0];
        String prenom =s.split(" ")[1].split(" ")[0];
        String login = s.split(" : ")[1];
        qs = "action=ajoutami" + "&iduser=" + iduser + "&nom=" + nom + "&prenom=" + prenom + "&login=" + login;
        cmg = cm;
        glapp = glpapp;
        new NetCheck().execute();
    }

    private void getResultat(String res) {
        if(res != "") {
            try {
                Log.i("DEBUG TEST REP", res);
                JSONObject json = new JSONObject(res);
                if(Integer.parseInt(json.getString("statutajout")) == 0) {
                    Toast.makeText(glapp.getApplicationContext(),"Following failed",Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(json.getString("statutajout")) == 1) {
                    Toast.makeText(glapp.getApplicationContext(),"Following successful",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class NetCheck extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            NetworkInfo netInfo = cmg.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    String urlData = "http://192.168.1.12/2i/APP2/projetmobile/data.php";
                    URL url = new URL(urlData + "?" + qs );
                    Log.i("DEBUG AJOUT EN AMI","url utilisée : " + url.toString());
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
                getResultat(txtReponse);
            }
        }

    }

}
