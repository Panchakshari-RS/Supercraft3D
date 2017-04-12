package com.trova.supercraft;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by david on 11/05/14.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout
{
	RecyclerView mAdapterView;
	int mAdapterViewId;


	public MySwipeRefreshLayout(Context context, AttributeSet attrs)
	{
		super (context, attrs);

		TypedArray mStyledAttributes = context.getTheme ().obtainStyledAttributes (attrs, R.styleable.MySwipeRefreshLayout, 0, 0);
		mAdapterViewId = mStyledAttributes.getResourceId (R.styleable.MySwipeRefreshLayout_adapter_view, -1);
		mStyledAttributes.recycle ();
	}


	@Override
	protected void onFinishInflate ()
	{
		super.onFinishInflate ();

		mAdapterView = (RecyclerView) findViewById (mAdapterViewId);
	}


	@Override
	public boolean canChildScrollUp ()
	{
		return mAdapterView.canScrollVertically (-1);
	}
}
