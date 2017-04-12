package com.trova.supercraft.Profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import static com.trova.supercraft.SuperCraftUtils.logInfo;

/**
 * Created by Trova on 11/2/2016.
 */
public class MyProfileAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private MyChangePasswordActivity myChangePasswordActivity = null;
    private MyEditProfileActivity myEditProfileActivity = null;

    public MyProfileAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        logInfo("getItem", position);
        switch (position) {
/*
            case 0:
                if(myChangePasswordActivity == null) {
                    logInfo("MyChangePasswordActivity", position);
                    myChangePasswordActivity = new MyChangePasswordActivity();
                }
                return myChangePasswordActivity;
*/
/*
            case 1:
                if(myEditProfileActivity == null) {
                    logInfo("myEditProfileActivity", position);
                    myEditProfileActivity = new MyEditProfileActivity();
                }
                return myEditProfileActivity;
*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
