package com.dexa.getideaseed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Dev on 21/01/18.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<ModelExplorer> modelExplorerArrayList;
    private Bundle bundle = new Bundle();

    public MainPagerAdapter(FragmentManager fm, ArrayList<ModelExplorer> arrayList) {
        super(fm);
        modelExplorerArrayList = arrayList;

        bundle.putParcelableArrayList("arr", modelExplorerArrayList);
    }

    @Override public Fragment getItem(int position) {

        switch (position){
            case 0:{
                return  HomeFragment.newInstance(bundle);
            }

            case 1:{
                return  ExplorerFragment.newInstance(bundle);
            }

            default:
                return ExplorerFragment.newInstance(bundle);
        }

    }

    @Override public int getCount() {
        return 2;
    }
}
