package com.flyingtoaster.thundr;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.util.ActivityController;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Test
    public void shouldHaveApplicationName() throws Exception {
        ActivityController controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity activity = (MainActivity)controller.get();
        assertThat(activity.getSlidingMenu(), notNullValue());
        activity.mOpenMenuButton.callOnClick();

        assertThat(activity.getSlidingMenu().isMenuShowing(), is(true));
    }

    @Test
    public void trivialTest() throws Exception {
        assertThat("TESTING", equalTo("TESTING"));
    }
}
