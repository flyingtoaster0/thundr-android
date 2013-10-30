package com.lakehead.thundr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import android.net.Uri;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;


import android.os.AsyncTask;

/**
 * Created by tim on 10/14/13.
 */

class GetJSONArrayTask extends AsyncTask<String, Void, JSONArray> {

    private OnTaskCompleted listener;

    public GetJSONArrayTask(OnTaskCompleted listener){
        this.listener=listener;
    }

    JSONArray jArray;

    protected JSONArray doInBackground(String... params) {
        JSONObject jObj = null;
        String json = "";
        String result="";

        try
        {
            URI uri = new URI(params[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost method = new HttpPost(uri);
            Log.d("Debug","Getting URL: " + params[0]);
            HttpResponse response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                //Log.d("Debug",EntityUtils.toString(entity));

                try {
                    InputStream is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result=sb.toString();
                    json = sb.toString();
                } catch (Exception e) {
                    Log.e("Exceptions", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    jArray = new JSONArray(result);
                    //jObj = new JSONObject(json);

                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        //Get an output to the screen
                        //returnString += "\n\t" + jArray.getJSONObject(i);

                    }
                    return jArray;

                } catch (JSONException e) {
                    Log.d("Debug","Error parsing JSON");
                    Log.e("Exceptions", e.toString());
                }

                // return JSON String
                //return jObj;
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            Log.d("Debug","Getting URL failed!");
            Log.e("Exceptions",e.toString());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    protected void onPostExecute(JSONArray jArray) {
        listener.onTaskCompleted(jArray);
    }
}