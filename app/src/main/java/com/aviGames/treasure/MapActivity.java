package com.aviGames.treasure;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

import java.util.Locale;


public class MapActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String TAG = MapActivity.class.getName();
    private GoogleMap mMap;
    MarkerOptions options = new MarkerOptions();
    Marker mark;        //this is the user value that appears on the map we can manipulate this
    Location treasureLocation = new Location("");       //tresurte chest locaiton
    TreasureChest chest = new TreasureChest();
    TreasureChest array[] = new TreasureChest[10];
    boolean firstLocation = true;
    Circle circle;
    int index = 0;
    TextView hint, timer, coinsText;
    TextView distanceText;

    int coins = 0;
    int statsCoins,statsChests;
    private static final long START_TIME_IN_MILLIS = 480000; //8minutes
    private long mTimeLeftInMilis = START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;
    boolean done = false;


    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        hint = (TextView) findViewById(R.id.HintText);
        timer = (TextView) findViewById(R.id.timerText);
        coinsText = (TextView) findViewById(R.id.coinsText);
        distanceText = (TextView) findViewById(R.id.textView);
        chooseCoins();
        coinsText.setText(String.valueOf(coins));
        loadStats();
        chooseIndex();
        setUpMapIfNeeded();
        setUpTreasureArray();
        chooseTime();
        startTimer();
        chest = array[index];
        // setUpTreasureChest(index);
        if (index >= 4)
            done = true;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


    }

    private void loadStats() { SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        statsCoins = pref.getInt("statsCoins", 0);
        statsChests=pref.getInt("statsChests",0);


    }

    private void chooseCoins() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        coins = pref.getInt("savedCoins", 0);
        Log.d(TAG, "choosecoin---------------->: " + coins);
    }

    private void chooseIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        index = pref.getInt("savedIndex", 0);
        //Log.d(TAG, "chooseIndex---------------->: " + index);
    }

    private void chooseTime() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        mTimeLeftInMilis = pref.getLong("savedTime", START_TIME_IN_MILLIS);
        Log.d(TAG, "choosetime---------------->: " + mTimeLeftInMilis);
    }

    private void startTimer() {

        mCountDownTimer = new CountDownTimer(mTimeLeftInMilis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMilis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timer.setText("00:00");
                showDistance();

            }
        }.start();

    }

    private void updateTimer() {
        int min = (int) (mTimeLeftInMilis / 1000) / 60;
        int sec = (int) (mTimeLeftInMilis / 1000) % 60;
        String time = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
        timer.setText(time);


    }

    private void resetTimer() {
        mCountDownTimer.cancel();
        mTimeLeftInMilis = START_TIME_IN_MILLIS;
        // chooseTime();
        startTimer();

    }

    //this sets up the array with the tresure chest locations in an array so we can pull an induvual chest and know where it is.
    private void setUpTreasureArray() {
        int i = 0;


        // The compass
        TreasureChest temp = new TreasureChest();
        temp.setLatitude(37.548135);
        temp.setLongitude(-77.453206);
        temp.setRingLatitude(37.548158);
        temp.setRingLongitude(-77.453170);
        temp.setRadius(100);
        temp.setHint("If you’re sailing on a pirate ship\n" +
                "To find your way you’ll need at least\n" +
                "This item that will help guide the way\n" +
                "By pointing north, south, west and east");
        temp.setFunFact("The Compass, a well-known landmark at VCU, was formerly the heart of the campus.");                              //TODO this is where funfact is called do this for the rest of the chests
        array[i] = temp;


        i++;


        //MONROE PARK
        TreasureChest temp2 = new TreasureChest();
        temp2.setLatitude(37.546779);
        temp2.setLongitude(-77.450401);
        temp2.setRingLatitude(37.546749);
        temp2.setRingLongitude(-77.450554);
        temp2.setRadius(150);
        temp2.setHint("There is a place we go for a walk\n" +
                "The children play and we can talk\n" +
                "Find this place if you want a lark\n" +
                "The answer you seek is in the Monroe...");
        temp2.setFunFact("Monroe Park is a 7.5 acres landscaped park located 1 mile (1.6 km) northwest of the Virginia State Capitol Building in Richmond, Virginia " +
                "and right infront of VCU's East Hall Engineering building. The park unofficially demarcates the eastern \"point\" of the Fan District \n" +
                "and is considered to be Richmond's oldest park.\n");
        array[i] = temp2;

        i++;



        // ram horns
        TreasureChest temp3 = new TreasureChest();
        temp3.setLatitude(37.546542);
        temp3.setLongitude(-77.453570);
        temp3.setRingLatitude(37.546555);
        temp3.setRingLongitude(-77.453596);
        temp3.setRadius(90);
        temp3.setHint("This is where the horns stand tall. Use your common sense");
        temp3.setFunFact("The Ram Horns is the symbol of VCU. During orientation," +
                "it is a tradition that all freshmen walk past the mythical horns and make a wish on them. " +
                "If the freshmen are then able to graduate, their wishes will come true.");
        array[i] = temp3;
        i++;

        //Richmond Howitzers Monument
        TreasureChest temp4 = new TreasureChest();
        temp4.setLatitude(37.548675);
        temp4.setLongitude(-77.454942);
        temp4.setRingLatitude(37.548673);
        temp4.setRingLongitude(-77.454941);
        temp4.setRadius(75);
        temp4.setHint("I stand tall even though my cannon is gone.");
        temp4.setFunFact("In the 1900s A sign next to the statue, placed there at some unknown date, seems to indicate that " +
                "the proper respect was not being shown by the local citizenry. The Richmond Howitzer Association " +
                "has offered a $10 reward \"for the arrest of any one throwing rocks into or trespassing on this park.\"");
        array[i] = temp4;


        i++;

        TreasureChest temp5 = new TreasureChest();
        temp5.setLatitude(37.548793);
        temp5.setLongitude(-77.452890);
        temp5.setRingLatitude(37.548791);
        temp5.setRingLongitude(-77.452894);
        temp5.setRadius(80);
        temp5.setHint("A small classroom. That doesn't have a room");
        temp5.setFunFact("Unveiled at the same time as this sculplure was the \"The Tableith\"  The " +
                "list of events imprinted on this sculpture led to " +
                "Richmond Professional Institute and the Medical " +
                "College of Virginia to combine into VCU." );
        array[i] = temp5;


        i++;
        TreasureChest temp6 = new TreasureChest();
        temp6.setLatitude(37.375317);
        temp6.setLongitude(-77.528775);
        temp6.setRingLatitude(37.375317);
        temp6.setRingLongitude(-77.528775);
        temp6.setRadius(50);
        temp6.setHint("hint5");
        temp6.setFunFact("tester5");
        array[i] = temp6;


        i++;


    }

    private void setUpTreasureChest(int index) {
        chest = array[index];
        resetTimer();
        Log.d(TAG, "setUpTreasureChest: setting u0p new chest number" + index);
    }

    public void profilePress(View view) {
        Intent startProfileScreen = new Intent(this, ProfileScreen.class);
        startActivity(startProfileScreen);
    }

    public void saveGame() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("savedIndex", index);
        editor.putLong("savedTime", mTimeLeftInMilis);
        editor.putInt("savedCoins", coins);
        editor.apply();
        Log.d(TAG, "backPress: we stored time-->" + mTimeLeftInMilis);
    }

    public void backPress(View view) {
        saveGame();
        //onBackPressed();
        finish();
    }

    @Override
    public void onBackPressed() {
        saveGame();
        finish();
    }
    public void showDistance() {
        distanceText.setVisibility(View.VISIBLE);
    }
    public void hideDistance() {
        distanceText.setVisibility(View.INVISIBLE);
    }

    //method toggles the hint text
    public void showHint(View View) {
        TextView text = findViewById(R.id.HintText);
        //check to see if its visible or not
        if (text.getVisibility() == View.VISIBLE) {
            text.setBackgroundColor(Color.TRANSPARENT);
            text.setVisibility(View.INVISIBLE);
        } else {
            text.setBackgroundColor(getResources().getColor(R.color.hint_color));
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
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "LOCATION not FOUNT ~~~~~~~~~~~~~~~ ");
        } else {
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

        Log.d(TAG, "handleNewLocation: options defined0000000 " + options.toString());
        Log.d(TAG, "handleNewLocation: last coordinanates " + coordinates.toString());
        mark.setPosition(coordinates);

        if (firstLocation)                                               //handles the first time a location is found and zooms in on it.
        {
            firstLocation = false;
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        }
        checkDistance(location);
    }

    //this method checks the distance from the current location to the location of the next chest
    private void checkDistance(Location location) {
        if (!done) {
            treasureLocation.setLatitude(chest.getLatitude());        //these are just for testing near me
            Log.d(TAG, "checkDistance: lat--" + chest.getLatitude());
            treasureLocation.setLongitude(chest.getLongitude());
            Log.d(TAG, "checkDistance: long---" + chest.getLongitude());
            Log.d(TAG, "checkDistance: curent chest locaiton " + treasureLocation.toString());
            distanceText.setText("Distance to Chest =" +location.distanceTo(treasureLocation) + "");
            Log.d(TAG, "checkDistance: distance to tresure ==" + location.distanceTo(treasureLocation));
            if (location.distanceTo(treasureLocation) <= 20) {                                                           //we have found a tresaure chest in meters
                int bonus = (int) ((mTimeLeftInMilis / 1000) / 1.5); //sec left / 1.5 as bous gold max = 200gold
                coins = coins + bonus + 100;
                hideDistance();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("statsCoins", statsCoins+coins);
                editor.putInt("statsChests", ++statsChests);
                editor.apply();

                if (index >= 4)        //if weve reched the end of the game
                {
                    done = true;
                    coinsText.setText(String.valueOf(coins));
                    // Toast toast = Toast.makeText(getApplicationContext(),"you found it", Toast.LENGTH_LONG);
                    //  toast.show();
                    // index++; //go to next index in treasure chest
                    Intent showFinalPopUp = new Intent(this, EndGamePopUp.class);   //this shows the new pop up with amomunt of bonus gold
                    showFinalPopUp.putExtra("bonus", bonus);
                    showFinalPopUp.putExtra("total", coins);
                    showFinalPopUp.putExtra("fact", chest.funFact);
                    startActivityForResult(showFinalPopUp, 1);
                    // startActivity(showFinalPopUp);
                    // finish();
                } else {

                    coinsText.setText(String.valueOf(coins));
                    //  Toast toast = Toast.makeText(getApplicationContext(),"you found it", Toast.LENGTH_LONG);
                    //   toast.show();
                    index++; //go to next index in treasure chest
                    String fact = chest.funFact;
                    setUpTreasureChest(index);
                    circle.setCenter(chest.getRingCoordinates());
                    circle.setRadius(chest.radius);
                    hint.setText(chest.hint);
                    Intent showPopUp = new Intent(this, popUp.class);   //this shows the new pop up with amomunt of bonus gold
                    showPopUp.putExtra("bonus", bonus);
                    showPopUp.putExtra("fact", fact);
                    startActivity(showPopUp);
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("savedIndex", index);
        editor.putLong("savedTime", mTimeLeftInMilis);
        editor.apply();

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged finction was called here ");
        handleNewLocation(location);
    }


    private void setUpMap() {

        Log.d(TAG, "setUpMap: this is ran------");
        mark = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //this creates the circle
        if (circle == null) {
            circle = mMap.addCircle(new CircleOptions()
                    .center(chest.getRingCoordinates())
                    .radius(chest.radius)
                    .strokeColor(Color.TRANSPARENT)
                    .fillColor(getApplicationContext().getResources().getColor(R.color.circleFill)));
            hint.setText(chest.hint);
        }


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
                //  setUpMap();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpMap();
    }

}
