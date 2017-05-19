package projet.jet.activity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.adapter.CustomListviewRestaurantAdapter;
import projet.jet.adapter.PlaceArrayAdapter;
import projet.jet.classe.Restaurant;

/**
 * Created by Jess on 18/05/2017.
 */
public class RestaurantSearchActivity extends AppCompatActivity implements
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
    private Toolbar toolbar;

    GlobalApp ga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);

        ga = (GlobalApp) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.AutoCompRestaurantName);
        mAutocompleteTextView.setThreshold(3);
        //mNameTextView = (TextView) findViewById(R.id.name);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        listViewSearch = (ListView) findViewById(R.id.listSearchRestaurant);

        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) listViewSearch.getItemAtPosition(position);
                Toast.makeText(RestaurantSearchActivity.this, "Vous avez cliqué sur : " + selectedItem,
                        Toast.LENGTH_SHORT).show();

                Restaurant viewRestaurant = (Restaurant) correspondSeachRestaurant.get(selectedItem);
                viewRestaurant.insert(RestaurantSearchActivity.this);
                //new CheckFavoris().execute();
            }
        });


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

            //mGoogleApiClient.disconnect();
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
                        ga.prefs.getString("id", ""),
                        Float.toString(place.getRating()));
                restaurantName.add(newRestaurant.getName());
                correspondSeachRestaurant.put(newRestaurant.getName(), newRestaurant);
            }

            CustomListviewRestaurantAdapter adapter = new CustomListviewRestaurantAdapter(RestaurantSearchActivity.this, restaurantName, correspondSeachRestaurant);
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

}
