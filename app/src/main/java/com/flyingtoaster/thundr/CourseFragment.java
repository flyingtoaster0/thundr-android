package com.flyingtoaster.thundr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by tim on 10/15/13.
 */
public class CourseFragment extends Fragment implements GetJSONArrayListener
{
    String courseDept;
    String courseCode;
    String courseSection;
    JSONObject courseObj;

    TextView titleText;
    TextView codeText;
    TextView descriptText;
    TextView prereqText;

    LinearLayout courseInfoLayout;
    LinearLayout sectionInfoView;
    LinearLayout seasonInfoView;

    private View mRootView;
    ListView listview;
    JSONArray jArray;
    ProgressBar bar;
    FragmentCallbackListener mListener;
    private String mDept;

    private ImageView showInfoButton;


    private ExpandableListView mExpListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_course, container, false);



        Bundle b = getArguments();
        courseDept = b.getString("dept_code");
        courseCode = b.getString("course_code");

        Log.d("Debug", "Opened course activity. Using: " + courseDept + "-" + courseCode);


        //sectionInfoView = (LinearLayout)findViewById(R.id.section_info);
        //seasonInfoView = (LinearLayout)mRootView.findViewById(R.id.season_info);
        //sectionInfoView.setSelector(R.color.transparent);

        courseInfoLayout = (LinearLayout)mRootView.findViewById(R.id.course_info);
        courseInfoLayout.setVisibility(View.INVISIBLE);

        mExpListView = (ExpandableListView)mRootView.findViewById(R.id.expand_fall_list);
        showInfoButton = (ImageView)mRootView.findViewById(R.id.show_info_button);


        //sectionInfoLayout = (ListView) findViewById(R.id.section_info);
        //sectionInfoView.setVisibility(View.INVISIBLE);
        //seasonInfoView.setVisibility(View.INVISIBLE);
        bar = (ProgressBar)mRootView.findViewById(R.id.loader);


        new GetJSONArrayTask(this, "http://thundr.ca/api/course_info/"+courseDept+"/"+courseCode).execute();

        return mRootView;
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
            LayoutInflater inflater = (LayoutInflater)   this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public void onJSONArrayPreExecute(){};
    public void onJSONArrayProgressUpdate(String... params){};
    public void onJSONArrayPostExecute(JSONArray jArray) {
        this.jArray = jArray;
        JSONObject jInfo = null;
        try
        {
            jInfo = jArray.getJSONObject(0);

            final ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
            titleText = (TextView)mRootView.findViewById(R.id.course_title);
            codeText = (TextView)mRootView.findViewById(R.id.course_code);
            descriptText = (TextView)mRootView.findViewById(R.id.course_description);
            prereqText = (TextView)mRootView.findViewById(R.id.course_prerequisites);


            TextView courseTitleText = (TextView)mRootView.findViewById(R.id.course_title);
            TextView courseCodeText = (TextView)mRootView.findViewById(R.id.course_code);

            final String description = jInfo.getString("description");
            final String prerequisite = jInfo.getString("prerequisite");


            TextView courseDescriptText = (TextView)mRootView.findViewById(R.id.course_description);
            TextView coursePrereqText = (TextView)mRootView.findViewById(R.id.course_prerequisites);

            courseTitleText.setText(jInfo.getString("name"));
            courseCodeText.setText(jInfo.getString("department") + "-" + jInfo.getString("course_code"));
            //courseDescriptText.setText(jInfo.getString("description"));
            //coursePrereqText.setText("Prerequisites: " + jInfo.getString("prerequisite"));

            if (description == null && prerequisite == null) {
                showInfoButton.setVisibility(View.GONE);
            } else {
                showInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CourseInfoDialog dialog = new CourseInfoDialog();

                        Bundle args = new Bundle();
                        args.putString("course_description", description);
                        args.putString("course_prereqs", prerequisite);
                        dialog.setArguments(args);
                        dialog.show(getActivity().getSupportFragmentManager(), "CourseInfoDialog");
                    }
                });
            }

            JSONObject fallSections = new JSONObject(jArray.getJSONObject(0).getString("fall"));
            JSONObject winterSections = new JSONObject(((JSONObject)jArray.get(0)).getString("winter"));


            JSONArray fallLectures = fallSections.getJSONArray("lectures");
            JSONArray winterLectures = winterSections.getJSONArray("lectures");
            HashMap<Integer, ArrayList<Klass>> sectionKlassHash = new HashMap<Integer, ArrayList<Klass>>();


            HashMap<String, ArrayList<Section>> seasonSectionHash = new HashMap<String, ArrayList<Section>>();
            HashMap<String, ArrayList<Object>> seasonObjectHash = new HashMap<String, ArrayList<Object>>();


            ArrayList<Object> fallObjectArray = new ArrayList<Object>();
            ArrayList<Object> winterObjectArray = new ArrayList<Object>();



            ArrayList<Integer> sectionIDs = new ArrayList<Integer>();
            ArrayList<Section> sectionArray = new ArrayList<Section>();
            ArrayList<String> seasonArray = new ArrayList<String>();

            for (int i=0; i<fallLectures.length(); i++) {
                JSONObject currentSectionJSON = fallLectures.getJSONObject(i);
                Integer currentSectionID = currentSectionJSON.getInt("id");
                sectionIDs.add(currentSectionID);

                Section currentSection = new Section();
                currentSection.setID(currentSectionJSON.getInt("id"));
                currentSection.setCourseID(currentSectionJSON.getInt("course_id"));
                currentSection.setSynonym(currentSectionJSON.getInt("synonym"));
                currentSection.setCourseName(currentSectionJSON.getString("name"));
                currentSection.setDepartment(currentSectionJSON.getString("department"));
                currentSection.setCourseCode(currentSectionJSON.getString("course_code"));
                currentSection.setSectionCode(currentSectionJSON.getString("section_code"));
                currentSection.setInstructor(currentSectionJSON.getString("instructor"));
                currentSection.setStartDate(currentSectionJSON.getString("start_date"));
                currentSection.setEndDate(currentSectionJSON.getString("end_date"));
                currentSection.setSeason(currentSectionJSON.getString("season"));
                currentSection.setMethod(currentSectionJSON.getString("method"));

                fallObjectArray.add(currentSection);

                JSONArray klassJSONArray = fallLectures.getJSONObject(i).getJSONArray("class_array");
                ArrayList<Klass> klassArray = new ArrayList<Klass>();
                for (int j=0; j<klassJSONArray.length(); j++) {
                    JSONObject jsonKlass = klassJSONArray.getJSONObject(j);
                    Klass klass = new Klass();

                    klass.setID(jsonKlass.getInt("id"));
                    klass.setSecionID(jsonKlass.getInt("section_id"));
                    klass.setDay(jsonKlass.getString("day"));
                    klass.setStartTime(jsonKlass.getString("start_time"));
                    klass.setEndTime(jsonKlass.getString("end_time"));
                    klass.setRoom(jsonKlass.getString("room"));

                    klassArray.add(klass);
                    fallObjectArray.add(klass);
                }
                currentSection.setKlasses(klassArray);


                sectionKlassHash.put(currentSectionID, klassArray);
            }

            if (!winterObjectArray.isEmpty()) {
                seasonObjectHash.put("Fall", winterObjectArray);
                seasonArray.add("Fall");
            }

            for (int i=0; i<winterLectures.length(); i++) {
                JSONObject currentSectionJSON = winterLectures.getJSONObject(i);
                Integer currentSectionID = currentSectionJSON.getInt("id");
                sectionIDs.add(currentSectionID);

                Section currentSection = new Section();
                currentSection.setID(currentSectionJSON.getInt("id"));
                currentSection.setCourseID(currentSectionJSON.getInt("course_id"));
                currentSection.setSynonym(currentSectionJSON.getInt("synonym"));
                currentSection.setCourseName(currentSectionJSON.getString("name"));
                currentSection.setDepartment(currentSectionJSON.getString("department"));
                currentSection.setCourseCode(currentSectionJSON.getString("course_code"));
                currentSection.setSectionCode(currentSectionJSON.getString("section_code"));
                currentSection.setInstructor(currentSectionJSON.getString("instructor"));
                currentSection.setStartDate(currentSectionJSON.getString("start_date"));
                currentSection.setEndDate(currentSectionJSON.getString("end_date"));
                currentSection.setSeason(currentSectionJSON.getString("season"));
                currentSection.setMethod(currentSectionJSON.getString("method"));

                winterObjectArray.add(currentSection);

                JSONArray klassJSONArray = winterLectures.getJSONObject(i).getJSONArray("class_array");
                ArrayList<Klass> klassArray = new ArrayList<Klass>();
                for (int j=0; j<klassJSONArray.length(); j++) {
                    JSONObject jsonKlass = klassJSONArray.getJSONObject(j);
                    Klass klass = new Klass();

                    klass.setID(jsonKlass.getInt("id"));
                    klass.setSecionID(jsonKlass.getInt("section_id"));
                    klass.setDay(jsonKlass.getString("day"));
                    klass.setStartTime(jsonKlass.getString("start_time"));
                    klass.setEndTime(jsonKlass.getString("end_time"));
                    klass.setRoom(jsonKlass.getString("room"));

                    klassArray.add(klass);
                    winterObjectArray.add(klass);
                }
                currentSection.setKlasses(klassArray);


                sectionKlassHash.put(currentSectionID, klassArray);
            }


            //seasonSectionHash.put("Fall", sectionArray);
            //seasonSectionHash.put("Winter", sectionArray);
            if (!winterObjectArray.isEmpty()) {
                seasonObjectHash.put("Winter", winterObjectArray);
                seasonArray.add("Winter");
            }




            CourseExpandableAdapter courseAdapter = new CourseExpandableAdapter(this.getActivity(), seasonArray, seasonObjectHash);

            mExpListView.setAdapter(courseAdapter);

            if (!seasonArray.isEmpty()) {
                for (int i = 0; i < seasonArray.size(); i++) {
                    mExpListView.expandGroup(i);
                }
            }



            /*
            if(!checkSeasonEmpty(fallSections))
            {
                //FALL-----------------------------------------------
                View seasonView;
                LayoutInflater inflater = (LayoutInflater)   this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                LayoutInflater inflater = (LayoutInflater)   this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            */


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        courseInfoLayout.setVisibility(View.VISIBLE);
        //seasonInfoView.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
    };
    public void onJSONArrayCancelled(){};

}