package com.trova.supercraft;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Panchakshari on 24/3/2017.
 */

public class TabLayoutUtils {

    public static void enableAllTabs(TabLayout tabLayout, Boolean enable){
        ViewGroup viewGroup = getTabViewGroup(tabLayout);
        if (viewGroup != null)
            for (int childIndex = 0; childIndex < viewGroup.getChildCount(); childIndex++)
            {
                View tabView = viewGroup.getChildAt(childIndex);
                if ( tabView != null)
                    tabView.setEnabled(enable);
            }
    }

    public static View getTabView(TabLayout tabLayout, int position){
        View tabView = null;
        ViewGroup viewGroup = getTabViewGroup(tabLayout);
        if (viewGroup != null && viewGroup.getChildCount() > position)
            tabView = viewGroup.getChildAt(position);

        return tabView;
    }

    private static ViewGroup getTabViewGroup(TabLayout tabLayout){
        ViewGroup viewGroup = null;

        if (tabLayout != null && tabLayout.getChildCount() > 0 ) {
            View view = tabLayout.getChildAt(0);
            if (view != null && view instanceof ViewGroup)
                viewGroup = (ViewGroup) view;
        }
        return viewGroup;
    }

    public static void disableTabs(TabLayout tabLayout, int[] tabIds) {
        View tabView = null;
        ViewGroup viewGroup = getTabViewGroup(tabLayout);
        if (viewGroup != null) {
            for (int position = 0; (position < tabIds.length); position++) {
                if (viewGroup.getChildCount() > position) {
                    if (tabIds[position] == 1) {
                        tabView = viewGroup.getChildAt(position);
                        if (tabView != null) {
                            tabView.setEnabled(false);
                        }
                    }
                }
            }
        }
    }

    public static void enableTabs(TabLayout tabLayout, int[] tabIds){
        View tabView = null;
        ViewGroup viewGroup = getTabViewGroup(tabLayout);
        if (viewGroup != null) {
            for (int position = 0; (position < tabIds.length); position++) {
                if (viewGroup.getChildCount() > position) {
                    if (tabIds[position] == 1) {
                        tabView = viewGroup.getChildAt(position);
                        if (tabView != null) {
                            tabView.setEnabled(true);
                        }
                    }
                }
            }
        }
    }


}
