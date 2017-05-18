package projet.jet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import projet.jet.R;
import projet.jet.classe.Restaurant;

/**
 * Created by Jess on 16/05/2017.
 */
public class RestaurantInfoViewActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback {

    private static final String LOG_TAG_MAP = "MAP :";
    private static final String LOG_TAG = "Restaurant infos act :";
    Restaurant restaurant;
    private GoogleMap map;
    UiSettings mapSettings;
    String placeId;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;
    LatLng queriedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_infos);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        TextView name = (TextView) findViewById(R.id.restaurant_infos_name);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView address = (TextView) findViewById(R.id.address);
        ImageView picture = (ImageView) findViewById(R.id.photoRestaurant);
        TextView note = (TextView)findViewById(R.id.note);


        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            restaurant = (Restaurant) b.get("restaurant");

            //RESTAURANT'S NAME
            name.setText(restaurant.getName());
            if (restaurant.getName().isEmpty()) {
                name.setVisibility(View.INVISIBLE);
            }

            //RESTAURANT'S PHONE NUMBER
            phone.setText("Contact : " + restaurant.getPhone());
            if (restaurant.getPhone().isEmpty()) {
                phone.setVisibility(View.INVISIBLE);
                findViewById(R.id.makeCall).setVisibility(View.INVISIBLE);
            }

            //RESTAURANT'S ADDRESS
            address.setText("Adresse : " + restaurant.getAdresse());
            if (restaurant.getAdresse().isEmpty()) {
                address.setVisibility(View.INVISIBLE);
            }

            //RESTAURANT'S NOTE
            note.setText("Note : "+restaurant.getNote());
            if (restaurant.getNote().isEmpty()) {
                note.setVisibility(View.INVISIBLE);
            }
        }

        ((ImageView)findViewById(R.id.makeCall)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextView phoneNum = (TextView) findViewById(R.id.phone);
                    String num = (String) phoneNum.getText().toString().replace("Contact : ","");
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+num));
                    startActivity(callIntent);
                }
                catch (SecurityException e){
                    Toast.makeText(getApplicationContext(),"CALL failed, please try again later!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getPlace(GoogleApiClient _mGoogleApiClient, final Restaurant _resto) {
        Places.GeoDataApi.getPlaceById(_mGoogleApiClient, _resto.getIdGoogle())
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            queriedLocation = myPlace.getLatLng();
                            Log.d("Latitude is", "" + queriedLocation.latitude);
                            Log.d("Longitude is", "" + queriedLocation.longitude);
                        }
                        places.release();
                        if (map != null) {
                            if(queriedLocation != null) {
                                map.addMarker(new MarkerOptions().position(queriedLocation).title(_resto.getName()));
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(queriedLocation, 12.0f));
                            }
                            /*try {

                                mapSettings = map.getUiSettings();
                                mapSettings.setScrollGesturesEnabled(true);
                                mapSettings.setZoomControlsEnabled(true);
                            } catch (SecurityException e) {
                            }*/
                        }
                    }
                });
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
    public void onConnected(Bundle bundle) {
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(LOG_TAG_MAP, "onMapReady()");
        map = googleMap;
        setUpMap();
        this.getPlace(mGoogleApiClient, restaurant);
    }
    private void setUpMap() {
        try {
            map.setMyLocationEnabled(true);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.getUiSettings().setMapToolbarEnabled(false);
        }
        catch(SecurityException e)
        {
            Log.e(LOG_TAG_MAP, "Erreur in setUpMap()");
        }


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                Log.d(LOG_TAG_MAP, "MapClick");

                //remove previously placed Marker
                if (marker != null) {
                    marker.remove();
                }

                //place marker where user just clicked
                marker = map.addMarker(new MarkerOptions().position(point).title("Point")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                Log.d(LOG_TAG_MAP, "MapClick After Add Marker");

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            Log.d(LOG_TAG, "onActivityResult " + data.getStringExtra("result"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "onResume");
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            Log.d(LOG_TAG_MAP, "setUpMapIfNeeded");
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(this);
        }
    }
}
