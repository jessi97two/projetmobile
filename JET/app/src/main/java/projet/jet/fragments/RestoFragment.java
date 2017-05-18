package projet.jet.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Map;

import projet.jet.CommonsFunctions;
import projet.jet.GeneralActivity;
import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.activity.RestaurantInfoViewActivity;
import projet.jet.activity.RestaurantsActivity;
import projet.jet.adapter.CustomListviewRestaurantAdapter;
import projet.jet.adapter.PlaceArrayAdapter;
import projet.jet.classe.Restaurant;


/**
 * Created by Jess on 03/04/2017.
 */
public class RestoFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

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
    //public ListView listViewSearch;
    ListView listViewFavoris;

    ImageButton imgBtnBackToGeneral;
    ImageButton imgBtnGoToSearch;
    public GlobalApp ga;

    public Activity a ;

    @Override
    public void onStart() {
        super.onStart();
        this.updateListFavori();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_favorite_restaurant,container,false);

        a  = getActivity();
        ga = (GlobalApp) a.getApplication();

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


        //TODO : AutoCompleteTextView


        mGoogleApiClient = new GoogleApiClient
                .Builder(a)
                .addApi(Places.GEO_DATA_API)
                //.enableAutoManage(a, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        listViewFavoris = (ListView) v.findViewById(R.id.listViewFavoris);

        listViewFavoris.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) listViewFavoris.getItemAtPosition(position);
                Restaurant favoriRestaurant = (Restaurant) correspondFavoriRestaurant.get(selectedItem);
                Intent gotoRestaurantInfoView = new Intent(a, RestaurantInfoViewActivity.class);
                gotoRestaurantInfoView.putExtra("restaurant", favoriRestaurant);
                startActivity(gotoRestaurantInfoView);
            }
        });

        this.updateListFavori();
        return v;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                return;
            }

            // Selecting the first object buffer.
            final Place place = places.get(0);

            mGoogleApiClient.disconnect();

            //SET RESULT IN ACTIVITY
            if (!restaurantName.contains(place.getName().toString()))
            {
                Restaurant newRestaurant = new Restaurant(
                        place.getId().toString(),
                        place.getName().toString(),
                        place.getAddress().toString(),
                        place.getPhoneNumber().toString(),
                        //TODO: ADD
                        //place.getWebsiteUri()
                        ga.prefs.getString("id", ""),
                        Float.toString(place.getRating()));
                restaurantName.add(newRestaurant.getName());
                correspondSeachRestaurant.put(newRestaurant.getName(), newRestaurant);
            }

           // CustomListviewRestaurantAdapter adapter = new CustomListviewRestaurantAdapter(a, restaurantName, correspondSeachRestaurant);
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Favorite_restaurant.this, R.layout.activity_search_restaurant_listview,restaurantName);
            //listViewSearch.setAdapter(adapter);
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
            Toast.makeText(a, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(a, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(a,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }
    public void updateListFavori()
    {
        new CheckFavoris().execute();
    }

    private class CheckFavoris extends AsyncTask<String, Integer, Boolean> {

        String txtReponse;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ConnectivityManager cnMngr = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                        ga.prefs.getString("id", ""),
                        jsonarray.getJSONObject(l).getString("note")
                );
                correspondFavoriRestaurant.put(resto.getName(), resto);
                result.add(jsonarray.getJSONObject(l).getString("nom"));
                Log.i("Debug resto rep :", jsonarray.getJSONObject(l).getString("nom"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(a, R.layout.activity_favorite_restaurant_listview, result);
        listViewFavoris.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_search) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        a.getMenuInflater().inflate(R.menu.menu_main, menu);

        AutoCompleteTextView mAutocompleteTextView;

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        mAutocompleteTextView = (AutoCompleteTextView) myActionMenuItem.getActionView();
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceArrayAdapter = new PlaceArrayAdapter(a, android.R.layout.simple_list_item_1,BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

    }

}


