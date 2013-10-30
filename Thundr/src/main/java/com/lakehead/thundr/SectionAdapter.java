package com.lakehead.thundr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;

public class SectionAdapter extends ArrayAdapter<ArrayList<String>> {

    private ArrayList<ArrayList<String>> objects;
    private final Activity context;

    public SectionAdapter (Activity context, ArrayList<ArrayList<String>> objects) {
        super(context, R.layout.course_section_info, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.course_section_info, null);
        }

        ArrayList<String> objs = objects.get(position);

        if(objs != null)
        {
            TextView dayView = (TextView) v.findViewById(R.id.day_of_week);
            String startTimeString = "";
            String endTimeString = "";
            try
            {
                SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat outFormat = new SimpleDateFormat("hh:mma");
                Date startTime = inFormat.parse(objs.get(1));
                Date endTime = inFormat.parse(objs.get(2));
                startTimeString = outFormat.format(startTime);
                endTimeString = outFormat.format(endTime);
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }


            TextView startTimeView = (TextView) v.findViewById(R.id.start_time);
            TextView endTimeView = (TextView) v.findViewById(R.id.end_time);
            TextView roomView = (TextView) v.findViewById(R.id.room);

            dayView.setText(objs.get(0));
            startTimeView.setText(startTimeString);
            endTimeView.setText(endTimeString);
            roomView.setText(objs.get(3));

        }

        return v;
    }
}