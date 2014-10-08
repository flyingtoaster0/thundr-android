package com.flyingtoaster.thundr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tim on 2014-10-05.
 */
public class MenuListAdapter extends ArrayAdapter<Object> {
    private ArrayList<Object> objects;
    private final Activity context;

    public MenuListAdapter (Activity context, ArrayList<Object> objects) {
        super(context, R.layout.course_row, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int layoutRes=0;
        int titleRes=0;

        switch (position) {
            case 0:
                layoutRes=R.layout.menu_heading;
                titleRes = R.string.courses;
                break;
            case 1:
                layoutRes=R.layout.menu_item;
                titleRes = R.string.collection;
                break;
            /*
            case 2:
                layoutRes=R.layout.menu_item;
                titleRes = R.string.today;
                break;
            */
            case 2:
                layoutRes=R.layout.menu_item;
                titleRes = R.string.browse;
                break;
            case 3:
                layoutRes=R.layout.menu_heading;
                titleRes = R.string.app;
                break;
            case 4:
                layoutRes=R.layout.menu_item;
                titleRes = R.string.about;
                break;
        }

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(layoutRes, null);
            if (layoutRes == R.layout.menu_heading) {
                v.setEnabled(false);
                v.setOnClickListener(null);;
            }

            TextView textView = (TextView)v.findViewById(R.id.menu_item_text);
            if (textView != null) {
                textView.setText(getContext().getString(titleRes));
            }
        }

        return v;
    }
}
