package com.lakehead.thundr;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by tim on 10/14/13.
 */

class LoginTask extends AsyncTask<String, Void, JSONObject> {

    private OnTaskCompleted listener;

    public LoginTask(OnTaskCompleted listener){
        this.listener=listener;
    }

    protected JSONObject doInBackground(String... params) {
        JSONObject jObj = null;
        String json = "";
        String result="";
        try
        {
            URI uri = new URI(params[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet method = new HttpGet(uri);
            method.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(params[1], params[2]), "UTF-8", false));
            HttpResponse response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                try
                {
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
                }
                catch (Exception e)
                {
                    Log.e("Exceptions", "Error converting result " + e.toString());
                }
                try
                {
                    jObj = new JSONObject(result);
                    return jObj;
                }
                catch (JSONException e)
                {
                    Log.e("Exceptions", e.toString());
                }
            }
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            Log.e("Exceptions",e.toString());
            e.printStackTrace();
            return null;
        }
        return null;
    }

    protected void onPostExecute(JSONObject jObj) {
        listener.onTaskCompleted(jObj);
    }
}