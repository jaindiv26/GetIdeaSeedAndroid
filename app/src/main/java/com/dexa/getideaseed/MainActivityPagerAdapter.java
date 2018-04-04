package com.dexa.getideaseed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Dev on 21/01/18.
 */

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    public Fragment getActiveFragmentAtPosition(int position){
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if(fragmentList != null && position < fragmentList.size()){
            return fragmentList.get(position);
        }
        return null;
    }

    @Override public Fragment getItem(int position) {

        switch (position){
            case 0:{
                return  HomeFragment.newInstance(null);
            }

            case 1:{
                return  ExplorerFragment.newInstance(null);
            }

            default:
                return ExplorerFragment.newInstance(null);
        }

    }

    @Override public int getCount() {
        return 2;
    }
}
