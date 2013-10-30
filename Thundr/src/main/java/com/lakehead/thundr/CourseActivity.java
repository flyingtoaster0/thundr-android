package com.lakehead.thundr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.LayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tim on 10/15/13.
 */
public class CourseActivity extends Activity implements OnTaskCompleted
{
    String courseDept;
    String courseCode;
    String courseSection;
    JSONArray jArray;
    JSONObject courseObj;

    TextView titleText;
    TextView codeText;
    TextView descriptText;
    TextView prereqText;

    LinearLayout courseInfoLayout;
    LinearLayout sectionInfoView;
    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        Bundle b = getIntent().getExtras();
        courseDept = b.getString("department");
        courseCode = b.getString("code");
        courseSection = b.getString("section");

        Log.d("Debug", "Opened course activity. Using: " + courseDept + "-" + courseCode + "-" + courseSection);

        new GetJSONArrayTask(this).execute("http://thundr.ca/api/courses/"+courseDept+"/"+courseCode+"/"+courseSection);

        sectionInfoView = (LinearLayout)findViewById(R.id.section_info);
        //sectionInfoView.setSelector(android.R.color.transparent);

        courseInfoLayout = (LinearLayout) findViewById(R.id.course_info);
        courseInfoLayout.setVisibility(View.INVISIBLE);

        //sectionInfoLayout = (ListView) findViewById(R.id.section_info);
        sectionInfoView.setVisibility(View.INVISIBLE);
        bar = (ProgressBar) findViewById(R.id.loader);
    }


    private ArrayList<ArrayList<String>> createSection(JSONObject jObject)
    {
        ArrayList<ArrayList<String>> outerList = new ArrayList<ArrayList<String>>();
        int i=0;
        try
        {
            if(jObject.getBoolean("mon") == true)
            {
                ArrayList<String> innerList = new ArrayList<String>();
                innerList.add("Mon");
                innerList.add(jObject.getString("startTime"));
                innerList.add(jObject.getString("endTime"));
                innerList.add(jObject.getString("room"));

                outerList.add(i++, innerList);
            }
            if(jObject.getBoolean("tue") == true)
            {
                ArrayList<String> innerList = new ArrayList<String>();
                innerList.add("Tues");
                innerList.add(jObject.getString("startTime"));
                innerList.add(jObject.getString("endTime"));
                innerList.add(jObject.getString("room"));

                outerList.add(i++, innerList);
            }
            if(jObject.getBoolean("wed") == true)
            {
                ArrayList<String> innerList = new ArrayList<String>();
                innerList.add("Wed");
                innerList.add(jObject.getString("startTime"));
                innerList.add(jObject.getString("endTime"));
                innerList.add(jObject.getString("room"));

                outerList.add(i++, innerList);
            }
            if(jObject.getBoolean("thu") == true)
            {
                ArrayList<String> innerList = new ArrayList<String>();
                innerList.add("Thurs");
                innerList.add(jObject.getString("startTime"));
                innerList.add(jObject.getString("endTime"));
                innerList.add(jObject.getString("room"));

                outerList.add(i++, innerList);
            }
            if(jObject.getBoolean("fri") == true)
            {
                ArrayList<String> innerList = new ArrayList<String>();
                innerList.add("Fri");
                innerList.add(jObject.getString("startTime"));
                innerList.add(jObject.getString("endTime"));
                innerList.add(jObject.getString("room"));

                outerList.add(i++, innerList);
            }

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return outerList;
    }


    @Override
    public void onTaskCompleted(JSONArray jArray) {
        this.jArray = jArray;
        final ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        titleText = (TextView)findViewById(R.id.course_title);
        codeText = (TextView)findViewById(R.id.course_code);
        descriptText = (TextView)findViewById(R.id.course_description);
        prereqText = (TextView)findViewById(R.id.course_prerequisites);

        try
        {
            for (int i=0; i < jArray.length(); ++i)
            {
                ArrayList<ArrayList<String>> tempList = createSection(jArray.getJSONObject(i));

                for(int j=0; j<tempList.size(); j++)
                {
                    list.add(tempList.get(j));
                }
            }

            final SectionAdapter adapter = new SectionAdapter(this, list);
            //sectionInfoView.setAdapter(adapter);



            // add it to Layout
            for(int i=0; i<list.size(); i++)
            {
                View view;
                LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.course_section_info, null);
                TextView dayView = (TextView) view.findViewById(R.id.day_of_week);
                String startTimeString = "";
                String endTimeString = "";
                try
                {
                    SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat outFormat = new SimpleDateFormat("hh:mma");
                    Date startTime = inFormat.parse(list.get(i).get(1));
                    Date endTime = inFormat.parse(list.get(i).get(2));
                    startTimeString = outFormat.format(startTime);
                    endTimeString = outFormat.format(endTime);
                }
                catch(ParseException e)
                {
                    e.printStackTrace();
                }
                TextView startTimeView = (TextView) view.findViewById(R.id.start_time);
                TextView endTimeView = (TextView) view.findViewById(R.id.end_time);
                TextView roomView = (TextView) view.findViewById(R.id.room);
                dayView.setText(list.get(i).get(0));
                startTimeView.setText(startTimeString);
                endTimeView.setText(endTimeString);
                roomView.setText(list.get(i).get(3));

                sectionInfoView.addView(view);
            }


            courseObj= jArray.getJSONObject(0);

            titleText.setText(courseObj.getString("name"));

            codeText.setText(courseDept + "-" + courseCode + "-" + courseSection);
            descriptText.setText(courseObj.getString("description"));

            String prereqString = "";
            if(courseObj.getString("prerequisite").length()>0)
                prereqString = "Prerequisites: " + courseObj.getString("prerequisite");
            prereqText.setText(prereqString);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        //construct array of "occurrences of a class time" and pass that to an adapter

        //progressBar.dismiss();
        courseInfoLayout.setVisibility(View.VISIBLE);
        sectionInfoView.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
    }

}