package com.flyingtoaster.thundr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class TodayFragment extends Fragment {
    View mRootView;
    Button mOpenButton;
    SlidingMenu mSlidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_today, container, false);

        mSlidingMenu = (SlidingMenu)mRootView.findViewById(R.id.slidingmenulayout);
        mOpenButton = (Button)mSlidingMenu.getContent().findViewById(R.id.open_frag_menu);
        mOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSlidingMenu.isMenuShowing()) {
                    mSlidingMenu.showMenu();
                } else {
                    mSlidingMenu.showContent();
                }
            }
        });

        return mRootView;
    }
}
