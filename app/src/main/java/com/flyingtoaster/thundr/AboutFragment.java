package com.flyingtoaster.thundr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
    private final String URL_SLIDINGMENU = "https://github.com/jfeinstein10/SlidingMenu";

    View mRootView;

    TextView mTimTextview;
    TextView mFeedbackTextView;
    TextView mSlidingMenuTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_about, container, false);

        mTimTextview = (TextView)mRootView.findViewById(R.id.about_name_text_view);
        mFeedbackTextView = (TextView)mRootView.findViewById(R.id.about_email_address);
        mSlidingMenuTextView = (TextView)mRootView.findViewById(R.id.about_slidingmenu_text_view);


        mTimTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimClicked();
            }
        });

        mFeedbackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        mSlidingMenuTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAboutSlidingMenu();
            }
        });

        return mRootView;
    }

    private void sendFeedback() {
        String[] email = {getString(R.string.contact_email)};
        String appName = getString(R.string.app_name);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, appName + "Feedback");

        startActivity(Intent.createChooser(intent, "Send Feedback"));
    }

    private void onClickAboutSlidingMenu() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(URL_SLIDINGMENU));
        startActivity(i);
    }

    private void onTimClicked() {

    }
}
