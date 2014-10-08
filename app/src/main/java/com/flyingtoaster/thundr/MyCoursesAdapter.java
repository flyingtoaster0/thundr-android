package com.flyingtoaster.thundr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCoursesAdapter extends ArrayAdapter<String> {

    private ArrayList<String> sectionCodes;
    private final Activity context;

    public MyCoursesAdapter(Activity context, ArrayList<String> sectionCodes) {
        super(context, R.layout.course_row, sectionCodes);
        this.context = context;
        this.sectionCodes = sectionCodes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.my_courses_row, null);

            String currentSectionCode = sectionCodes.get(position);

            if(currentSectionCode != null) {
                TextView sectionTextView = (TextView)v.findViewById(R.id.my_courses_row_textview);
                sectionTextView.setText(currentSectionCode);
            }
        }

        return v;
    }
}
