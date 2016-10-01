package edu.sjsu.cmpe.partyon.activities;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.sjsu.cmpe.partyon.R;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int REQUEST_LOCATION = 5;
    private static final int REQUEST_RESOLVE_ERROR = 6;
    private GoogleMap mMap;
//    private com.google.android.gms.location.LocationListener mGMSLocationListener;
    private android.location.LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private final static long LOCATION_REFRESH_TIME = 0;
    private final static float LOCATION_REFRESH_DISTANCE = 0;//16.0f;
    private final static String TAG = "MapSearchActivity";
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mMapFragment;
    private Marker currLocationMarker;
    private LatLng latLng;
    private boolean mResolvingError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        initGoogleApiClient();
        initMapLocationLister();
        initLocationManager();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG,"onConnected");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG,"onConnectionSuspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG,"onConnectionFailed");
                    }
                })
                .addApi(LocationServices.API)
                .build();
        initMap();*/
        /*mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "checkSelfPermission2");
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);*/
    }

    private void initLocationManager() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"checkSelfPermission location manager");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
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
        };/*new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "location changed:" + location.getLatitude() + location.getLongitude());
                //moveCamera(new LatLng(location.getLatitude(), location.getLongitude()));

            }

        };*/
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng sydney2 = new LatLng(-32, 157);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            //private static final int REQUEST_LOCATION = 2;

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
            Log.d(TAG, "try to get myLocation");

            Location myLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(myLocation != null)
                Log.d(TAG,"==>"+myLocation.getLongitude()+" :"+myLocation.getLatitude());
        }
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Sydney2"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private synchronized void initGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /*private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            //mMarker = mMap.addMarker(new MarkerOptions().position(loc));
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };*/


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
            mMap.addMarker(new MarkerOptions().position(
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
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //5 seconds
        mLocationRequest.setFastestInterval(500); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
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
        });

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
}
