package com.flyingtoaster.thundr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.app.SearchManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class SearchResultsActivity extends Activity  implements GetJSONArrayListener {
    String query;
    JSONArray jArray;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction()))
        {
            query = getIntent().getStringExtra(SearchManager.QUERY);
            try
            {
                query = URLEncoder.encode(query, "utf-8");
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }


            Log.d("Debug","Entered search activity with query: " + query);

            new GetJSONArrayTask(this, "http://thundr.ca/api/search/" + query).execute();

            listview = (ListView) findViewById(R.id.course_list);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    public void onJSONArrayPreExecute(){};
    public void onJSONArrayProgressUpdate(String... params){};
    public void onJSONArrayPostExecute(JSONArray jArray) {
        this.jArray = jArray;

        final ArrayList<JSONObject> list = new ArrayList<JSONObject>();

        try
        {
            for (int i = 0; i < jArray.length(); ++i) {
                list.add(jArray.getJSONObject(i));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        final CourseAdapter adapter = new CourseAdapter(this, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final JSONObject item = (JSONObject)parent.getItemAtPosition(position);
                /*
                try
                {
                    Intent intent = new Intent(SearchResultsActivity.this, CourseActivity.class);

                    Bundle b = new Bundle();
                    b.putString("department", item.getString("department"));
                    b.putString("course_code", item.getString("course_code"));
                    intent.putExtras(b);
                    startActivity(intent);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                */
            }
        });
        //progressBar.dismiss();
    };
    public void onJSONArrayCancelled(){};
}
