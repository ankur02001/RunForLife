/////////////////////////////////////////////////////////////////////////////////
//                                                                             //
//  SettingView.java - RunForLife                                              //
//              Source file containing settings class                          //
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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;


//------------------------------------------------------------------------------
// Class implementation
//------------------------------------------------------------------------------
public class settingView extends Activity {
    private final String TAG = "RunForLife:HomePage:";

    //------------------------------------------------------------------------------
    // Create Toggle Button
    //------------------------------------------------------------------------------
    private ToggleButton togButton1,togButton2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_new);
        addListenerOnButton();
    }


    public void addListenerOnButton() {

        final Context context = this;
        togButton1 = (ToggleButton) findViewById(R.id.toggleButton2);
        togButton2 = (ToggleButton) findViewById(R.id.toggleButton3);


        //------------------------------------------------------------------------------
        // Home Button
        //------------------------------------------------------------------------------
        ImageButton homeButton;
        homeButton = (ImageButton) findViewById(R.id.homeImageButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View arg0) {
            StringBuffer message_output = new StringBuffer();

            //------------------------------------------------------------------------------
            // Toast message text as per the text of toggle button
            //------------------------------------------------------------------------------
            message_output.append("toggleButton1 : ").append(togButton1.getText());
            message_output.append("\ntoggleButton2 : ").append(togButton2.getText());

            String distancematch = (String) togButton1.getText();
           
            //------------------------------------------------------------------------------
            // Change the Distance metric in Home screen to/fro Miles/Kilometers
            //------------------------------------------------------------------------------
            if(distancematch.equalsIgnoreCase("Miles")){
            Home.isDistancein = false ;
            }
            else{
            Home.isDistancein = true ;
            }

            String ElevationinMet = (String) togButton1.getText();
            
            //------------------------------------------------------------------------------
            // Change the Elevation metric in Home screen to/fro Meters/Feet
            //------------------------------------------------------------------------------
            if(ElevationinMet.equalsIgnoreCase("Meters")){
                Home.isElevationinMeter = true ;
            }else{
                Home.isElevationinMeter = false ;
            }

            Toast.makeText(settingView.this, message_output.toString(), Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(context, Home.class);
                startActivity(intent2);
				settingView.this.finish();

            }
        });
    }
}
