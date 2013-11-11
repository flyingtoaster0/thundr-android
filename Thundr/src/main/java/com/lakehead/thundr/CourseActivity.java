package com.lakehead.thundr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.LayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    LinearLayout seasonInfoView;
    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        Bundle b = getIntent().getExtras();
        courseDept = b.getString("department");
        courseCode = b.getString("course_code");
        //courseSection = b.getString("section_code");

        Log.d("Debug", "Opened course activity. Using: " + courseDept + "-" + courseCode);

        new GetJSONArrayTask(this).execute("http://thundr.ca/api/course_info/"+courseDept+"/"+courseCode);

        //sectionInfoView = (LinearLayout)findViewById(R.id.section_info);
        seasonInfoView = (LinearLayout)findViewById(R.id.season_info);
        //sectionInfoView.setSelector(android.R.color.transparent);

        courseInfoLayout = (LinearLayout) findViewById(R.id.course_info);
        courseInfoLayout.setVisibility(View.INVISIBLE);

        //sectionInfoLayout = (ListView) findViewById(R.id.section_info);
        //sectionInfoView.setVisibility(View.INVISIBLE);
        seasonInfoView.setVisibility(View.INVISIBLE);
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




    public boolean checkSeasonEmpty(JSONObject season)
    {
        boolean empty = true;
        try
        {
            for(int i=0; i<season.length(); i++)
            {
                if(season.getJSONArray((String)season.names().get(i)).length() > 0)
                {
                    empty = false;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return empty;
    }


    @Override
    public void onTaskCompleted(JSONArray jArray) {
        this.jArray = jArray;
        final ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        titleText = (TextView)findViewById(R.id.course_title);
        codeText = (TextView)findViewById(R.id.course_code);
        descriptText = (TextView)findViewById(R.id.course_description);
        prereqText = (TextView)findViewById(R.id.course_prerequisites);


        //ArrayList<JSONObject> seasonList = new ArrayList<JSONObject>();

        try
        {
            JSONObject fallSections = new JSONObject(((JSONObject)jArray.get(0)).getString("fall"));
            JSONObject winterSections = new JSONObject(((JSONObject)jArray.get(0)).getString("winter"));


            if(!checkSeasonEmpty(fallSections))
            {
                //FALL-----------------------------------------------
                View seasonView;
                LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                seasonView = inflater.inflate(R.layout.course_season_info, null);
                TextView seasonNameView = (TextView) seasonView.findViewById(R.id.season_name);
                seasonNameView.setText("Fall");

                seasonInfoView.addView(seasonView);

                LinearLayout seasonLayout = (LinearLayout)seasonView;


                // --------------Lectures--------------
                for(int i=0; i<fallSections.getJSONArray("lectures").length(); i++)
                {
                    View sectionInfoView;
                    sectionInfoView = inflater.inflate(R.layout.section_info, null);
                    seasonLayout.addView(sectionInfoView);


                    JSONObject lecture = (JSONObject)fallSections.getJSONArray("lectures").get(i);

                    for(int j=0; j<lecture.getJSONArray("class_array").length(); j++)
                    {
                        JSONObject klass = (JSONObject)lecture.getJSONArray("class_array").get(j);

                        View classRowView;
                        classRowView = inflater.inflate(R.layout.class_info, null);
                        TextView dayView = (TextView) classRowView.findViewById(R.id.day_of_week);
                        dayView.setText(klass.getString("day"));

                        LinearLayout classRowLayout = (LinearLayout)sectionInfoView;
                        classRowLayout.addView(classRowView);
                    }
                }



                // --------------Labs--------------
                for(int i=0; i<fallSections.getJSONArray("labs").length(); i++)
                {
                    View sectionInfoView;
                    sectionInfoView = inflater.inflate(R.layout.section_info, null);
                    seasonLayout.addView(sectionInfoView);


                    JSONObject lecture = (JSONObject)fallSections.getJSONArray("labs").get(i);

                    for(int j=0; j<lecture.getJSONArray("class_array").length(); j++)
                    {
                        JSONObject klass = (JSONObject)lecture.getJSONArray("class_array").get(j);

                        View classRowView;
                        classRowView = inflater.inflate(R.layout.class_info, null);
                        TextView dayView = (TextView) classRowView.findViewById(R.id.day_of_week);
                        dayView.setText(klass.getString("day"));

                        LinearLayout classRowLayout = (LinearLayout)sectionInfoView;
                        classRowLayout.addView(classRowView);
                    }
                }



                // --------------Practicals--------------
                for(int i=0; i<fallSections.getJSONArray("practicals").length(); i++)
                {
                    View sectionInfoView;
                    sectionInfoView = inflater.inflate(R.layout.section_info, null);
                    seasonLayout.addView(sectionInfoView);


                    JSONObject lecture = (JSONObject)fallSections.getJSONArray("practicals").get(i);

                    for(int j=0; j<lecture.getJSONArray("class_array").length(); j++)
                    {
                        JSONObject klass = (JSONObject)lecture.getJSONArray("class_array").get(j);

                        View classRowView;
                        classRowView = inflater.inflate(R.layout.class_info, null);
                        TextView dayView = (TextView) classRowView.findViewById(R.id.day_of_week);
                        dayView.setText(klass.getString("day"));

                        LinearLayout classRowLayout = (LinearLayout)sectionInfoView;
                        classRowLayout.addView(classRowView);
                    }
                }



                // --------------Tutorials--------------
                for(int i=0; i<fallSections.getJSONArray("tutorials").length(); i++)
                {
                    View sectionInfoView;
                    sectionInfoView = inflater.inflate(R.layout.section_info, null);
                    seasonLayout.addView(sectionInfoView);


                    JSONObject lecture = (JSONObject)fallSections.getJSONArray("tutorials").get(i);

                    for(int j=0; j<lecture.getJSONArray("class_array").length(); j++)
                    {
                        JSONObject klass = (JSONObject)lecture.getJSONArray("class_array").get(j);

                        View classRowView;
                        classRowView = inflater.inflate(R.layout.class_info, null);
                        TextView dayView = (TextView) classRowView.findViewById(R.id.day_of_week);
                        dayView.setText(klass.getString("day"));

                        LinearLayout classRowLayout = (LinearLayout)sectionInfoView;
                        classRowLayout.addView(classRowView);
                    }
                }

                //you've gotta DRY this up a bit, man...
                //etc

            }
        }
        catch(Exception e)
        {
            Log.e("Exceptions", e.toString());
        }

        courseInfoLayout.setVisibility(View.VISIBLE);
        seasonInfoView.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
    }

}