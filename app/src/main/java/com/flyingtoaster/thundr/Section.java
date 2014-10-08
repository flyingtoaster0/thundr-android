package com.flyingtoaster.thundr;

import java.util.ArrayList;

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
}
