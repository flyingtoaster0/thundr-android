package com.flyingtoaster.thundr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by tim on 2014-10-05.
 */
public class MainActivity extends SlidingFragmentActivity {
    static final int FRAGMENT_MY_COURSES = 1;
    static final int FRAGMENT_TODAY = 2;
    static final int FRAGMENT_BROWSE = 3;
    static final int FRAGMENT_ABOUT = 5;

    MenuFragment mMenuFragment;
    Fragment mActiveFragment;

    ImageView mOpenMenuButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("TEST");
        // set the content view
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.navigation_menu);
        // configure the SlidingMenu

        SlidingMenu menu = getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.behind_offset);
        menu.setFadeDegree(0.35f);
        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //menu.setMenu(R.layout.navigation_menu);

        mOpenMenuButton = (ImageView)findViewById(R.id.menu_open_button);
        mOpenMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSlidingMenu().showMenu();
            }
        });

        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mMenuFragment = new MenuFragment();
            t.replace(R.id.navigation_menu, mMenuFragment);
            t.commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        goToFragment(FRAGMENT_MY_COURSES);
    }

    public void goToFragment(int fragmentNum) {
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        boolean shouldReplace = true;

        switch (fragmentNum) {
            case FRAGMENT_MY_COURSES:
                if (mActiveFragment instanceof MyCoursesFragment) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new MyCoursesFragment();
                }
                break;
            case FRAGMENT_BROWSE:
                if (mActiveFragment instanceof BrowseFragment) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new BrowseFragment();
                }
                break;
            case FRAGMENT_TODAY:
                if (mActiveFragment instanceof TodayFragment) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new TodayFragment();
                }
                break;
            case FRAGMENT_ABOUT:
                if (mActiveFragment instanceof AboutFragment) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new AboutFragment();
                }
                break;
        }

        if (!shouldReplace || mActiveFragment == null) {
            getSlidingMenu().showContent();
            return;
        }

        t.replace(R.id.active_fragment, mActiveFragment);
        t.commit();
        getSupportFragmentManager().executePendingTransactions();
        getSlidingMenu().showContent(true);
    }
}
