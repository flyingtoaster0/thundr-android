package com.lakehead.thundr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
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

public class ScheduleActivity extends Activity implements OnTaskCompleted, ActionMode.Callback {
    ListView listview;
    ProgressDialog progressBar;
    JSONArray jArray;
    ProgressBar bar;
    SharedPreferences prefs;
    String token;
    ScheduleAdapter adapter;
    ActionMode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listview = (ListView) findViewById(R.id.list);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        bar = (ProgressBar) findViewById(R.id.loader);

        prefs = this.getSharedPreferences("com.lakehead.thundr", Context.MODE_PRIVATE);
        token = prefs.getString("remember_token","");
        new GetScheduleTask(this).execute("http://thundr.ca/api/schedules/show", "f4f79c9b11f98e2c3d0e18819b42d88e701ec59c");

    }

    private void goToDeptList()
    {
        Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void goToCalendar()
    {
        finish();
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
                goToDeptList();
                break;

            case R.id.action_to_calendar:
                goToCalendar();
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

        adapter = new ScheduleAdapter(this, list);
        listview.setAdapter(adapter);

        /*final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                goToCourseActivity(position);
                listview.setItemChecked(position, false);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

                listview.setItemChecked(position, true);
                listview.setOnItemClickListener(null);
                Log.d("Debug", "pos: " + listview.getCheckedItemPosition());
                mode = startActionMode(ScheduleActivity.this);
                return true;
            }
        });
        bar.setVisibility(View.GONE);
        //progressBar.dismiss();
    }

    private void deleteSection()
    {
        JSONObject section;
        try
        {
            section = (JSONObject)jArray.get(listview.getCheckedItemPosition());
            int id = section.getInt("id");
            new DeleteSectionTask(this).execute("http://thundr.ca/api/schedules/delete_section/"+id, token);
            adapter.remove(section);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void deselectAll()
    {
        for(int i=0; i<listview.getCount(); i++)
        {
            listview.setItemChecked(i, false);
        }
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        return true;
    }

    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
    }

    // Called when the user selects a contextual menu item
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_section:
                deleteSection();
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        deselectAll();
        mode = null;
    }

    void goToCourseActivity(int index)
    {
        try
        {
            Intent intent = new Intent(ScheduleActivity.this, CourseActivity.class);

            Bundle b = new Bundle();

            JSONObject item = jArray.getJSONObject(index);
            b.putString("department", item.getString("department"));
            b.putString("course_code", item.getString("course_code"));
            intent.putExtras(b);
            startActivity(intent);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            Log.e("Exceptions", e.toString());
        }
    }
}
