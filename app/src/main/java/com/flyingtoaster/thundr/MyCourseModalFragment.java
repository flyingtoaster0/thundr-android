package com.flyingtoaster.thundr;

/**
 * Created by tim on 10/7/14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class MyCourseModalFragment extends Fragment {
    View mRootView;
    TextView mFullSectionTextView;
    TextView mSectionNameTextView;
    ListView mTimeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.my_course_modal, container, false);

        mFullSectionTextView = (TextView)mRootView.findViewById(R.id.section_full_code);
        mSectionNameTextView = (TextView)mRootView.findViewById(R.id.section_name);
        mTimeListView = (ListView)mRootView.findViewById(R.id.section_time_listview);

        return mRootView;
    }
}

