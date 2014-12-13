/////////////////////////////////////////////////////////////////////////////////
//                                                                             //
//  Signsignupview.java - RunForLife                                           //
//              Source file containing signin/signupclass                      //
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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.ImageButton;

//------------------------------------------------------------------------------
// Class implementation: signsignupview
// Login and Signup functionality
//------------------------------------------------------------------------------
public class signsignupview extends Activity {
    private ImageButton settingHomeNav;
    
    //------------------------------------------------------------------------------
    // SharedPreferences to save user name and password
    //------------------------------------------------------------------------------
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String namekey = "nameKey";
    public static final String passkey = "passwordKey";
    public SharedPreferences sharedpreferences;
   
    //------------------------------------------------------------------------------
    // Progress Dialog Bar
    //------------------------------------------------------------------------------
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    
    //------------------------------------------------------------------------------
    // Sign up fields
    //------------------------------------------------------------------------------
    EditText username2;
    EditText password2;
    EditText emailfill;
    EditText addressfill;
    
    //------------------------------------------------------------------------------
    // URL to connect to PHP files for Sign up and Login
    //------------------------------------------------------------------------------
    private static String url_sign_up = "http://149.119.216.129:8888/sign_up.php";
    private static String url_sign = "http://149.119.216.129:8888/sign_in.php";
   
    //------------------------------------------------------------------------------
    // Login fields
    //------------------------------------------------------------------------------
    EditText emailsignin;
    EditText password;
    
    //------------------------------------------------------------------------------
    // JSON node name
    //------------------------------------------------------------------------------
    private static final String TAG_SUCCESS = "success";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signsignup);

        username2 = (EditText) findViewById(R.id.username2);
        password2 = (EditText) findViewById(R.id.password2);
        emailfill = (EditText) findViewById(R.id.emailfill);
        addressfill = (EditText) findViewById(R.id.addressfill);

        emailsignin = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        addListenerOnButton();
    }
    //------------------------------------------------------------------------------
    // Data persistence
    //------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        sharedpreferences=getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(namekey))
        {
            if(sharedpreferences.contains(passkey)){
                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
            }
        }
        super.onResume();
    }
    
    //------------------------------------------------------------------------------
    // Home button
    //------------------------------------------------------------------------------
    public void addListenerOnButton() {
        final Context context = this;
        settingHomeNav = (ImageButton) findViewById(R.id.homeImageButton);
        settingHomeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(context, Home.class);
                startActivity(intent2);
            }
        });
        
        //------------------------------------------------------------------------------
        // Sign up button
        //------------------------------------------------------------------------------
        Button btn_signup = (Button) findViewById(R.id.signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------------------------------------------------------------------------------
                // Create a new function to run in background thread while Signing up
                //------------------------------------------------------------------------------
                new CreateNewProduct().execute();
            }
        });
        
        //------------------------------------------------------------------------------
        // Home button
        //------------------------------------------------------------------------------
        Button btn_signin = (Button) findViewById(R.id.login);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------------------------------------------------------------------------------
                // Create a new function to run in background thread while Logging in
                //------------------------------------------------------------------------------
                new createSignIn().execute();
            }
        });
    }
    
    //------------------------------------------------------------------------------
    // Background Async Task for user Log in
    //------------------------------------------------------------------------------
    class createSignIn extends AsyncTask<String, String, String > {
       
        //------------------------------------------------------------------------------
        // Before starting background thread Show Progress Dialog
        //------------------------------------------------------------------------------
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(signsignupview.this);
            pDialog.setMessage("Signing In ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {
            Editor editor = sharedpreferences.edit();
            String email = emailsignin.getText().toString();
            String passwd = password.getText().toString();
            
            //------------------------------------------------------------------------------
            // Building Parameters
            //------------------------------------------------------------------------------
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", passwd));
            
            //------------------------------------------------------------------------------
            // Getting JSON Object
            //------------------------------------------------------------------------------
            JSONObject json = jsonParser.makeHttpRequest(url_sign,
                    "POST", params);
            Log.d("Create Response", json.toString());
            
            //------------------------------------------------------------------------------
            // Check for success tag
            //------------------------------------------------------------------------------
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    
                    //------------------------------------------------------------------------------
                    // Login successful
                    //------------------------------------------------------------------------------
                    editor.putString(namekey, email);
                    editor.putString(passkey, passwd);
                    editor.commit();
                    
                    //------------------------------------------------------------------------------
                    // Launching Home activity
                    //------------------------------------------------------------------------------
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    finish();
                } else {
                    
                    //------------------------------------------------------------------------------
                    // Display Toast message - "Username/Password Incorrect !"
                    //------------------------------------------------------------------------------
                    Handler handler =  new Handler(getApplicationContext().getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(), "Username/Password Incorrect !",Toast.LENGTH_LONG).show();
                        }
                    });
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
        }
    }
    
    //------------------------------------------------------------------------------
    // Background Async Task for user Sign up
    //------------------------------------------------------------------------------
    class CreateNewProduct extends AsyncTask<String, String, String > {
        
        //------------------------------------------------------------------------------
        // Before starting background thread Show Progress Dialog
        //------------------------------------------------------------------------------
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(signsignupview.this);
            pDialog.setMessage("Signing Up..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {
            String name = username2.getText().toString();
            String password = password2.getText().toString();
            String email = emailfill.getText().toString();
            String city = addressfill.getText().toString();
            
            //------------------------------------------------------------------------------
            // Building Parameters
            //------------------------------------------------------------------------------
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("city", city));
            
            //------------------------------------------------------------------------------
            // Getting JSON Object
            //------------------------------------------------------------------------------
            JSONObject json = jsonParser.makeHttpRequest(url_sign_up,
                    "POST", params);
            Log.d("Create Response", json.toString());
            
            //------------------------------------------------------------------------------
            // Check for success tag
            //------------------------------------------------------------------------------
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    
                    //------------------------------------------------------------------------------
                    // Display Toast message as success and prompt the user to log in
                    //------------------------------------------------------------------------------
                    Handler handler =  new Handler(getApplicationContext().getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(), "User Created Successfully. Please Login now",Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    
                    //------------------------------------------------------------------------------
                    // Display Toast message as failure
                    //------------------------------------------------------------------------------
                    Handler handler =  new Handler(getApplicationContext().getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(getApplicationContext(), "Failed to create User.Please check entries",Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.d("sign up ","failed");

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
            // Funtion to clear the user sign up form
            //------------------------------------------------------------------------------
            ViewGroup group = (ViewGroup) findViewById(R.id.signsignupview);
            clearForm(group);
        }
    }
    
    //------------------------------------------------------------------------------
    // Clear the user sign up form
    //------------------------------------------------------------------------------
    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }
            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }
}