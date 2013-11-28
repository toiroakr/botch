package kom.botch.rasaki;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

public class TabsAdapter extends FragmentPagerAdapter implements
		TabHost.OnTabChangeListener, MyViewPager.OnPageChangeListener {
	private final MainActivity mContext;
	private final TabHost mTabHost;
	private final MyViewPager mViewPager;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	private final ArrayList<TabInfo> protectedTabs = new ArrayList<TabInfo>();
	private boolean is_btn_view;

	static final class TabInfo {
		@SuppressWarnings("unused")
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;

		TabInfo(String _tag, Class<?> _class, Bundle _args) {
			tag = _tag;
			clss = _class;
			args = _args;
		}
	}

	static class DummyTabFactory implements TabHost.TabContentFactory {
		private final Context mContext;

		public DummyTabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	public TabsAdapter(MainActivity activity, TabHost tabHost, MyViewPager pager) {
		super(activity.getSupportFragmentManager());
		mContext = activity;
		mTabHost = tabHost;
		mViewPager = pager;
		mTabHost.setOnTabChangedListener(this);
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
	}

	public TabInfo addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args,
			boolean protect) {
		TabInfo info = addTab(tabSpec, clss, args);
		if (protect)
			protectedTabs.add(info);
		return info;
	}

	public TabInfo addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
		tabSpec.setContent(new DummyTabFactory(mContext));
		String tag = tabSpec.getTag();

		TabInfo info = new TabInfo(tag, clss, args);
		mTabs.add(info);
		mTabHost.addTab(tabSpec);
		notifyDataSetChanged();
		return info;
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d("onTabChanged", "" + tabId);
		int position = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(position, false);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		Log.d("onPageScrolled", "" + position);
	}

	@Override
	public void onPageSelected(int position) {
		Log.d("onPageSelected", "" + position);
		int preTab = mTabHost.getCurrentTab();
		int visible = mContext.findViewById(R.id.marker_btns).getVisibility();
		if (preTab == 0 && visible == View.VISIBLE) {
			is_btn_view = true;
			mContext.hideButtons();
		} else if (position == 0 && is_btn_view == true)
			mContext.viewButtons();

		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		widget.setDescendantFocusability(oldFocusability);
		mTabHost.setCurrentTab(position);

		if (position == 0) {
			mViewPager.setRSwipeHold(false);
			mViewPager.setLSwipeHold(true);
		} else if (position == mTabs.size() - 1) {
			mViewPager.setRSwipeHold(true);
			mViewPager.setLSwipeHold(false);
		} else {
			mViewPager.setRSwipeHold(false);
			mViewPager.setLSwipeHold(false);
		}
		if(position == 1)
			TitleFragment.redraw();
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (protectedTabs.contains(mTabs.get(position)))
			return;
		super.destroyItem(container, position, object);
	}
}
