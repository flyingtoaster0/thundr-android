package com.flyingtoaster.thundr;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.ProgressDialog;
import android.widget.SearchView;
import android.app.SearchManager;
import android.content.Context;

import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends Activity implements GetJSONArrayListener {
    ListView listview;
    ProgressDialog progressBar;
    JSONArray jArray;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetJSONArrayTask(this, "http://thundr.ca/api/departments").execute();

        listview = (ListView) findViewById(R.id.list);


        bar = (ProgressBar) findViewById(R.id.loader);

        //progressBar = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        //progressBar.setCancelable(false);
        //progressBar.setMessage("Loading departments...");
        //progressBar.show();

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
        final ArrayList<String> list = new ArrayList<String>();

        try
        {
            for (int i = 0; i < jArray.length(); ++i) {
                list.add(jArray.getJSONObject(i).getString("dept_name"));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                try
                {
                    final String deptCode = (String)MainActivity.this.jArray.getJSONObject(position).get("dept_code");
                    Intent intent = new Intent(MainActivity.this, CourseListActivity.class);

                    Bundle b = new Bundle();
                    b.putString("dept_code", deptCode);
                    intent.putExtras(b);
                    startActivity(intent);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        bar.setVisibility(View.GONE);
        //progressBar.dismiss();
    };
    public void onJSONArrayCancelled(){};
}
