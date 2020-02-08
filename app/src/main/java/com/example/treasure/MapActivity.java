package com.example.treasure;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String TAG = MapActivity.class.getName();
    private GoogleMap mMap;
    MarkerOptions options = new MarkerOptions();
    Marker mark;        //this is the user value that appears on the map we can manipulate this
    Location treasureLocation = new Location("");       //tresurte chest locaiton
    TreasureChest chest = new TreasureChest();
    boolean firstLocation = true;
    Circle circle;
   // private MarkerOptions options = new MarkerOptions();


    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();
        setUpTreasureChest();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1*1000); // 1 second, in milliseconds


    }

    private void setUpTreasureChest() {

        chest.setLatitude(37.375010);
        chest.setLongitude( -77.529626);
        chest.setRingLatitude(37.374334);
        chest.setRingLongitude(-77.529381);
       chest.setRadius(150);
      //  treasureLocation.setLatitude(37.375010);        //these are just for testing near me
     //   treasureLocation.setLongitude( -77.529626);


    }

    public void profilePress(View view){
    Intent startProfileScreen = new Intent(this, ProfileScreen.class);
    startActivity(startProfileScreen);
}
public void backPress(View view)
{
    onBackPressed();
}
//method toggles the hint text
    public void showHint(View View){
        TextView text =  findViewById(R.id.HintText);
        //check to see if its visible or not
        if(text.getVisibility() == View.VISIBLE)
        {
            text.setVisibility(View.INVISIBLE);
        }
        else {
            text.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: connection failed");

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.d(TAG, "onPointerCaptureChanged: ");

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location service was connected.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location==null)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "LOCATION not FOUNT ~~~~~~~~~~~~~~~ ");
        }
        else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "LOCATION FOUNT ----------------------> " + location.toString());
            handleNewLocation(location);
        }

    }
    //mathod where we recive a new location
    private void handleNewLocation(Location location) {
        Log.d(TAG, "new location found-----" + location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng coordinates = new LatLng(currentLatitude, currentLongitude); //combine lat and long into a silgle variable

        Log.d(TAG, "handleNewLocation: options defined0000000 " +options.toString() );
        Log.d(TAG, "handleNewLocation: last coordinanates " + coordinates.toString());
        mark.setPosition(coordinates);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        if(firstLocation)                                               //handles the first time a location is found and zooms in on it.
        {
            firstLocation=false;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        }
        checkDistance(location);
    }

    //this method checks the distance from the current location to the location of the next chest
    private void checkDistance(Location location) {
        treasureLocation.setLatitude(chest.getLatitude());        //these are just for testing near me
        Log.d(TAG, "checkDistance: lat--"+ chest.getLatitude());
        treasureLocation.setLongitude( chest.getLongitude());
        Log.d(TAG, "checkDistance: long---"+ chest.getLongitude());
        Log.d(TAG, "checkDistance: curent chest locaiton " + treasureLocation.toString());
        TextView tester = (TextView)findViewById(R.id.textView);
        tester.setText(location.distanceTo(treasureLocation) +"");
        Log.d(TAG, "checkDistance: distance to tresure =="+ location.distanceTo(treasureLocation) );
        if(location.distanceTo(treasureLocation)<= 10 ){
            Toast toast = Toast.makeText(getApplicationContext(),"you found it", Toast.LENGTH_LONG);
            toast.show();
        }


    }


    //if connection is somehow lost
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services was suspended.");
    }

    //when app is opened again
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }
    //when app is tabbed out
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged finction was called here ");
        handleNewLocation(location);
    }



    private void setUpMap() {
        mark = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //this creates the circle
        circle = mMap.addCircle(new CircleOptions()
                                .center(chest.getRingCoordinates())
                                .radius(chest.radius)
                                .strokeColor(Color.TRANSPARENT)
                                .fillColor(getApplicationContext().getResources().getColor(R.color.circleFill)));



    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
           // mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

            SupportMapFragment mapFrag = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            mapFrag.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpMap();
    }

}
