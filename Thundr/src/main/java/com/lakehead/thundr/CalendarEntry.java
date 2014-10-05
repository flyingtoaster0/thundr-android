package com.lakehead.thundr;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.util.Date;


/**
 * Created by tim on 12/11/13.
 */
public class CalendarEntry
{
    private String dept;
    private String code;
    private String room;
    private String startTime;
    private String endTime;
    private Activity activity;


    public CalendarEntry(String dept, String code, String room, String startTime, String endTime, Activity activity)
    {
        this.dept = dept;
        this.code = code;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activity = activity;
    }


    public View generateView()
    {
        View eventView;
        int start=0, end=0;

        Date startDate;
        Date endDate;

        SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat hoursOut = new SimpleDateFormat("HH");
        SimpleDateFormat minutesOut = new SimpleDateFormat("mm");
        try
        {
            startDate = inFormat.parse(startTime);
            endDate = inFormat.parse(endTime);

            start = Integer.parseInt(minutesOut.format(startDate));
            start += Integer.parseInt(hoursOut.format(startDate))*60;
            end = Integer.parseInt(minutesOut.format(endDate));
            end += Integer.parseInt(hoursOut.format(endDate))*60;
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        int location = end - start;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        int dp = (int) (location * logicalDensity + 0.5);

        //480 is the pixel offset that comes from not showing midnight to 8AM
        int startdp = (int) ((start - 480) * logicalDensity + 0.5);

        LayoutInflater inflater = (LayoutInflater)   activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        eventView = inflater.inflate(R.layout.calendar_class, null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp);
        layoutParams.setMargins(0,startdp,0,0);

        eventView.setLayoutParams(layoutParams);

        TextView deptTextView = (TextView) eventView.findViewById(R.id.class_dept);
        deptTextView.setText(dept);

        TextView codeTextView = (TextView) eventView.findViewById(R.id.class_code);
        codeTextView.setText(code);

        TextView roomTextView = (TextView) eventView.findViewById(R.id.class_room);
        roomTextView.setText(room);

        return eventView;
    }
}
