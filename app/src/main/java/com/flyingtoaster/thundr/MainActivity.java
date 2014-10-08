package com.flyingtoaster.thundr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.Stack;

/**
 * Created by tim on 2014-10-05.
 */
public class MainActivity extends FragmentActivity implements FragmentCallbackListener {
    static final int FRAGMENT_MY_COURSES = 1;
    static final int FRAGMENT_TODAY = 2;
    static final int FRAGMENT_BROWSE = 2;
    static final int FRAGMENT_ABOUT = 4;

    int mCurrentSection;

    MenuFragment mMenuFragment;
    Fragment mActiveFragment;

    ImageView mOpenMenuButton;

    Stack<Fragment> browseStack;
    SlidingMenu mMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("TEST");
        // set the content view
        setContentView(R.layout.activity_main);
        //setBehindContentView(R.layout.navigation_menu);
        // configure the SlidingMenu

        //SlidingMenu mMenu; // = getSlidingMenu();
        mMenu = (SlidingMenu)findViewById(R.id.main_sliding_menu);
        mMenu.setMode(SlidingMenu.LEFT);
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mMenu.setShadowWidthRes(R.dimen.shadow_width);
        mMenu.setShadowDrawable(R.drawable.shadow);
        mMenu.setBehindOffsetRes(R.dimen.behind_offset);
        mMenu.setFadeDegree(0.35f);
        mMenu.setMenu(R.layout.navigation_menu);
        //mMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //mMenu.setMenu(R.layout.navigation_menu);






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

    public SlidingMenu getSlidingMenu () {
        return mMenu;
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
                    emptyBrowseStack();
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
            /*
            case FRAGMENT_TODAY:
                if (mCurrentSection == FRAGMENT_TODAY) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new TodayFragment();
                    mCurrentSection = FRAGMENT_TODAY;
                }
                break;
            */
            case FRAGMENT_ABOUT:
                if (mCurrentSection == FRAGMENT_ABOUT) {
                    shouldReplace = false;
                } else {
                    mActiveFragment = new AboutFragment();
                    mCurrentSection = FRAGMENT_ABOUT;
                    emptyBrowseStack();
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
        } else if (mActiveFragment instanceof MyCoursesFragment && ((MyCoursesFragment)mActiveFragment).isModalShowing()) {
            ((MyCoursesFragment)mActiveFragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void emptyBrowseStack() {
        while (browseStack.size() > 0) {
            browseStack.pop();
        }
    }
}
