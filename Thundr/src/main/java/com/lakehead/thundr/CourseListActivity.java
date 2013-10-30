package com.lakehead.thundr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CourseListActivity extends Activity implements OnTaskCompleted {
    String deptCode;
    JSONArray jArray;
    ListView listview;
    RelativeLayout layout;
    RelativeLayout loadingLayout;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Bundle b = getIntent().getExtras();
        deptCode = b.getString("deptCode");

        Log.d("Debug", "Opened course list activity. Using department code: "+ deptCode);

        new GetJSONArrayTask(this).execute("http://thundr.ca/api/courses/department/"+deptCode);

        listview = (ListView) findViewById(R.id.course_list);

        bar = (ProgressBar) findViewById(R.id.loader);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onTaskCompleted(JSONArray jArray) {
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
        Log.d("Debug","Loading complete");
        //((RelativeLayout)bar.getParent()).removeView(bar);

        final CourseAdapter adapter = new CourseAdapter(this, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final JSONObject item = (JSONObject)parent.getItemAtPosition(position);
                try
                {
                    Intent intent = new Intent(CourseListActivity.this, CourseActivity.class);

                    Bundle b = new Bundle();
                    b.putString("department", item.getString("department"));
                    b.putString("code", item.getString("code"));
                    b.putString("section", item.getString("section"));
                    intent.putExtras(b);
                    startActivity(intent);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        });
        bar.setVisibility(View.GONE);
    }
}