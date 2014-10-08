package com.flyingtoaster.thundr;

import org.robolectric.RobolectricTestRunner;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by tim on 10/8/14.
 */

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Test
    public void shouldHaveApplicationName() throws Exception {
        String appName = new MainActivity().getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("Thundr2"));
    }
}
