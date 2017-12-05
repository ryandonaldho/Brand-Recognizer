package brand.brandrecognizer;


import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by Hersh on 11/27/2017.
 */

public class   MapFragment extends Fragment implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setPadding(0,120,0,0);

            //init();
            //use mMap.getUiSettings().
            //for different options.
        }
    }

    public void setPadding(View view){
        mMap.setPadding(0,100, 0, 0);
    }

    private static final String TAG = "MapActivity ";
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //private EditText mSearchText = (EditText) findViewById(R.id.input_search);

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationPRoviderClient;

    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;


    Object dataTransfer[] = new Object[2];
    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
    String brand;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map);
        //setContentView(R.layout.fragment_map);
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        getLocationPermission();

        return v;
        //init();
    }

    private int checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this.getActivity(), permission);
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getting the device current locaiton");
        mFusedLocationPRoviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        try{
            if(mLocationPermissionsGranted){
                //error with getLastLocation returning NULL
                //will work if another app is returning before hand using location services.
                final Task location = mFusedLocationPRoviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();


                            if (currentLocation != null) {
                                longitude = currentLocation.getLongitude();
                                latitude = currentLocation.getLatitude();

                                String url = getUrl(latitude, longitude, brand);

                                dataTransfer[0] = mMap;
                                dataTransfer[1] = url;

                                System.out.println(url);

                                getNearbyPlacesData.execute(dataTransfer);

                            }

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

                        } else {
                            Log.d(TAG, "onComplete: current Location is NULL");
                            //Toast.makeText(MapFragment.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }
    private void initMap(){
        Log.d(TAG,"initMap: initialize map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapFragment.this);
    }
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermissions: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getActivity(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }
        }else{
            ActivityCompat.requestPermissions(this.getActivity(),
                    permissions,LOCATION_PERMISSION_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResults: called");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE :{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++) {
                        mLocationPermissionsGranted = false;
                        Log.d(TAG, "onRequestPermissionsResults: permissions failed");
                        return;
                    }
                    Log.d(TAG, "onRequestPermissionsResults: permissions granted");
                    mLocationPermissionsGranted = true;
                    //initialize map
                    initMap();

                }
            }
        }
    }


    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {
        Bundle bundle = this.getArguments();
        brand = bundle.getString("brand");

        brand =  brand.replaceAll("\\s+","+");

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+"store");
        googlePlaceUrl.append("&keyword="+ brand);
        googlePlaceUrl.append("&key="+"AIzaSyAQ0tHxeLZE3pElkuH5xiTl12lrBhMoSlw");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

}
