package com.flyingtoaster.thundr;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by tim on 10/7/14.
 */
public class Section {
    private int mID;
    private int mCourseID;
    private int mSynonym;
    private String mName;
    private String mDepartment;
    private String mCourseCode;
    private String mSectionCode;
    private String mInstructor;
    private String mStartDate;
    private String mEndDate;
    private String mSeason;
    private String mMethod;
    private ArrayList<Klass> mKlasses;

    public int getID() {
        return mID;
    }

    public void setID(int id) {
        this.mID = id;
    }

    public int getCourseID() {
        return mCourseID;
    }

    public void setCourseID(int courseID) {
        this.mCourseID = courseID;
    }

    public void setSynonym(int synonym) {
        this.mSynonym = synonym;
    }

    public int getSynonym() {
        return mSynonym;
    }

    public String getCourseName() {
        return mName;
    }

    public void setCourseName(String courseName) {
        this.mName = courseName;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        this.mDepartment = department;
    }

    public String getCourseCode() {
        return mCourseCode;
    }

    public void setCourseCode(String courseCode) {
        this.mCourseCode = courseCode;
    }

    public String getSectionCode() {
        return mSectionCode;
    }

    public void setSectionCode(String dectionCode) {
        this.mSectionCode = dectionCode;
    }

    public String getInstructor() {
        return mInstructor;
    }

    public void setInstructor(String instructor) {
        this.mInstructor = instructor;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        this.mEndDate = endDate;
    }

    public String getSeason() {
        return mSeason;
    }

    public void setSeason(String season) {
        this.mSeason = season;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        this.mMethod = method;
    }

    public ArrayList<Klass> getKlasses() {
        return mKlasses;
    }

    public void setKlasses(ArrayList<Klass> klasses) {
        this.mKlasses = klasses;
    }

    public String getFullCourseCode() {
        return mDepartment + "-" + mCourseCode;
    }

    public String getFullSectionCode() {
        return mDepartment + "-" + mCourseCode + "-" + mSectionCode;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public void saveToPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("sections", Context.MODE_PRIVATE);
        prefs.edit().putString(this.getFullSectionCode(), this.toJSON()).apply();
    }

    public static void saveToPreferences(Context context, Section section) {
        SharedPreferences prefs = context.getSharedPreferences("sections", Context.MODE_PRIVATE);
        prefs.edit().putString(section.getFullSectionCode(), section.toJSON()).apply();
    }

    public static ArrayList<String> getStoredSectionCodes(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("sections", Context.MODE_PRIVATE);
        Map<String, ?> savedSections = prefs.getAll();
        ArrayList<String> sectionCodes = new ArrayList<String>();

        for (String key : savedSections.keySet()) {
            sectionCodes.add(key);
        }

        return sectionCodes;
    }

    public static Section getStoredSection(String fullSectionCode, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("sections", Context.MODE_PRIVATE);
        String jsonString = prefs.getString(fullSectionCode, "");
        if (jsonString == null) return null;

        Gson gson = new Gson();
        Section section = gson.fromJson(jsonString, Section.class);

        if (section == null) return null;

        return section;
    }

    public static ArrayList<Section> getAllStoredSections(Context context) {
        ArrayList<Section> allSections = new ArrayList<Section>();

        for (String key : getStoredSectionCodes(context)) {
            Section section = getStoredSection(key, context);
            if (section != null) {
                allSections.add(section);
            }
        }

        return allSections;
    }
}
