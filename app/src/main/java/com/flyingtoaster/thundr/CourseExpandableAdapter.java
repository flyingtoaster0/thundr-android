package com.flyingtoaster.thundr;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * The adapter used to populate the list view for the klasses fragment
 */
public class CourseExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> seasons; // header titles
    private HashMap<String, ArrayList<Object>> sections; //klasses nested within departments


    /**
     * @param context Used to get the inflater for the object.
     * @param seasons An array of department names
     * @param sections A HashMap where a department name maps to an array of klasses
     */
    public CourseExpandableAdapter(Context context, ArrayList<String> seasons,
                                       HashMap<String, ArrayList<Object>> sections) {
        this.context = context;
        this.seasons = seasons;
        this.sections = sections;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.sections.get(this.seasons.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     *
     * @param groupPosition The current header position
     * @param childPosition The current child element position
     * @param isLastChild Determines whether the current child is that last
     * @param convertView unused
     * @param parent The parent of the current View.
     * @return Returns the now-formulated row of data.
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String season = seasons.get(groupPosition);
        Object object = sections.get(season).get(childPosition);
        View rowView = null;


        if (object instanceof Section) {
            rowView = inflater.inflate(R.layout.course_row_section, parent, false);
            TextView sectionTitleView = (TextView)rowView.findViewById(R.id.section_title);

            final Section section = (Section)object;

            sectionTitleView.setText("Section: " + section.getSynonym() + " (" + section.getMethod() + ")");

            ArrayList<Klass> klassArray = new ArrayList<Klass>();
            for (int i=0; i<sections.get(season).size(); i++) {
                if ((sections.get(season).get(i) instanceof Klass) && (((Klass)sections.get(season).get(i))).getSecionID() == section.getID()) {
                    klassArray.add((Klass)sections.get(season).get(i));
                }
            }
            section.setKlasses(klassArray);

            final Button mSaveButton;
            final ImageView mOpenButton;
            final SlidingMenu mSlidingMenu;
            mSlidingMenu = (SlidingMenu)rowView.findViewById(R.id.slidingmenulayout);
            mSaveButton = (Button)rowView.findViewById(R.id.button_add);
            mOpenButton = (ImageView)mSlidingMenu.getContent().findViewById(R.id.open_frag_menu);
            mOpenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mSlidingMenu.isMenuShowing()) {
                        mSlidingMenu.showMenu();
                    } else {
                        mSlidingMenu.showContent();
                    }
                }
            });

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    //Log.d("TEST", gson.toJson(section));
                    Section.saveToPreferences(context, section);
                    mSlidingMenu.showContent();
                }
            });

        } else if (object instanceof Klass) {
            rowView = inflater.inflate(R.layout.section_time_row, parent, false);

            TextView dayView = (TextView)rowView.findViewById(R.id.day_of_week);
            TextView timeView = (TextView)rowView.findViewById(R.id.klass_time_textview);
            TextView roomView = (TextView)rowView.findViewById(R.id.room);

            Klass klass = (Klass)object;

            dayView.setText(klass.getDay());
            timeView.setText(klass.getStartEndTime());
            roomView.setText(klass.getRoom());

        }

        return rowView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.sections.get(this.seasons.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.seasons.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.seasons.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.course_row_season, null);

            TextView groupTitle = (TextView)convertView.findViewById(R.id.season_title);
            if (groupTitle == null || seasons.get(groupPosition) == null) return convertView;

            groupTitle.setText(seasons.get(groupPosition));
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
