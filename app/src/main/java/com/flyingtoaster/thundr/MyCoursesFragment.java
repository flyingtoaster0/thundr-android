package com.flyingtoaster.thundr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by tim on 10/6/14.
 */
public class MyCoursesFragment extends Fragment {
    View mRootView;
    ListView mMyCoursesListView;
    ArrayList<String> mSectionCodes;
    MyCoursesAdapter mMyCoursesAdapter;
    MyCourseModalFragment mModal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my_courses, container, false);

        mMyCoursesListView = (ListView)mRootView.findViewById(R.id.my_courses_listview);

        mSectionCodes = Section.getStoredSectionCodes(getActivity());
        mMyCoursesAdapter = new MyCoursesAdapter(getActivity(), mSectionCodes);
        mMyCoursesListView.setAdapter(mMyCoursesAdapter);
        mMyCoursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mModal = new MyCourseModalFragment();

                String fullSectionCode = (String)parent.getItemAtPosition(position);
                Bundle args = new Bundle();
                args.putString("full_section_code", fullSectionCode);
                mModal.setArguments(args);

                FragmentTransaction t = MyCoursesFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                t.replace(R.id.my_course_modal, mModal);
                t.commit();
                MyCoursesFragment.this.getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });

        return mRootView;
    }

    public void onBackPressed() {
        if (mModal == null) {
            this.getActivity().onBackPressed();
        } else {
            FragmentTransaction t = MyCoursesFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
            t.remove(mModal);
            t.commit();
            MyCoursesFragment.this.getActivity().getSupportFragmentManager().executePendingTransactions();
            mModal = null;
        }
    }

    public boolean isModalShowing() {
        return mModal != null;
    }
}
