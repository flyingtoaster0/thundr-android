package com.flyingtoaster.thundr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by tim on 10/6/14.
 */
public class MyCoursesFragment extends Fragment {
    View mRootView;
    ListView mMyCoursesListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my_courses, container, false);

        mMyCoursesListView = (ListView)mRootView.findViewById(R.id.my_courses_listview);

        return mRootView;
    }
}
