package com.flyingtoaster.thundr;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.util.ActivityController;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Test
    public void shouldHaveApplicationName() throws Exception {
        String appName = new MainActivity().getResources().getString(R.string.app_name);
        ActivityController controller = Robolectric.buildActivity(MainActivity.class).create().start();

        MainActivity activity = (MainActivity)controller.get();
        assertThat(activity.getSlidingMenu(), notNullValue());

        activity.mOpenMenuButton.callOnClick();

        assertThat(activity.getSlidingMenu().isMenuShowing(), is(true));
        //activity.mOpenMenuButton.callOnClick();

        //Thread.sleep(1000);

        //assertThat(activity.getSlidingMenu().isMenuShowing(), equalTo(true));
    }
}
