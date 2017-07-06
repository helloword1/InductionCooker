package com.goockr.inductioncooker.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.fragment.SmsLoginFragment;

/**
 * Created by CMQ on 2017/7/5.
 */

public class FragmentHelper {

    public  static void addFragment(Activity activity, int content, Fragment fragment, String tag)
    {

    }


    public  static void addFragmentToBackStack(Activity activity, int content,Fragment currentFragment, Fragment fragment, String tag)
    {
        if (activity==null)return;
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();

        fragmentTransaction .setCustomAnimations(R.animator.slide_right_in, R.animator.fragment_slide_left_out);

        fragmentTransaction.add(content,fragment, tag);

        fragmentTransaction.hide(currentFragment);

        fragmentTransaction.show(fragment);

        fragmentTransaction.addToBackStack(tag);

        fragmentTransaction.commit();

    }

    public  static void addFirstFragmentToBackStack(Activity activity, int content, Fragment fragment, String tag)
    {
        if (activity==null)return;

        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.add(content,fragment, tag);
        fragmentTransaction.show(fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }


    public  static void pop(Activity activity)
    {
        if (activity==null)return;
        activity.getFragmentManager().popBackStack();
    }

    public static void popRootFragment(Activity activity)
    {
        if (activity==null)return;
        FragmentManager manager=activity.getFragmentManager();
        while (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate();
            int count=manager.getBackStackEntryCount();

            if (count==1)return;

        }

    }

    public static void clearBackStack(Activity activity)
    {
        if (activity==null)return;

        FragmentManager fragmentManager=activity.getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
            int count=fragmentManager.getBackStackEntryCount();
        }
    }


}
