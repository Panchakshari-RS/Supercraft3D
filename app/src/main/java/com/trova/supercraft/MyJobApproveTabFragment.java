package com.trova.supercraft;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.trova.supercraft.SuperCraftUtils.logInfo;

/**
 * Created by Panchakshari on 16/3/2017.
 */

public class MyJobApproveTabFragment extends Fragment {
    private static Context context;
    private View contentview = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logInfo("MyJobNotesTabFragment", "0 .........");
        contentview = inflater.inflate(R.layout.notes_tab, container, false);
        context = this.getContext();

        return contentview;
    }

}