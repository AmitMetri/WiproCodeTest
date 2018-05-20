package com.amit.wiprocodetest;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by dell on 5/20/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


   @Rule
   public ActivityTestRule<MainActivity> mActivityRule =
           new ActivityTestRule<>(MainActivity.class);

   MainActivity mainActivity = null;

   @Before
   public void setUp() throws Exception {
      mainActivity = mActivityRule.getActivity();
   }

   @Test
   public  void testLaunch(){

      //Test if Activity is launched or not
      View view = mainActivity.findViewById(R.id.refresh);
      assertNotNull(view);

   }

   @After
   public void tearDown() throws Exception {
      mainActivity= null;
   }

}