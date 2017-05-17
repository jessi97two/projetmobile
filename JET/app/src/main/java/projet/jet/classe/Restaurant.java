package projet.jet.classe;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.concurrent.ExecutionException;

import projet.jet.CommonsFunctions;

/**
 * Created by Jess on 16/05/2017.
 */
public class Restaurant implements Serializable {


    //Communication with PHP
    String txtReponse;
    String urlData = "http://172.20.10.3/projetmobile/data.php";
    String query;

    private String name;
    private String idGoogle;
    private String adresse;
    private String Phone;
    private int id;
    private String idUser;
    Context contextGlobal;
    public boolean isFavori;
    public boolean wait = false;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Restaurant(String idGoogle, String name, String adresse, String phone, String idUser) {
        this.idGoogle = idGoogle;
        this.name = name;
        this.adresse = adresse;
        this.idUser = idUser;
        this.Phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdGoogle() {
        return idGoogle;
    }

    public void setIdGoogle(String idGoogle) {
        this.idGoogle = idGoogle;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getId() {
        return id;
    }

    public void insert(Context context)
    {
        contextGlobal = context;
        new addRestaurant().execute();
    }
    public boolean isFavori(Context context)
    {
        contextGlobal = context;
        try {
            isFavori = new isFavori().execute().get();
        }
        catch(InterruptedException e){}
        catch (ExecutionException e){}
        return isFavori;
    }

    public void removeFavori(Context context)
    {
        contextGlobal = context;
        new removeRestaurantFromFavori().execute();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        return idGoogle.equals(that.idGoogle);

    }

    @Override
    public int hashCode() {
        return idGoogle.hashCode();
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", idGoogle='" + idGoogle + '\'' +
                ", adresse='" + adresse + '\'' +
                ", Phone='" + Phone + '\'' +
                '}';
    }

    /*public Restaurant getFavoris(Context context, GlobalApp ga)
    {
        ConnectivityManager cnMngr = (ConnectivityManager) ga.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                query = "action=getFavoris&iduser=" + ga.prefs.getString("id", "");
                URL url = new URL(urlData + "?" + query);
                Log.i("DEBUG CONNEXION", "url utilisée : " + url.toString());
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    InputStream in = null;
                    in = new BufferedInputStream(urlConnection.getInputStream());
                    txtReponse = CommonsFunctions.convertStreamToString(in);
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (txtReponse != "") {
            try {
                Log.i("DEBUG TEST REP", txtReponse);
                JSONObject jsonobj = new JSONObject(txtReponse);
                String val = jsonobj.getString("favoris");
                JSONArray json = new JSONArray(val);
                displayResult(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/
    private class addRestaurant extends AsyncTask<String, Integer, Boolean>{

        String txtReponse = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("DEBUG TEST","onPreExecute");
        }

        @Override
        protected Boolean doInBackground(String... args) {


            ConnectivityManager cnMngr = (ConnectivityManager) contextGlobal.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    query = "action=insertRestaurant" +
                            "&nom=" + name +
                            "&adresse=" + adresse +
                            "&contact=" + Phone +
                            "&idGoogle=" + idGoogle +
                            "&idUser=" + idUser;

                    query = query.replaceAll("\\s", "%20");
                    query = query.replaceAll("'", "%27");
                    query = Normalizer.normalize(query, Normalizer.Form.NFD);
                    query = query.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

                    URL url = new URL(urlData + "?" + query);
                    Log.i("DEBUG CONNEXION", "url utilisée : " + url.toString());
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
                } catch (MalformedURLException e1) {
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
            Log.i("DEBUG TEST","onPostExecute,  res =" + txtReponse);
        }
    }
    private class removeRestaurantFromFavori extends AsyncTask<String, Integer, Boolean>{

        String txtReponse = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("DEBUG TEST","onPreExecuteFromRemove");
        }

        @Override
        protected Boolean doInBackground(String... args) {


            ConnectivityManager cnMngr = (ConnectivityManager) contextGlobal.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    query = "action=removeRestaurant" +
                            "&idGoogle=" + idGoogle +
                            "&idUser=" + idUser;

                    query = query.replaceAll("\\s", "%20");
                    query = query.replaceAll("'", "%27");
                    query = Normalizer.normalize(query, Normalizer.Form.NFD);
                    query = query.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

                    URL url = new URL(urlData + "?" + query);
                    Log.i("DEBUG CONNEXION", "url utilisée : " + url.toString());
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
                } catch (MalformedURLException e1) {
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
            Log.i("DEBUG TEST","onPostExecuteFromRemove,  res =" + txtReponse);
        }
    }
    private class isFavori extends AsyncTask<String, Integer, Boolean>{

        String txtReponse = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("DEBUG TEST","onPreExecute");
        }

        @Override
        protected Boolean doInBackground(String... args) {

            boolean result = false;
            ConnectivityManager cnMngr = (ConnectivityManager) contextGlobal.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    query = "action=isFavori" +
                            "&idGoogle=" + idGoogle +
                            "&idUser=" + idUser;

                    query = query.replaceAll("\\s", "%20");
                    query = query.replaceAll("'", "%27");
                    query = Normalizer.normalize(query, Normalizer.Form.NFD);
                    query = query.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

                    URL url = new URL(urlData + "?" + query);
                    Log.i("DEBUG CONNEXION", "url utilisée : " + url.toString());
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream in = null;
                        in = new BufferedInputStream(urlConnection.getInputStream());
                        txtReponse = CommonsFunctions.convertStreamToString(in);
                        urlConnection.disconnect();
                        if (txtReponse != "") {
                            try {
                                Log.i("DEBUG TEST REP", txtReponse);
                                JSONObject jsonobj = new JSONObject(txtReponse);
                                result = jsonobj.getBoolean("isfavori");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return result;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

}
