/////////////////////////////////////////////////////////////////////////////////
//                                                                             //
//  JSONParser.java - RunForLife                                               //
//              Source file containing JSONParser class                        //
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



//------------------------------------------------------------------------------
// Class implementation
//------------------------------------------------------------------------------
public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	
    //------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------
    public JSONParser() {

	}

    //------------------------------------------------------------------------------
    // Function get json from url
    // by making HTTP POST or GET mehtod
    //------------------------------------------------------------------------------
    public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {
        
        //------------------------------------------------------------------------------
        // Making HTTP request
        //------------------------------------------------------------------------------
        try {
			
            //------------------------------------------------------------------------------
            // Check for request method
            //------------------------------------------------------------------------------
            if(method == "POST"){
                
                //------------------------------------------------------------------------------
                // Creating DefaultHttpClient object
                //------------------------------------------------------------------------------
                DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				
			}else if(method == "GET"){
				
                //------------------------------------------------------------------------------
                // Check for request method
                //------------------------------------------------------------------------------
                DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}			
			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

        //------------------------------------------------------------------------------
        // Parse the string to a JSON object
        //------------------------------------------------------------------------------
        try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

        //------------------------------------------------------------------------------
        // Return JSON String
        //------------------------------------------------------------------------------
        return jObj;

	}
}
