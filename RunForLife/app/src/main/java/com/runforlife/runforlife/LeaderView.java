/////////////////////////////////////////////////////////////////////////////////
//                                                                             //
//  LeaderView.java - RunForLife                                               //
//              Source file containing Leader class                            //
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.ImageButton;
import android.widget.ImageButton;

import android.widget.ImageButton;

//------------------------------------------------------------------------------
// Class implementation
//------------------------------------------------------------------------------
public class LeaderView extends Activity {

    private ImageButton settingProfileNav;
    private ImageButton settingHomeNav;
    private ImageButton settingLeaderNav;

    private String caloriesValueT;
    private String userNameT;
    private TextView caloriesValue;
    private TextView userName;

    //------------------------------------------------------------------------------
    // Progress Dialog
    //------------------------------------------------------------------------------
    private ProgressDialog pDialog;
    
    //------------------------------------------------------------------------------
    // Creating JSON Parser object
    //------------------------------------------------------------------------------
    JSONParser jParser = new JSONParser();

    //------------------------------------------------------------------------------
    // URL to get leader board details from get_leader.php
    //------------------------------------------------------------------------------
    private static String url_get_leader = "http://149.119.216.129:8888/get_leader.php";

    //HashMap<String, String> totalmap = new HashMap<String, String>();
    //HashMap<String, String> latestmap = new HashMap<String, String>();


    //------------------------------------------------------------------------------
    //JSON Node names
    //------------------------------------------------------------------------------
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "leader";
    private static final String TAG_NAME = "name";
    private static final String TAG_CAL = "calories";

    //------------------------------------------------------------------------------
    //JSONArray
    //------------------------------------------------------------------------------
    JSONArray leader = null;

    //------------------------------------------------------------------------------
    // Used for setting textview visible after getting it from database
    //------------------------------------------------------------------------------
    private TextView textView12;
    private TextView textView13;
    private TextView textView22;
    private TextView textView23;
    private TextView textView32;
    private TextView textView33;
    private TextView textView42;
    private TextView textView43;
    private TextView textView52;
    private TextView textView53;
    private TextView textView72;
    private TextView textView73;
    private TextView textView82;
    private TextView textView83;
    private TextView textView92;
    private TextView textView93;

    private String userName0 ;
    private String caloriesValue0;
    private String userName1 ;
    private String caloriesValue1;
    private String userName2 ;
    private String caloriesValue2;
    private String userName3 ;
    private String caloriesValue3;
    private String userName4 ;
    private String caloriesValue4;
    private String userName5 ;
    private String caloriesValue5;
    private String userName6 ;
    private String caloriesValue6;
    private String userName7 ;
    private String caloriesValue7;
    private String userName8 ;
    private String caloriesValue8;

