package com.flyingtoaster.thundr;

/**
 * Created by tim on 10/7/14.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCourseDialog extends DialogFragment {
    View mRootView;
    TextView mFullSectionTextView;
    TextView mSectionNameTextView;
    ListView mTimeListView;

    String mFullSectionCode;
    Section mSection;
    ArrayList<Klass> mKlasses;
    MyCourseTimeAdapter mTimeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mRootView = inflater.inflate(R.layout.my_course_modal, null);

        mFullSectionTextView = (TextView)mRootView.findViewById(R.id.section_full_code);
        mSectionNameTextView = (TextView)mRootView.findViewById(R.id.section_name);
        mTimeListView = (ListView)mRootView.findViewById(R.id.section_time_listview);

        Bundle args = getArguments();
        mFullSectionCode = args.getString("full_section_code","");

        mSection = Section.getStoredSection(mFullSectionCode, getActivity());

        if (mSection != null) {
            mFullSectionTextView.setText(mSection.getFullSectionCode());
            mSectionNameTextView.setText(mSection.getCourseName());

            mKlasses = mSection.getKlasses();

            mTimeAdapter = new MyCourseTimeAdapter(getActivity(), mKlasses);
            mTimeListView.setAdapter(mTimeAdapter);
        }

        builder.setView(mRootView);

        return builder.create();
    }
}

