package edu.sjsu.cmpe.partyon.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Party;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnCameraChangeListener{

    private static final int REQUEST_LOCATION = 5;
    private static final int REQUEST_RESOLVE_ERROR = 6;
    private GoogleMap mMap;
//    private com.google.android.gms.location.LocationListener mGMSLocationListener;
    private android.location.LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private final static long LOCATION_REFRESH_TIME = 6000; //6s
    private final static long LOCATION_FASTEST_REFRESH_TIME = 1000; //6s
    private final static int DEFAULT_ZOOM_DISTANCE = 16;
    private final static float LOCATION_MINI_DISTANCE = 0;//16.0f;
    private final static String TAG = "MapSearchActivity";
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mMapFragment;
    private Marker currLocationMarker;
    private MarkerOptions mUserLocationMarkerOptions;
    private LatLng latLng;
    private boolean mResolvingError;
    private boolean isCameraInitialized = false;
    private Map<String, Party> resultPartMap;
    private Button mReSearchBtn;
    private boolean isFirstSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        initGoogleApiClient();
        initMapLocationLister();
        initLocationManager();
        //initMarkerListener();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mReSearchBtn = (Button)findViewById(R.id.map_re_search_btn);

    }

    private void initLocationManager() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"checkSelfPermission location manager");
            // TODO: Consider calling

            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_MINI_DISTANCE, mLocationListener);
    }

    private void initMapLocationLister() {
        mLocationListener = new android.location.LocationListener(){
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG,"onLocationChanged:"+location.getLatitude()+"-"+location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d(TAG,"onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d(TAG,"onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d(TAG,"onProviderDisabled");
            }
        };
    }
    private void initMarkerListener(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "Looking for" + marker.getId());
                /*Party p = resultPartMap.get(marker.getPosition().latitude+""
                        +marker.getPosition().longitude);*/
                Party p = resultPartMap.get(marker.getId());
                Log.d(TAG, "clicked:" + p.getName());
                Intent intent = new Intent(MapSearchActivity.this, PartyDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AppData.OBJ_PARTY_ID, p.getObjectId().toString());
                bundle.putString(AppData.OBJ_PARTY_NAME, p.getName().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }
        });
        mReSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPartiesInCurrentBounds();
            }
        });
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,"onMapReady");
        mMap = googleMap;
        initMarkerListener();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng sydney2 = new LatLng(-32, 157);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG,"shouldShowRequestPermissionRationale");
                // Display UI and wait for user interaction
            } else {
                Log.d(TAG,"requestPermissions");
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
            Log.d(TAG, "checkSelfPermission1");
        }else {
            Log.d(TAG, "Permission is granted.");
        }
        mMap.setMyLocationEnabled(true);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Sydney2"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private synchronized void initGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    void getCurrentLocation() {
        Location myLocation = mMap.getMyLocation();
        if (myLocation != null) {
            double dLatitude = myLocation.getLatitude();
            double dLongitude = myLocation.getLongitude();
            Log.i("APPLICATION", " : " + dLatitude);
            Log.i("APPLICATION", " : " + dLongitude);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_geolocation);

            /*MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                    .title("Current Location")
                    .snippet("Thinking of finding some thing...")
                    .icon(icon);*/
            currLocationMarker = mMap.addMarker(new MarkerOptions().position(
                    new LatLng(dLatitude, dLongitude)).title("My Location").icon(icon));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 8));

        } else {
            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
        }

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult");
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
               /* Location myLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);*/
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,"onConnected");
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"checkSelfPermission");
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null && !isCameraInitialized) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mUserLocationMarkerOptions = new MarkerOptions();
            mUserLocationMarkerOptions.position(latLng);
            mUserLocationMarkerOptions.title("Current Position");
            mUserLocationMarkerOptions.snippet("This is a party description");
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_geolocation);
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mUserLocationMarkerOptions.icon(icon);
            currLocationMarker = mMap.addMarker(mUserLocationMarkerOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(DEFAULT_ZOOM_DISTANCE).build();

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            isCameraInitialized = true;
            mMap.setOnCameraChangeListener(this);

        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REFRESH_TIME); //5 seconds
        mLocationRequest.setFastestInterval(LOCATION_FASTEST_REFRESH_TIME); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        /*LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                new com.google.android.gms.location.LocationListener(){
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG,"onLocationChanged 1");
                //place marker at current position
                //mGoogleMap.clear();
                if (currLocationMarker != null) {
                    currLocationMarker.remove();
                }
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                currLocationMarker = mMap.addMarker(markerOptions);

                Toast.makeText(MapSearchActivity.this,"Location Changed",Toast.LENGTH_SHORT).show();

                //zoom to current position:
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(14).build();

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                //If you only need one location, unregister the listener
                //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        });*/

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }
    /**
     * android.location.Location.LocationListener
     * */
/*
    @Override
    public void onLocationChanged(Location location) {
        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mMap.addMarker(markerOptions);

        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
*/



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            Toast.makeText(this,"onConnectionFailed:"+connectionResult.getErrorCode(),Toast.LENGTH_SHORT).show();
            mResolvingError = true;
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if(isFirstSearch){
            searchPartiesInCurrentBounds();
            isFirstSearch = false;
        }else {
            mReSearchBtn.setVisibility(View.VISIBLE);
        }
        Log.d(TAG,"Camera moved to:"+cameraPosition.target.latitude+" - "+cameraPosition.target.longitude);
        Log.d(TAG,"Camera zoom:"+cameraPosition.zoom);
        Log.d(TAG,"cameraPosition:"+cameraPosition.toString());
    }
    private void searchPartiesInCurrentBounds(){
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        //Log.d(TAG,"looking Ps in " + distance + "from "+latitude+","+longitude);
        //ParseGeoPoint target = new ParseGeoPoint(latitude, longitude);
        ParseQuery<Party> query = ParseQuery.getQuery(AppData.OBJ_NAME_PARTY);
        query.whereWithinGeoBox("location",
                new ParseGeoPoint(bounds.southwest.latitude, bounds.southwest.longitude),
                new ParseGeoPoint(bounds.northeast.latitude, bounds.northeast.longitude));
        query.setLimit(10);
        final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);//BitmapDescriptorFactory.fromResource(R.drawable.ic_geolocation);

        query.findInBackground(new FindCallback<Party>() {

            @Override
            public void done(List<Party> objects, ParseException e) {
                Log.d(TAG,"get parties:"+objects.size());
                mMap.clear();
                mMap.addMarker(mUserLocationMarkerOptions);
                resultPartMap = new HashMap<String, Party>();
                //if(objects != null)
                for (Party p : objects){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(p.getLocation().getLatitude(), p.getLocation().getLongitude()));
                    markerOptions.title(p.getName());
                    markerOptions.snippet(p.getDescription());
                    //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    Log.d(TAG,p.getName());
                    markerOptions.icon(icon);
//                    resultPartMap.put(p.getLocation().getLatitude() + "" +
//                            p.getLocation().getLongitude(),p);
                    currLocationMarker = mMap.addMarker(markerOptions);
                    currLocationMarker.showInfoWindow();
                    Log.d(TAG,"adding ID:"+currLocationMarker.getId());
                    resultPartMap.put(currLocationMarker.getId(),p);
//                    resultPartMap.put(markerOptions.getPosition().latitude+""
//                            +markerOptions.getPosition().longitude,p);
                }
                mReSearchBtn.setVisibility(View.INVISIBLE);
            }
        });
    }
}
