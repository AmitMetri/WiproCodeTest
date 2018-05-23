package com.amit.wiprocodetest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;



/**
 * Created by dell on 5/23/2018.
 */

public class MainActivityOnRefreshTest {


    MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = Mockito.mock(MainActivity.class);
    }

    @Test
    public void testMethodCall() throws Exception {
        /*A simple testcase to check how many times a method call happens.
        * We are call onStart() method in which there are zero calls for onRefresh().
        * So this test will fail with below info.
        * Wanted but not invoked:mainActivity.onRefresh();*/
        mainActivity.onStart();
        Mockito.verify(mainActivity,Mockito.times(1)).onRefresh();
    }

    @After
    public void tearDown() throws Exception {
        mainActivity= null;
    }

}