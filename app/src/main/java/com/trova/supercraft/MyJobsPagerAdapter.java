package com.trova.supercraft;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import static com.trova.supercraft.SuperCraftUtils.logInfo;

/**
 * Created by Trova on 11/2/2016.
 */
public class MyJobsPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private MyNewJobsTabFragment myNewJobsTabFragment = null;
    private MyInprogressJobsTabFragment myInprogressJobsTabFragment = null;
    private MyCompletedJobsTabFragment myCompletedJobsTabFragment = null;
    private MyCriticalJobsTabFragment myCriticalJobsTabFragment = null;

    public MyJobsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        logInfo("getItem", position);
        switch (position) {
            case 0:
                if(myNewJobsTabFragment == null) {
                    logInfo("MyNewJobsTabFragment", position);
                    myNewJobsTabFragment = new MyNewJobsTabFragment();
                }
                return myNewJobsTabFragment;
            case 1:
                if(myInprogressJobsTabFragment == null) {
                    logInfo("MyInprogressJobsTabFragment", position);
                    myInprogressJobsTabFragment = new MyInprogressJobsTabFragment();
                }
                return myInprogressJobsTabFragment;
            case 2:
                if(myCompletedJobsTabFragment == null) {
                    logInfo("MyCompletedJobsTabFragment", position);
                    myCompletedJobsTabFragment = new MyCompletedJobsTabFragment();
                }
                return myCompletedJobsTabFragment;
            case 3:
                if(myCriticalJobsTabFragment == null) {
                    logInfo("myCriticalJobsTabFragment", position);
                    myCriticalJobsTabFragment = new MyCriticalJobsTabFragment();
                }
                return myCriticalJobsTabFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
