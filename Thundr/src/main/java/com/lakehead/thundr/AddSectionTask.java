package com.lakehead.thundr;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by tim on 10/14/13.
 */

class AddSectionTask extends AsyncTask<String, Void, JSONArray> {

    private OnTaskCompleted listener;

    public AddSectionTask(OnTaskCompleted listener){
        this.listener=listener;
    }

    protected JSONArray doInBackground(String... params) {
        JSONArray jArray = null;
        String json = "";
        String result="";
        try
        {
            URI uri = new URI(params[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet method = new HttpGet(uri);

            method.addHeader("Authorization", "Token " + params[1]);

            HttpResponse response = httpclient.execute(method);
            if(response.getStatusLine().getStatusCode() == 200)
            {
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
                        jArray = new JSONArray(result);
                        return jArray;
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
            else
            {
                listener.onTaskCompleted("Course already exists in your schedule.");
                return null;
            }
        }
        catch(Exception e)
        {
            Log.e("Exceptions",e.toString());

        }
        return null;
    }

    protected void onPostExecute(JSONArray jArray)
    {
        listener.onTaskCompleted("Course added successfully");
    }
}