package projet.jet.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

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
import java.util.HashMap;
import java.util.Map;

import projet.jet.CommonsFunctions;
import projet.jet.GeneralActivity;
import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.adapter.CustomListviewRestaurantAdapter;
import projet.jet.adapter.PlaceArrayAdapter;
import projet.jet.classe.Restaurant;

/**
 * Created by Jess on 16/05/2017.
 */
public class RestaurantsActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    final LatLngBounds BOUNDS_MOUNTAIN_VIEW= new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private static final String LOG_TAG = "Restaurant Activity :";
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView mNameTextView;
    ArrayList<String> restaurantName = new ArrayList<String>();
    int index = 0;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    Map correspondSeachRestaurant = new HashMap();
    Map correspondFavoriRestaurant = new HashMap();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    ListView listViewSearch;
    ListView listViewFavoris;

    ImageButton imgBtnBackToGeneral;
    ImageButton imgBtnGoToSearch;
    GlobalApp ga;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite_restaurant);

        ga = (GlobalApp) getApplication();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    /*    mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.AutoCompRestaurantName);
        mAutocompleteTextView.setThreshold(3);
        //mNameTextView = (TextView) findViewById(R.id.name);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        imgBtnBackToGeneral = (ImageButton) findViewById((R.id.imageButtonBackToGeneral));
        imgBtnGoToSearch = (ImageButton) findViewById(R.id.imageButtonSearchRestaurant);
    */

        listViewSearch = (ListView) findViewById(R.id.listSearchRestaurant);
        listViewFavoris = (ListView) findViewById(R.id.listViewFavoris);



    /*    imgBtnBackToGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoGeneralPage = new Intent(RestaurantsActivity.this, GeneralActivity.class);
                startActivity(gotoGeneralPage);
                finish();

            }
        });
    */

        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) listViewSearch.getItemAtPosition(position);
                Toast.makeText(RestaurantsActivity.this, "Vous avez cliqué sur : " + selectedItem,
                        Toast.LENGTH_SHORT).show();

                Restaurant viewRestaurant = (Restaurant) correspondSeachRestaurant.get(selectedItem);
                viewRestaurant.insert(RestaurantsActivity.this);
                new CheckFavoris().execute();
            }
        });

        listViewFavoris.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) listViewFavoris.getItemAtPosition(position);
                Toast.makeText(RestaurantsActivity.this, "Vous avez cliqué sur le favori : " + selectedItem,
                        Toast.LENGTH_SHORT).show();

                Restaurant favoriRestaurant = (Restaurant) correspondFavoriRestaurant.get(selectedItem);

                Intent gotoRestaurantInfoView = new Intent(RestaurantsActivity.this, RestaurantInfoViewActivity.class);
                /*Bundle b = new Bundle();
                b.putSerializable("restaurant",favoriRestaurant);*/
                gotoRestaurantInfoView.putExtra("restaurant", favoriRestaurant);
                //gotoRestaurantInfoView.putExtras(b);
                startActivity(gotoRestaurantInfoView);
            }
        });
    /*
        imgBtnGoToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Click sur SEARCH");
            }
        });

        new CheckFavoris().execute();
    */

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            //SET RESULT IN ACTIVITY
            //mNameTextView.setText(Html.fromHtml(place.getName() + ""));

            mGoogleApiClient.disconnect();
            if (!restaurantName.contains(place.getName().toString()))
            {
                Restaurant newRestaurant = new Restaurant(
                        place.getId().toString(),
                        place.getName().toString(),
                        place.getAddress().toString(),
                        place.getPhoneNumber().toString(),
                        //TO ADD
                        //place.getRating()
                        //place.getWebsiteUri()
                        ga.prefs.getString("id", ""));
                restaurantName.add(newRestaurant.getName());
                correspondSeachRestaurant.put(newRestaurant.getName(), newRestaurant);
            }

            CustomListviewRestaurantAdapter adapter = new CustomListviewRestaurantAdapter(RestaurantsActivity.this, restaurantName, correspondSeachRestaurant);
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Favorite_restaurant.this, R.layout.activity_search_restaurant_listview,restaurantName);
            listViewSearch.setAdapter(adapter);
        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    private class CheckFavoris extends AsyncTask<String, Integer, Boolean> {

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
                    String qs = "action=getFavoris&iduser=" + ga.prefs.getString("id", "");
                    URL url = new URL(urlData + "?" + qs);
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
            if (th == true) {
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
            }
        }
    }


    private void displayResult(JSONArray jsonarray) {

        ArrayList<String> result = new ArrayList<String>();

        try {
            for (int l=0; l < jsonarray.length(); l++) {
                Restaurant resto = new Restaurant(
                        jsonarray.getJSONObject(l).getString("idGoogle"),
                        jsonarray.getJSONObject(l).getString("nom"),
                        jsonarray.getJSONObject(l).getString("adresse"),
                        jsonarray.getJSONObject(l).getString("contact"),
                        ga.prefs.getString("id", "")
                );
                correspondFavoriRestaurant.put(resto.getName(), resto);
                result.add(jsonarray.getJSONObject(l).getString("nom"));
                Log.i("Debug resto rep :", jsonarray.getJSONObject(l).getString("nom"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_favorite_restaurant_listview, result);
        //CustomListviewRestaurantAdapter adapter = new CustomListviewRestaurantAdapter(Favorite_restaurant.this, result);
        listViewFavoris.setAdapter(adapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_search) {

        }

        return super.onOptionsItemSelected(item);
    }
}

