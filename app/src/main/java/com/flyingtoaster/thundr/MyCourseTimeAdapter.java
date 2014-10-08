package com.flyingtoaster.thundr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCourseTimeAdapter extends ArrayAdapter<Klass> {

    private ArrayList<Klass> sectionCodes;
    private final Activity context;

    public MyCourseTimeAdapter(Activity context, ArrayList<Klass> sectionCodes) {
        super(context, R.layout.section_time_row, sectionCodes);
        this.context = context;
        this.sectionCodes = sectionCodes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.section_time_row, null);

            Klass currentKlass = sectionCodes.get(position);

            if(currentKlass != null) {

                TextView dayTextView = (TextView)v.findViewById(R.id.day_of_week);
                TextView timeTextView = (TextView)v.findViewById(R.id.klass_time_textview);
                TextView roomTextView = (TextView)v.findViewById(R.id.room);

                dayTextView.setText(currentKlass.getDay());
                timeTextView.setText(currentKlass.getStartEndTime());
                roomTextView.setText(currentKlass.getRoom());
            }
        }

        return v;
    }
}
