package com.lakehead.thundr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScheduleActivity extends Activity implements OnTaskCompleted {
    ListView listview;
    ProgressDialog progressBar;
    JSONArray jArray;
    ProgressBar bar;
    SharedPreferences prefs;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listview = (ListView) findViewById(R.id.list);


        bar = (ProgressBar) findViewById(R.id.loader);

        prefs = this.getSharedPreferences("com.lakehead.thundr", Context.MODE_PRIVATE);
        token = prefs.getString("remember_token","");
        new GetScheduleTask(this).execute("http://thundr.ca/api/schedules/show", "f4f79c9b11f98e2c3d0e18819b42d88e701ec59c");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedule, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_class:
                Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

        return true;
    }


    @Override
    public void onTaskCompleted(Object obj) {
        this.jArray = (JSONArray)obj;
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

        final ScheduleAdapter adapter = new ScheduleAdapter(this, list);
        listview.setAdapter(adapter);

        /*final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                try
                {
                    final String deptCode = (String)ScheduleActivity.this.jArray.getJSONObject(position).get("deptCode");
                    Intent intent = new Intent(ScheduleActivity.this, CourseListActivity.class);

                    Bundle b = new Bundle();
                    b.putString("deptCode", deptCode);
                    intent.putExtras(b);
                    startActivity(intent);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });*/
        bar.setVisibility(View.GONE);
        //progressBar.dismiss();
    }
}
