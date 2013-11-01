package com.lakehead.thundr;

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

public class CourseAdapter extends ArrayAdapter<JSONObject> {

    private ArrayList<JSONObject> objects;
    private final Activity context;

    public CourseAdapter (Activity context, ArrayList<JSONObject> objects) {
        super(context, R.layout.course_row, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.course_row, null);
        }

        JSONObject i = objects.get(position);

        if(i != null)
        {
            TextView nameView = (TextView) v.findViewById(R.id.course_name);
            TextView codeView = (TextView) v.findViewById(R.id.course_code);
            try
            {
                if(nameView != null)
                {
                    nameView.setText(i.getString("name"));
                }
                if(codeView != null)
                {
                    codeView.setText(i.getString("department") + "-" + i.getString("code") + "-" + i.getString("section"));
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

        return v;
    }
}
