/////////////////////////////////////////////////////////////////////////////////
//                                                                             //
//  Profile.java - RunForLife                                                  //
//              Source file containing Profile class                           //
//  Language:        Java                                                      //
//  Platform:        Android SDK                                               //
//  Course No.:      CIS-651                                                   //
//  Assignment No.:  Final Project                                             //
//  Author:          Ankur Pandey, SUID: 408067486, apandey@syr.edu            //
//                   Rupan Talwar, SUID: 402408828, rutalwar@syr.edu           //
//                                                                             //
/////////////////////////////////////////////////////////////////////////////////


package com.runforlife.runforlife;

//------------------------------------------------------------------------------
// Importing Files
//------------------------------------------------------------------------------
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.*;
import java.io.*;
import java.lang.*;

//------------------------------------------------------------------------------
// Class implementation
//------------------------------------------------------------------------------
public class ProfileView  extends Activity {


    private ImageButton settingProfileNav;
    private ImageButton settingHomeNav;
    private ImageButton settingLeaderNav;

    private String distanceValueT;
    private String paceValueT;
    private String caloriesValueT;
    private String elevationValueT;

    private String distanceValueL;
    private String paceValueL;
    private String caloriesValueL;
    private String elevationValueL;

    private String userNameL;
    private String userCityL;


    private TextView distanceValue;
    private TextView paceValue;
    private TextView caloriesValue;
    private TextView elevationValue;

    private TextView distanceValue1;
    private TextView paceValue1;
    private TextView caloriesValue1;
    private TextView elevationValue1;

    private TextView userName;
    private TextView userCity;

    //------------------------------------------------------------------------------
    // Progress Dialog
    //------------------------------------------------------------------------------
    private ProgressDialog pDialog;
    
    //------------------------------------------------------------------------------
    // Creating JSON Parser object
    //------------------------------------------------------------------------------
    JSONParser jParser = new JSONParser();


    //------------------------------------------------------------------------------
    // URL to get user details from get_user_profile.php
    //------------------------------------------------------------------------------
    private static String url_get_total_user_profile = "http://149.119.216.129:8888/get_user_profile.php";

    //------------------------------------------------------------------------------
    // HashMap to store array of data
    //------------------------------------------------------------------------------
    HashMap<String, String> totalmap = new HashMap<String, String>();
    HashMap<String, String> latestmap = new HashMap<String, String>();


    //------------------------------------------------------------------------------
    //JSON Node names
    //------------------------------------------------------------------------------
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS_TOT = "total";
    private static final String TAG_PRODUCTS_LAT = "latest";
    private static final String TAG_CITY = "city";
    private static final String TAG_NAME = "name";
    private static final String TAG_DIST = "distance";
    private static final String TAG_CAL = "calories";
    private static final String TAG_PACE = "pace";
    private static final String TAG_ELE = "elevation";

    //------------------------------------------------------------------------------
    //JSONArray
    //------------------------------------------------------------------------------
    JSONArray total = null;
    JSONArray latest = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_new);

        distanceValue = (TextView) findViewById(R.id.distanceValue);
        paceValue = (TextView) findViewById(R.id.paceValue);
        caloriesValue = (TextView) findViewById(R.id.caloriesValue);
        elevationValue = (TextView) findViewById(R.id.elevationValue);

        distanceValue1 = (TextView) findViewById(R.id.distanceValue1);
        paceValue1 = (TextView) findViewById(R.id.paceValue1);
        caloriesValue1 = (TextView) findViewById(R.id.caloriesValue1);
        elevationValue1 = (TextView) findViewById(R.id.elevationValue1);

        userName = (TextView) findViewById(R.id.userName);
        userCity = (TextView) findViewById(R.id.userCity);

        // new LoadAllProducts().execute();

        addListenerOnButton();
    }
    public void addListenerOnButton() {
        final Context context = this;
        settingProfileNav = (ImageButton) findViewById(R.id.userImageButton);
        settingProfileNav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(context, ProfileView.class);
                startActivity(intent2);
				ProfileView.this.finish();

            }
        });
        settingHomeNav = (ImageButton) findViewById(R.id.homeImageButton);
        settingHomeNav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(context, Home.class);
                startActivity(intent2);
			    ProfileView.this.finish();

            }
        });
        settingLeaderNav = (ImageButton) findViewById(R.id.leaderImageButton);
        settingLeaderNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(context, LeaderView.class);
                startActivity(intent2);
				ProfileView.this.finish();

            }
        });
    }

    //------------------------------------------------------------------------------
    // Background Async Task to Load all user data by making HTTP Request
    //------------------------------------------------------------------------------
    class LoadAllProducts extends AsyncTask<String, String, String> {

        //------------------------------------------------------------------------------
        // Before starting background thread Show Progress Dialog
        //------------------------------------------------------------------------------
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileView.this);
            pDialog.setMessage("Loading user Profile. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        //------------------------------------------------------------------------------
        // Getting All items from url
        //------------------------------------------------------------------------------
        protected String doInBackground(String... args) {
            SharedPreferences sharedpref = getSharedPreferences
                    (signsignupview.MyPREFERENCES, Context.MODE_PRIVATE);
            String email = sharedpref.getString("nameKey", "");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));

            //------------------------------------------------------------------------------
            // Getting JSON Object
            //------------------------------------------------------------------------------
            JSONObject json = jParser.makeHttpRequest(url_get_total_user_profile, "GET", params);
            Log.d("All Products: ", json.toString());

            try {
                //------------------------------------------------------------------------------
                // Check for success tag
                //------------------------------------------------------------------------------
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    
                    
                    total = json.getJSONArray(TAG_PRODUCTS_TOT);
                    latest = json.getJSONArray(TAG_PRODUCTS_LAT);
                    
                    JSONObject c = total.getJSONObject(0);
                    JSONObject cc = latest.getJSONObject(0);
                    
                    //------------------------------------------------------------------------------
                    // Storing each json item in variable
                    //------------------------------------------------------------------------------
                    distanceValueT = c.getString(TAG_DIST);
                    caloriesValueT = c.getString(TAG_CAL);
                    paceValueT = c.getString(TAG_PACE);
                    elevationValueT = c.getString(TAG_ELE);
                    userCityL = c.getString(TAG_CITY);
                    userNameL = c.getString(TAG_NAME);
					
                    distanceValueL = cc.getString(TAG_DIST);
                    caloriesValueL = cc.getString(TAG_CAL);
                    paceValueL = cc.getString(TAG_PACE);
                    elevationValueL = cc.getString(TAG_ELE);

                } else {
                    // no products found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        //------------------------------------------------------------------------------
        // After completing background task Dismiss the progress dialog
        //------------------------------------------------------------------------------
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            
            //------------------------------------------------------------------------------
            // updating UI from Background Thread
            //------------------------------------------------------------------------------
            runOnUiThread(new Runnable() {
                public void run() {
                    userName.setText("" + userNameL + "");
                    userCity.setText("" + userCityL + "");
                    distanceValue.setText("" + distanceValueL + " Distance");
                    caloriesValue.setText("" + caloriesValueL + " Calories");
                    paceValue.setText("" + paceValueL + " Pace");
                    elevationValue.setText("" + elevationValueL + " Eleva");
                    distanceValue1.setText("" + distanceValueT + " Distance");
                    caloriesValue1.setText("" + caloriesValueT + " Calories");
                    paceValue1.setText("" + paceValueT + " Pace");
                    elevationValue1.setText("" + elevationValueT + " Eleva");
                }
            });
        }
    }
}