    private TableRow tableRow2;
    private TableRow tableRow3;
    private TableRow tableRow4;
    private TableRow tableRow5;
    private TableRow tableRow6;
    private TableRow tableRow8;
    private TableRow tableRow9;
    private TableRow tableRow10;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_profile_new);

        textView12 = (TextView) findViewById(R.id.textView12);
        textView13 = (TextView) findViewById(R.id.textView13);
        textView22 = (TextView) findViewById(R.id.textView22);
        textView23 = (TextView) findViewById(R.id.textView23);
        textView32 = (TextView) findViewById(R.id.textView32);
        textView33 = (TextView) findViewById(R.id.textView33);
        textView42 = (TextView) findViewById(R.id.textView42);
        textView43 = (TextView) findViewById(R.id.textView43);
        textView52 = (TextView) findViewById(R.id.textView52);
        textView53 = (TextView) findViewById(R.id.textView53);
        textView72 = (TextView) findViewById(R.id.textView72);
        textView73 = (TextView) findViewById(R.id.textView73);
        textView82 = (TextView) findViewById(R.id.textView82);
        textView83 = (TextView) findViewById(R.id.textView83);
        textView92 = (TextView) findViewById(R.id.textView92);
        textView93 = (TextView) findViewById(R.id.textView93);

        tableRow2 = (TableRow) findViewById(R.id.tableRow2);
        tableRow3 = (TableRow) findViewById(R.id.tableRow3);
        tableRow4 = (TableRow) findViewById(R.id.tableRow4);
        tableRow5 = (TableRow) findViewById(R.id.tableRow5);
        tableRow6 = (TableRow) findViewById(R.id.tableRow6);
        tableRow8 = (TableRow) findViewById(R.id.tableRow8);
        tableRow9 = (TableRow) findViewById(R.id.tableRow9);
        tableRow10 = (TableRow) findViewById(R.id.tableRow10);


       //new LoadAllProducts().execute();
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
				LeaderView.this.finish();
            }
        });
        settingHomeNav = (ImageButton) findViewById(R.id.homeImageButton);
        settingHomeNav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(context, Home.class);
                startActivity(intent2);
				LeaderView.this.finish();
            }
        });
        settingLeaderNav = (ImageButton) findViewById(R.id.leaderImageButton);
        settingLeaderNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(context, LeaderView.class);
                startActivity(intent2);
				LeaderView.this.finish();
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
            pDialog = new ProgressDialog(LeaderView.this);
            pDialog.setMessage("Loading Leader Profile. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        //------------------------------------------------------------------------------
        // Getting All items from url
        //------------------------------------------------------------------------------
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
            //------------------------------------------------------------------------------
            // Getting JSON Object
            //------------------------------------------------------------------------------
            JSONObject json = jParser.makeHttpRequest(url_get_leader, "GET", params);
            Log.d("All Products: ", json.toString());

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    
                    //------------------------------------------------------------------------------
                    // User Data found
                    // Getting Array of Leaders
                    //------------------------------------------------------------------------------
                    leader = json.getJSONArray(TAG_PRODUCTS);
                    
                    //------------------------------------------------------------------------------
                    // Looping through All Leaders
                    //------------------------------------------------------------------------------
                    for (int i = 0; i < leader.length(); i++) {
                        JSONObject c0 = leader.getJSONObject(i);
                        switch (i){
                            case 0 :
                                userName0 = c0.getString(TAG_NAME);
                                caloriesValue0 = c0.getString(TAG_CAL);
                                break;
                            case 1 :

                                userName1 = c0.getString(TAG_NAME);
                                caloriesValue1 = c0.getString(TAG_CAL);
                                break;
                            case 2 :
                                userName2 = c0.getString(TAG_NAME);
                                caloriesValue2 = c0.getString(TAG_CAL);
                                break;
                            case 3 :
                                userName3 = c0.getString(TAG_NAME);
                                caloriesValue3 = c0.getString(TAG_CAL);
                                break;
                            case 4 :
                                userName4 = c0.getString(TAG_NAME);
                                caloriesValue4 = c0.getString(TAG_CAL);
                                break;
                            case 5 :
                                userName5 = c0.getString(TAG_NAME);
                                caloriesValue5 = c0.getString(TAG_CAL);
                                break;
                            case 6 :
                                userName6 = c0.getString(TAG_NAME);
                                caloriesValue7 = c0.getString(TAG_CAL);
                                break;
                            default :
                                caloriesValue8 = c0.getString(TAG_CAL);
                                userName8 = c0.getString(TAG_NAME);
                                break;
                        }

                    }


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
            // Updating UI from Background Thread
            //------------------------------------------------------------------------------
            runOnUiThread(new Runnable() {
                public void run() {
                    textView12.setText("" + userName0 + "");
                    textView13.setText("" + caloriesValue0 + "");
                    textView22.setText("" + userName1 + "");
                    textView23.setText("" + caloriesValue1 + "");
                    textView32.setText("" + userName2 + "");
                    textView33.setText("" + caloriesValue2 + "");
                    textView42.setText("" + userName3 + "");
                    textView43.setText("" + caloriesValue3 + "");
                    textView52.setText("" + userName4 + "");
                    textView53.setText("" + caloriesValue4 + "");
                    textView72.setText("" + userName5 + "");
                    textView73.setText("" + caloriesValue5 + "");
                    textView82.setText("" + userName6 + "");
                    textView83.setText("" + caloriesValue7 + "");
                    textView93.setText("" + caloriesValue8 + "");
                    textView92.setText("" + userName8 + "");

                    tableRow2.setVisibility(View.VISIBLE);
                    tableRow3.setVisibility(View.VISIBLE);
                    tableRow4.setVisibility(View.VISIBLE);
                    tableRow5.setVisibility(View.VISIBLE);
                    tableRow6.setVisibility(View.VISIBLE);
                    tableRow8.setVisibility(View.VISIBLE);
                    tableRow9.setVisibility(View.VISIBLE);
                    tableRow10.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}
