package com.lakehead.thundr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.text.ParseException;

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
    String tokenString;
    SharedPreferences prefs;


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
        //sectionInfoView.setSelector(R.color.transparent);

        courseInfoLayout = (LinearLayout) findViewById(R.id.course_info);
        courseInfoLayout.setVisibility(View.INVISIBLE);

        //sectionInfoLayout = (ListView) findViewById(R.id.section_info);
        //sectionInfoView.setVisibility(View.INVISIBLE);
        seasonInfoView.setVisibility(View.INVISIBLE);
        bar = (ProgressBar) findViewById(R.id.loader);

        prefs = this.getSharedPreferences("com.lakehead.thundr", Context.MODE_PRIVATE);
        tokenString = prefs.getString("remember_token","");

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


    public void addSection(String sectionType, LinearLayout parentLayout, JSONObject sectionsList)
    {
        try
        {
            LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for(int i=0; i<sectionsList.getJSONArray(sectionType).length(); i++)
            {
                JSONObject lecture = (JSONObject)sectionsList.getJSONArray(sectionType).get(i);
                View infoListView = inflater.inflate(R.layout.season_info_section_list, null);
                parentLayout.addView(infoListView);
                LinearLayout infoListLayout = (LinearLayout)infoListView;

                View sectionInfoView;
                sectionInfoView = inflater.inflate(R.layout.section_info, null);
                TextView methodView = (TextView) sectionInfoView.findViewById(R.id.section_method);
                methodView.setText(lecture.getString("method"));

                TextView fullCodeView = (TextView) sectionInfoView.findViewById(R.id.section_full_code);
                fullCodeView.setText("(" + lecture.getString("department") + "-" + lecture.getString("course_code") + "-" + lecture.getString("section_code") + ")");

                TextView synonymView = (TextView) sectionInfoView.findViewById(R.id.section_synonym);
                synonymView.setText(lecture.getString("synonym"));
                final int id = lecture.getInt("id");

                Button addButton = (Button) sectionInfoView.findViewById(R.id.add_to_schedule_button);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AddSectionTask(CourseActivity.this).execute("http://thundr.ca/api/schedules/add_section/" + id, tokenString);
                    }
                });

                infoListLayout.addView(sectionInfoView);
                RelativeLayout sectionInfoLayout = (RelativeLayout)sectionInfoView;
                //set its synonym etc

                if(lecture.getJSONArray("class_array").length()>0)
                {
                    View classListView = inflater.inflate(R.layout.class_list, null);
                    infoListLayout.addView(classListView);
                    for(int j=0; j<lecture.getJSONArray("class_array").length(); j++)
                    {
                        JSONObject klass = (JSONObject)lecture.getJSONArray("class_array").get(j);

                        View classRowView = inflater.inflate(R.layout.class_info, null);
                        TextView dayView = (TextView) classRowView.findViewById(R.id.day_of_week);
                        dayView.setText(klass.getString("day"));


                        TextView startTimeView = (TextView) classRowView.findViewById(R.id.start_time);
                        TextView endTimeView = (TextView) classRowView.findViewById(R.id.end_time);
                        TextView roomView = (TextView) classRowView.findViewById(R.id.room);

                        String startTimeString = "";
                        String endTimeString = "";
                        try
                        {
                            SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat outFormat = new SimpleDateFormat("hh:mma");
                            Date startTime = inFormat.parse(klass.getString("start_time"));
                            Date endTime = inFormat.parse(klass.getString("end_time"));
                            startTimeString = outFormat.format(startTime);
                            endTimeString = outFormat.format(endTime);
                        }
                        catch(ParseException e)
                        {
                            e.printStackTrace();
                        }

                        startTimeView.setText(startTimeString);
                        endTimeView.setText(endTimeString);

                        roomView.setText(klass.getString("room"));




                        LinearLayout classListLayout = (LinearLayout)classListView;
                        classListLayout.addView(classRowView);
                    }
                }




            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onTaskCompleted(Object obj) {
        if(obj.getClass() == JSONArray.class)
        {
            this.jArray = (JSONArray)obj;
            JSONObject jInfo = null;
            try
            {
                jInfo = jArray.getJSONObject(0);

                final ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                titleText = (TextView)findViewById(R.id.course_title);
                codeText = (TextView)findViewById(R.id.course_code);
                descriptText = (TextView)findViewById(R.id.course_description);
                prereqText = (TextView)findViewById(R.id.course_prerequisites);


                TextView courseTitleText = (TextView)findViewById(R.id.course_title);
                TextView courseCodeText = (TextView)findViewById(R.id.course_code);
                TextView courseDescriptText = (TextView)findViewById(R.id.course_description);
                TextView coursePrereqText = (TextView)findViewById(R.id.course_prerequisites);

                courseTitleText.setText(jInfo.getString("name"));
                courseCodeText.setText(jInfo.getString("department") + "-" + jInfo.getString("course_code"));
                courseDescriptText.setText(jInfo.getString("description"));
                coursePrereqText.setText("Prerequisites: " + jInfo.getString("prerequisite"));




                JSONObject fallSections = new JSONObject(jArray.getJSONObject(0).getString("fall"));
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


                    addSection("lectures", seasonLayout, fallSections);
                    addSection("labs", seasonLayout, fallSections);
                    addSection("practicals", seasonLayout, fallSections);
                    addSection("tutorials", seasonLayout, fallSections);
                }

                if(!checkSeasonEmpty(winterSections))
                {
                    //FALL-----------------------------------------------
                    View seasonView;
                    LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    seasonView = inflater.inflate(R.layout.course_season_info, null);
                    TextView seasonNameView = (TextView) seasonView.findViewById(R.id.season_name);
                    seasonNameView.setText("Winter");

                    seasonInfoView.addView(seasonView);
                    LinearLayout seasonLayout = (LinearLayout)seasonView;


                    addSection("lectures", seasonLayout, winterSections);
                    addSection("labs", seasonLayout, winterSections);
                    addSection("practicals", seasonLayout, winterSections);
                    addSection("tutorials", seasonLayout, winterSections);
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
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), (String)obj, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}