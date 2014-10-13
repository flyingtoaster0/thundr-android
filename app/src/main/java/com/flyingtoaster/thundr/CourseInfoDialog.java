package com.flyingtoaster.thundr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tim on 2014-10-12.
 */
public class CourseInfoDialog extends DialogFragment {
    View mRootView;
    TextView mDescriptionTextView;
    TextView mPrereqTextView;

    String mDescription;
    String mPrerequisite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mRootView = inflater.inflate(R.layout.course_info_dialog, null);

        mDescriptionTextView = (TextView)mRootView.findViewById(R.id.course_description);
        mPrereqTextView = (TextView)mRootView.findViewById(R.id.course_prerequisites);

        Bundle args = getArguments();
        mDescription = args.getString("course_description","");
        mPrerequisite = args.getString("course_prereqs","");

        mDescriptionTextView.setText(mDescription);
        mPrereqTextView.setText(mPrerequisite);

        builder.setView(mRootView);

        return builder.create();
    }
}
