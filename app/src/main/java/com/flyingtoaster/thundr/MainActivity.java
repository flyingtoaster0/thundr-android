package com.flyingtoaster.thundr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.Stack;

/**
 * Created by tim on 2014-10-05.
 */
public class MainActivity extends SlidingFragmentActivity implements FragmentCallbackListener {
    static final int FRAGMENT_MY_COURSES = 1;
    static final int FRAGMENT_TODAY = 2;
    static final int FRAGMENT_BROWSE = 3;
    static final int FRAGMENT_ABOUT = 5;

    int mCurrentSection;

    MenuFragment mMenuFragment;
    Fragment mActiveFragment;

    ImageView mOpenMenuButton;

    Stack<Fragment> browseStack;

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

        browseStack = new Stack<Fragment>();


        goToFragment(FRAGMENT_MY_COURSES);
    }

    public void goToFragment(int fragmentNum) {
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        boolean shouldReplace = true;

        switch (fragmentNum) {
            case FRAGMENT_MY_COURSES:
                if (mCurrentSection == FRAGMENT_MY_COURSES) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new MyCoursesFragment();
                    mCurrentSection = FRAGMENT_MY_COURSES;
                }
                break;
            case FRAGMENT_BROWSE:
                if (mCurrentSection == FRAGMENT_BROWSE) {
                    shouldReplace = false;
                } else {
                    BrowseFragment browseFragment = new BrowseFragment();
                    browseFragment.setFragmentCallbackListener(this);
                    mActiveFragment = browseFragment;
                    mCurrentSection = FRAGMENT_BROWSE;
                }
                break;
            case FRAGMENT_TODAY:
                if (mCurrentSection == FRAGMENT_TODAY) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new TodayFragment();
                    mCurrentSection = FRAGMENT_TODAY;
                }
                break;
            case FRAGMENT_ABOUT:
                if (mCurrentSection == FRAGMENT_ABOUT) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new AboutFragment();
                    mCurrentSection = FRAGMENT_ABOUT;
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

    public void onDeptSelected(Bundle bundle) {
        browseStack.add(mActiveFragment);
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        DepartmentListFragment deptListFragment = new DepartmentListFragment();
        deptListFragment.setArguments(bundle);
        deptListFragment.setFragmentCallbackListener(this);
        mActiveFragment = deptListFragment;
        t.replace(R.id.active_fragment, deptListFragment);
        t.commit();
        getSupportFragmentManager().executePendingTransactions();
        getSlidingMenu().showContent(true);
    }

    public void onCourseSelected(Bundle bundle) {
        browseStack.add(mActiveFragment);
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(bundle);
        mActiveFragment = courseFragment;
        t.replace(R.id.active_fragment, courseFragment);
        t.commit();
        getSupportFragmentManager().executePendingTransactions();
        getSlidingMenu().showContent(true);
    }

    @Override
    public void onBackPressed() {
        if (!browseStack.empty()) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mActiveFragment = browseStack.pop();
            t.replace(R.id.active_fragment, mActiveFragment);
            t.commit();
            getSupportFragmentManager().executePendingTransactions();
        } else {
            super.onBackPressed();
        }
    }
}
