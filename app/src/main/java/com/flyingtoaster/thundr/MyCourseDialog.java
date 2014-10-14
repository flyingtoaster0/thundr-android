package com.flyingtoaster.thundr;

/**
 * Created by tim on 10/7/14.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    Button mOkButton;
    Button mRemoveButton;

    MyCoursesFragment mParent;

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
        mOkButton = (Button)mRootView.findViewById(R.id.button_ok);
        mRemoveButton = (Button)mRootView.findViewById(R.id.button_remove);


        Bundle args = getArguments();
        mFullSectionCode = args.getString("full_section_code","");

        if (mFullSectionCode != null && !mFullSectionCode.isEmpty()) {

            mSection = Section.getStoredSection(mFullSectionCode, getActivity());
            if (mSection != null) {
                mFullSectionTextView.setText(mSection.getFullSectionCode());
                mSectionNameTextView.setText(mSection.getCourseName());

                mKlasses = mSection.getKlasses();

                mTimeAdapter = new MyCourseTimeAdapter(getActivity(), mKlasses);
                mTimeListView.setAdapter(mTimeAdapter);
            }

            mOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            mRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Remove course?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                            mParent.removeCourse(mFullSectionCode);
                        }
                    });
                    builder.setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
            });
        }

        builder.setView(mRootView);

        return builder.create();
    }

    public void setParent(MyCoursesFragment parent) {
        this.mParent = parent;
    }
}

