package com.trova.supercraft;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import static com.trova.supercraft.SuperCraftUtils.logInfo;

/**
 * Created by Trova on 11/2/2016.
 */
public class MyJobsDetailsAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private MySTLViewerTabFragment mySTLViewerTabFragment = null;
    private MyJobDetailsTabFragment myJobDetailsTabFragment = null;
    private MyJobNotesTabFragment myJobNotesTabFragment = null;
    private MyJobApproveTabFragment myJobApproveTabFragment = null;
    private MyJobChatTabFragment myJobChatTabFragment = null;

    public MyJobsDetailsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        logInfo("MyJobsDetailsAdapter getItem", position);
        switch (position) {
            case 2:
                if(mySTLViewerTabFragment == null) {
                    logInfo("MySTLViewerTabFragment", position);
                    mySTLViewerTabFragment = new MySTLViewerTabFragment();
                }
                return mySTLViewerTabFragment;
            case 0:
                if(myJobDetailsTabFragment == null) {
                    logInfo("MyJobDetailsTabFragment", position);
                    myJobDetailsTabFragment = new MyJobDetailsTabFragment();
                }
                return myJobDetailsTabFragment;
/*
            case 1:
                if(myJobNotesTabFragment == null) {
                    logInfo("MyJobNotesTabFragment", position);
                    myJobNotesTabFragment = new MyJobNotesTabFragment();
                }
                return myJobNotesTabFragment;
*/
            case 1:
                if(myJobChatTabFragment == null) {
                    logInfo("MyJobChatTabFragment", position);
                    myJobChatTabFragment = new MyJobChatTabFragment();
                }
                return myJobChatTabFragment;
            case 3:
                logInfo("MyJobApproveTabFragment", position);
                if(myJobApproveTabFragment == null) {
                    logInfo("MyJobApproveTabFragment", position);
                    myJobApproveTabFragment = new MyJobApproveTabFragment();
                }
                return myJobApproveTabFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
