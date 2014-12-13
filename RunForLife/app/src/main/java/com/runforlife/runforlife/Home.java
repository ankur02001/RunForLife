/////////////////////////////////////////////////////////////////////////////////
//                                                                             //
//  Home.java - RunForLife                                                     //
//              Source file containing Home class with Fragment Activity       //
//  Language:        Java                                                      //
//  Platform:        Android SDK                                               //
//  Course No.:      CIS-651                                                   //
//  Assignment No.:  Final Project                                             //
//  Author:          Ankur Pandey, SUID: 408067486, apandey@syr.edu            //
//                   Rupan Talwar, SUID: 402408828, rutalwar@syr.edu           //
// REFE:     www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql//
/////////////////////////////////////////////////////////////////////////////////

package com.runforlife.runforlife;

//------------------------------------------------------------------------------
// Importing Files
//------------------------------------------------------------------------------
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import android.location.Location;
import android.location.Criteria;
import android.location.LocationManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


//------------------------------------------------------------------------------
// Class implementation
//------------------------------------------------------------------------------
public class Home extends FragmentActivity implements ConnectionCallbacks,OnConnectionFailedListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static boolean isElevationinMeter = true;
    public static boolean isDistancein = true;
    public static String emailstore = "";
    private final String TAG = "RunForLife:HomePage:";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private LocationManager locationManager;
    private String provider;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    boolean calcualtionStart = false;
    boolean isCalcualtionStartFirstTime = true;

    private ImageButton startButton;
    private ImageButton pauseButton;

    private ImageButton settingProfileNav;
    private ImageButton settingHomeNav;
    private ImageButton settingLeaderNav;
    private ImageButton settingParameterNav;

    private TextView distanceValue;
    private TextView paceValue;
    private TextView caloriesValue;
    private TextView elevationValue;

    private Handler distanceHandler = new Handler();
    private Handler paceHandler = new Handler();
    private Handler caloriesHandler = new Handler();
    private Handler elevationHandler = new Handler();

    // Time values
    private long  lastTimeValuee= 0L;
    private long  currentTimeValuee =0L;
    long timeInMillisecondss = 0L;
    long timeSwapBufff = 0L;
    long updatedTimee = 0L;


    // distance initilization
    double current_latitude,current_longitude;

    private double betweenDistanceTraveled=0 ;
    private double totalDistanceTraveled =0;
    private double lastLongitude;
    private double currentLongitude;
    private double lastLatitude;
    private double currentLatitude;

    // time initilization
    private double beetweenTimeTraveled=0 ;
    private double lastTime;
    private double currentTime;

    // pace value
    private double maxpace = 0L;
    private double calculatedpace = 0L;

    // calorie value
    private double currentcalorieValue = 0;
    // Elevation values
    private double lastelevation= 0L;
    private double currentelevation= 0L;
    private double evelationValue= 0L;
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    //------------------------------------------------------------------------------
    // Home url to insert user profile
    //------------------------------------------------------------------------------
    // url to create new product
    private static String url_save = "http://149.119.216.129:8888/insert_user_profile.php";

    private static final String TAG_SUCCESS = "success";

    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    //------------------------------------------------------------------------------
    // Oncreate function called
    //------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setUpMapIfNeeded();

        timerValue = (TextView) findViewById(R.id.timerValue);
        startButton = (ImageButton) findViewById(R.id.startimageButton);
        distanceValue = (TextView) findViewById(R.id.distanceValue);
        paceValue = (TextView) findViewById(R.id.paceValue);
        caloriesValue = (TextView) findViewById(R.id.caloriesValue);
        elevationValue = (TextView) findViewById(R.id.elevationValue);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                calcualtionStart = true;

                //------------------------------------------------------------------------------
                // multiple thread for distance speed time pace and elevation
                //------------------------------------------------------------------------------
                customHandler.postDelayed(updateTimerThread, 0);
                distanceHandler.postDelayed(updateDistanceThread, 1000);
                caloriesHandler.postDelayed(updatecaloriesThread, 1000);
                elevationHandler.postDelayed(updateelevationThread,1000);
                paceHandler.postDelayed(updatepaceThread, 1000);
            }
        });

        pauseButton = (ImageButton) findViewById(R.id.pauseimageButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

                calcualtionStart = false;
                distanceHandler.removeCallbacks(updateDistanceThread);
                caloriesHandler.removeCallbacks(updatecaloriesThread);
                elevationHandler.removeCallbacks(updateelevationThread);
                paceHandler.removeCallbacks(updatepaceThread);
            }
           });

        SharedPreferences sharedpref = getSharedPreferences
                (signsignupview.MyPREFERENCES, Context.MODE_PRIVATE);
        emailstore = sharedpref.getString("nameKey", "");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
        }
        addListenerOnButton();
    }
    //------------------------------------------------------------------------------
    // Location update onconnected
    //------------------------------------------------------------------------------
    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    //------------------------------------------------------------------------------
    // Logout  from application
    //------------------------------------------------------------------------------

    public void logout(View view){
        SharedPreferences sharedpreferences = getSharedPreferences
                (signsignupview.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        //moveTaskToBack(true);
        super.onStop();
        System.exit(0);
    }

    public void exit(View view){
        moveTaskToBack(true);
        Home.this.finish();
   }

    //------------------------------------------------------------------------------
    // add listner button
    //------------------------------------------------------------------------------
    public void addListenerOnButton() {
        final Context context = this;
        settingProfileNav = (ImageButton) findViewById(R.id.userImageButton);
        settingProfileNav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                        Intent intent2 = new Intent(context, ProfileView.class);
                        startActivity(intent2);
                Home.this.finish();

            }
        });

       settingHomeNav = (ImageButton) findViewById(R.id.logoutImageButton);
        settingHomeNav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
               logout(arg0);
                Home.this.finish();
            }
        });

        settingLeaderNav = (ImageButton) findViewById(R.id.leaderImageButton);
        settingLeaderNav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(context, LeaderView.class);
                startActivity(intent2);
                Home.this.finish();
            }
        });


        settingParameterNav = (ImageButton) findViewById(R.id.settingsImageButton);
        settingParameterNav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(context, settingView.class);
                startActivity(intent2);
                Home.this.finish();
            }
        });


        ImageButton btn_save = (ImageButton) findViewById(R.id.saveImageButton);
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new saveProduct().execute();
               // Home.this.finish();
            }
        });

    }


    class saveProduct extends AsyncTask<String, String, String > {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Home.this);
            pDialog.setMessage("Saving...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        //------------------------------------------------------------------------------
        // background processing
        //------------------------------------------------------------------------------
            protected String doInBackground(String... args) {
            SharedPreferences sharedpref = getSharedPreferences
                    (signsignupview.MyPREFERENCES, Context.MODE_PRIVATE);
            String email = sharedpref.getString("nameKey", "");
            double dist = totalDistanceTraveled;
            String  distance= String.valueOf(dist);
            double cals = currentcalorieValue ;
            String calories = String.valueOf(cals);
            double pce = maxpace;
            String pace=String.valueOf(pce);
            double eln =  evelationValue;
            String elevation=String.valueOf(eln);


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("distance", distance));
            params.add(new BasicNameValuePair("calories", calories));
            params.add(new BasicNameValuePair("pace", pace));
            params.add(new BasicNameValuePair("elevation", elevation));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_save,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Handler handler =  new Handler(getApplicationContext().getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(), "Session details saved successfully.",Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    // failed to create product
                    Log.d("Data insertion ","failed");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }

    //------------------------------------------------------------------------------
    // on resume
    //------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    //------------------------------------------------------------------------------
    // set up map
    //------------------------------------------------------------------------------
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    //------------------------------------------------------------------------------
    // update Thread
    //------------------------------------------------------------------------------
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };
    private Runnable updateDistanceThread = new Runnable() {
        public void run() {
            distanceCalculation();
            customHandler.postDelayed(this, 1000);
        }
    };

    private Runnable updatecaloriesThread = new Runnable() {
        public void run() {
            calorieCalculation();
            caloriesHandler.postDelayed(this, 1000);
        }
    };
    private Runnable updateelevationThread = new Runnable() {
        public void run() {
            elevationCalculation();
            elevationHandler.postDelayed(this, 1000);
        }
    };
    private Runnable updatepaceThread = new Runnable() {
        public void run() {
            timeInMillisecondss = SystemClock.uptimeMillis() - startTime;
            updatedTimee = timeSwapBufff + timeInMillisecondss;
            int secs = (int) (updatedTimee / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTimee % 1000);
            currentTimeValuee = updatedTimee;
            paceCalculation();
            paceHandler.postDelayed(this, 1000);
        }
    };

    private static final int DECIMAL_PLACES = 2;
    private static java.math.BigDecimal twoDecimalPlaces(final double d) {
        return new java.math.BigDecimal(d).setScale(DECIMAL_PLACES,
                java.math.RoundingMode.HALF_UP);
    }
    private static final int Three_DECIMAL_PLACES = 3;
    private static java.math.BigDecimal threeDecimalPlaces(final double d) {
        return new java.math.BigDecimal(d).setScale(Three_DECIMAL_PLACES,
                java.math.RoundingMode.HALF_UP);
    }
    //------------------------------------------------------------------------------
    // Distance calculation
    //------------------------------------------------------------------------------
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    private void distanceCalculation(){

        double betweenDistanceTraveled = distance(currentLatitude,currentLongitude,lastLatitude,lastLongitude);

      //  Toast.makeText(getApplicationContext(),"betweenDistanceTraveled=" + betweenDistanceTraveled + "", Toast.LENGTH_LONG).show();
        betweenDistanceTraveled =  threeDecimalPlaces(betweenDistanceTraveled).doubleValue();

        totalDistanceTraveled = totalDistanceTraveled + betweenDistanceTraveled;
        // is in meter
        if(isDistancein) {
            distanceValue.setText("" + threeDecimalPlaces(totalDistanceTraveled) + " Dist(km)");
        }else{
            distanceValue.setText("" + (totalDistanceTraveled * 1000) + " Dist(m)");
        }
        lastLatitude = currentLatitude;
        lastLongitude = currentLongitude;
    }

    //------------------------------------------------------------------------------
    // Pace calculation
    //------------------------------------------------------------------------------
    private void paceCalculation(){
        beetweenTimeTraveled = currentTimeValuee - lastTimeValuee;
        double distanceTraveled = betweenDistanceTraveled * 1000;
        int secs = (int) (beetweenTimeTraveled / 1000);
        lastTimeValuee = currentTimeValuee;

    if(secs !=0) {
            calculatedpace = distanceTraveled / secs;
            paceValue.setText("" + twoDecimalPlaces(calculatedpace) + " Speed(mps)");
            if (calculatedpace > maxpace)
                maxpace = calculatedpace;
        }
    }

    //------------------------------------------------------------------------------
    // calorie calculation
    // REFERENCE:  http://www.nutristrategy.com/activitylist4.htm
    // avg weight taken 155 lbs 0r 70.03 kg
    //Exercise & Calories Burned per Hour
    // 1mph == 0.45 m/sec
    //------------------------------------------------------------------------------
    private void calorieCalculation(){
        //   http://www.nutristrategy.com/activitylist4.htm
        // avg weight taken 155 lbs 0r 70.03 kg
        //Exercise & Calories Burned per Hour
        // 1mph == 0.45 m/sec
        double calorieSet = 0;
        double paceinmeterperhour= (calculatedpace * 60 * 60) ; // meter/hour if calculatedpace is meter/sec
        double paceinmph = paceinmeterperhour * 2.24;
        double speed = paceinmph;

        if(speed <= 2.0){
            calorieSet =176;
        }else if(speed > 2.0 && speed<2.5){
            calorieSet =211;
        }else if(speed >=2.5 && speed < 3.0 ){
            calorieSet =211;
        }else if(speed >= 3.0 && speed < 3.5){
            calorieSet =232;
        }else if(speed >= 3.5 && speed < 4.0){
            calorieSet =267;
        }else if(speed >= 4.0 && speed <4.5){
            calorieSet =352;
        }else if(speed >= 4.5 && speed < 5){
            calorieSet =443;
        }else if(speed >= 5 && speed < 5.2){
            calorieSet = 563;
        }else if(speed >= 5.2 && speed < 6 ){
            calorieSet =633;
        }else if(speed >= 6 && speed < 6.7 ){
            calorieSet =704;
        }else if(speed >= 6.7 && speed < 7.0){
            calorieSet =774;
        }else if(speed >= 7.0 && speed <7.5){
            calorieSet =809;
        }else if(speed >= 7.5 && speed < 8.0){
            calorieSet = 880;
        }else if(speed >= 8 && speed < 8.6){
            calorieSet = 950;
        }else if(speed>= 8.6 && speed <9.0){
            calorieSet = 985;
        }else if(speed>=9.0 && speed < 10.0){
            calorieSet = 1056;
        }else if(speed >=10.0 && speed < 10.5){
            calorieSet =1126;
        }else{
            calorieSet =1267;
        }

        double calculatedCalorie =  calorieSet/(60*60);
        currentcalorieValue = currentcalorieValue + calculatedCalorie;
        caloriesValue.setText("" + twoDecimalPlaces(currentcalorieValue) + " Cal");
    }

    //------------------------------------------------------------------------------
    // elevation calculation
    //------------------------------------------------------------------------------
    private void elevationCalculation(){
        double calculatedElevation;

        if(lastelevation>currentelevation) {
            calculatedElevation = lastelevation - currentelevation;
        }else{
            calculatedElevation = currentelevation - lastelevation;
        }
        lastelevation = currentelevation;
        if(calculatedElevation < 10) {
            evelationValue = evelationValue + calculatedElevation;
            if(isElevationinMeter) {
                elevationValue.setText("" + threeDecimalPlaces(evelationValue) + " Elev(m)");
            }else{
               // evelationValue = evelationValue * 3.2808399 ; // meters to feet
                elevationValue.setText("" + threeDecimalPlaces(evelationValue) + " Elev(ft)");
            }

            if (lastelevation != currentelevation) {
                lastelevation = currentelevation;
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        current_latitude = (double) (location.getLatitude());
        current_longitude = (double) (location.getLongitude());
        currentelevation = (double) (location.getAltitude());
        if(isCalcualtionStartFirstTime){
            lastelevation = currentelevation;
            lastLatitude = current_latitude;
            lastLongitude  = current_longitude;
        }
        if( calcualtionStart || isCalcualtionStartFirstTime ) {

            mMap.addMarker(new MarkerOptions().position(new LatLng(current_latitude,current_longitude )).title("Marker"));
            CameraPosition newCamPos = new CameraPosition(new LatLng(current_latitude, current_longitude),
                    15.5f, mMap.getCameraPosition().tilt, //use old tilt
                    mMap.getCameraPosition().bearing); //use old bearing
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 4000, null);

            currentLongitude  = current_longitude;
            currentLatitude = current_latitude;
        }
        isCalcualtionStartFirstTime = false;
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "GoogleApiClient connection has been disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

}
