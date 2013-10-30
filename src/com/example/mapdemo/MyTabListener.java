package com.example.mapdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;

import com.google.android.gms.internal.t;

@SuppressLint("ValidFragment")
public class MyTabListener extends Fragment implements TabListener {
	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;
	private final Class<t> mClass;
	private final Bundle mArgs;

	/**
	 * Constructor used each time a new tab is created.
	 * 
	 * @param activity
	 *            The host Activity, used to instantiate the fragment
	 * @param tag
	 *            The identifier tag for the fragment
	 * @param clz
	 *            The fragment's Class, used to instantiate the fragment
	 */
	public MyTabListener(Activity activity, String tag, Class<t> clz,
			Bundle args) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		mArgs = args;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Check if the fragment is already initialized
		if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			mFragment.setArguments(mArgs);
			ft.add(android.R.id.content, mFragment, mTag);
		} else {
			// If it exists, simply attach it in order to show it
			ft.attach(mFragment);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (mFragment != null) {
			// Detach the fragment, because another one is being attached
			ft.detach(mFragment);
		}
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}
}