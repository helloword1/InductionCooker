package com.goockr.inductioncooker.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.goockr.ui.view.FragmentTransactionExtended;

/**
 * Created by CMQ on 2017/7/5.
 * @author CMQ
 */

public class FragmentHelper {

    public  static void addFragment(Activity activity, int content, Fragment fragment, String tag)
    {

    }


    public  static void addFragmentToBackStack(Activity activity, int content,Fragment currentFragment, Fragment fragment, String tag)
    {
        if (activity==null){
            return;
        }
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(activity, fragmentTransaction, currentFragment, fragment, content,tag);
        fragmentTransactionExtended.addTransition(7);
        fragmentTransactionExtended.commit();


    }
    public  static void addFirstFragmentToBackStack(Activity activity, int content, Fragment fragment, String tag)
    {
        if (activity==null){
            return;
        }

        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.add(content,fragment, tag);
        fragmentTransaction.show(fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }


    public  static void pop(Activity activity)
    {
        if (activity==null){
            return;
        }
        activity.getFragmentManager().popBackStack();
    }

    public  static void pop(Fragment fragment)
    {
        if (fragment==null){
            return;
        }
        fragment.getFragmentManager().popBackStack();
    }

    public  static void pop(Fragment fragment,String tag,int flags)
    {
       fragment.getFragmentManager().popBackStack(tag,flags);
    }


    public static void popRootFragment(Activity activity)
    {
        if (activity==null){
            return;
        }
        FragmentManager manager=activity.getFragmentManager();
        while (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate();
            int count=manager.getBackStackEntryCount();

            if (count==1){
                return;
            }

        }
    }


    public static void clearBackStack(Activity activity)
    {
        if (activity==null){
            return;
        }

        FragmentManager fragmentManager=activity.getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
            int count=fragmentManager.getBackStackEntryCount();
        }
    }


}
