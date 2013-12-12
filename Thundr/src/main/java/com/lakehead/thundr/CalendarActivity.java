package com.lakehead.thundr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarActivity extends Activity implements OnTaskCompleted
{
    RelativeLayout monday;
    RelativeLayout tuesday;
    RelativeLayout wednesday;
    RelativeLayout thursday;
    RelativeLayout friday;
    ScrollView scrollView;

    JSONArray classArray;
    SharedPreferences prefs;
    String token;
    LinearLayout currentTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_fragment);

        prefs = this.getSharedPreferences("com.lakehead.thundr", Context.MODE_PRIVATE);
        token = prefs.getString("remember_token","");
        new GetJSONArrayTask(this).execute("http://192.168.35.100:3000/api/schedules/classes", "cb5da6f4b4b014e61c612284e28ed192b5812c63");


        monday = (RelativeLayout)findViewById(R.id.mondayRelativeLayout);
        tuesday = (RelativeLayout)findViewById(R.id.tuesdayRelativeLayout);
        wednesday = (RelativeLayout)findViewById(R.id.wednesdayRelativeLayout);
        thursday = (RelativeLayout)findViewById(R.id.thursdayRelativeLayout);
        friday = (RelativeLayout)findViewById(R.id.fridayRelativeLayout);
        currentTimeView = (LinearLayout)findViewById(R.id.currentTimeMarker);
        scrollView = (ScrollView)findViewById(R.id.calendarScrollView);
        scrollView.setVisibility(View.GONE);
        currentTimeView.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_class_calendar:
                goToDeptList();
                break;
            case R.id.action_list_classes:
                goToClassList();
                break;

            default:
                break;
        }

        return true;
    }

    private void goToDeptList()
    {
        Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void goToClassList()
    {
        Intent intent = new Intent(CalendarActivity.this, ScheduleActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskCompleted(Object obj)
    {
        this.classArray = (JSONArray)obj;


        for(int i=0; i<classArray.length(); i++)
        {
            JSONObject currentClass;
            String day = "";
            try
            {
                currentClass = classArray.getJSONObject(i);
                day = currentClass.getString("day");


                if(day.equals("M"))
                {
                    CalendarEntry c = new CalendarEntry(currentClass.getString("department"),
                            currentClass.getString("course_code"),
                            currentClass.getString("room"),
                            currentClass.getString("start_time"),
                            currentClass.getString("end_time"),this);
                    monday.addView(c.generateView());
                }
                else if(day.equals("T"))
                {
                    CalendarEntry c = new CalendarEntry(currentClass.getString("department"),
                            currentClass.getString("course_code"),
                            currentClass.getString("room"),
                            currentClass.getString("start_time"),
                            currentClass.getString("end_time"),this);
                    tuesday.addView(c.generateView());
                }
                else if(day.equals("W"))
                {
                    CalendarEntry c = new CalendarEntry(currentClass.getString("department"),
                            currentClass.getString("course_code"),
                            currentClass.getString("room"),
                            currentClass.getString("start_time"),
                            currentClass.getString("end_time"),this);
                    wednesday.addView(c.generateView());
                }
                else if(day.equals("Th"))
                {
                    CalendarEntry c = new CalendarEntry(currentClass.getString("department"),
                            currentClass.getString("course_code"),
                            currentClass.getString("room"),
                            currentClass.getString("start_time"),
                            currentClass.getString("end_time"),this);
                    thursday.addView(c.generateView());
                }
                else if(day.equals("F"))
                {
                    CalendarEntry c = new CalendarEntry(currentClass.getString("department"),
                            currentClass.getString("course_code"),
                            currentClass.getString("room"),
                            currentClass.getString("start_time"),
                            currentClass.getString("end_time"),this);
                    friday.addView(c.generateView());
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }


            //Everything below deals with positioning the current time marker
            Date now = new Date();
            SimpleDateFormat hoursOut = new SimpleDateFormat("HH");
            SimpleDateFormat minutesOut = new SimpleDateFormat("mm");

            int hours = Integer.parseInt(hoursOut.format(now));

            if(hours>8 && hours<10)
            {
                int currentTime = Integer.parseInt(minutesOut.format(now)) + Integer.parseInt(hoursOut.format(now))*60 - 480;
                DisplayMetrics metrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                float logicalDensity = metrics.density;
                currentTime = (int) (currentTime * logicalDensity + 0.5);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2);
                layoutParams.setMargins(0,currentTime,0,0);
                currentTimeView.setLayoutParams(layoutParams);
                currentTimeView.setVisibility(View.VISIBLE);
            }

            scrollView.setVisibility(View.VISIBLE);

        }
    }
}
