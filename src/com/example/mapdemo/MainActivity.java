package com.example.mapdemo;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		MyViewPager.OnPageChangeListener {
	TabHost mTabHost;
	MyViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	private boolean is_btn_view = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs_pager);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (MyViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		mViewPager.setOnPageChangeListener(this);

		mTabsAdapter.addTab(
				mTabHost.newTabSpec("test").setIndicator(getIndicator("MAP")),
				MyMapFragment.class, null);
		mTabsAdapter
				.addTab(mTabHost.newTabSpec("test").setIndicator(
						getIndicator("Resta")), RestaurantFragment.class, null);
		mTabsAdapter
				.addTab(mTabHost.newTabSpec("test").setIndicator(
						getIndicator("RandP")), RequestAndParser.class, null);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	public View getIndicator(String str) {
		View view1 = View.inflate(getApplicationContext(), R.layout.tabview,
				null);
		TextView text = (TextView) view1.findViewById(R.id.tab_text);
		text.setText(str);
		return view1;
	}

	public void hideButtons() {
		findViewById(R.id.marker_btns).setVisibility(View.INVISIBLE);
	}

	public void viewButtons() {
		findViewById(R.id.marker_btns).setVisibility(View.VISIBLE);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final MyViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

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

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				MyViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position, false);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		// Unfortunately when TabHost changes the current tab, it kindly
		// also takes care of putting focus on it when not in touch mode.
		// The jerk.
		// This hack tries to prevent this from pulling focus out of our
		// ViewPager.
		int preTab = mTabHost.getCurrentTab();
		int visible = findViewById(R.id.marker_btns).getVisibility();
		if (preTab == 0 && visible == View.VISIBLE) {
			is_btn_view = true;
			hideButtons();
		} else if (position == 0 && is_btn_view == true)
			viewButtons();
		else
			is_btn_view = false;

		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(position);
		widget.setDescendantFocusability(oldFocusability);

		if (position == 0)
			mViewPager.setSwipeHold(true);
		else
			mViewPager.setSwipeHold(false);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	public void showEvalDialog(final Context context, Restaurant rst) {
		// カスタムビューを設定
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.eval_dialog,
				(ViewGroup) findViewById(R.id.layout_root));

		// アラートダイアログを生成
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(rst.getRestaurantName() + "の評価");
		builder.setView(layout);

		builder.setPositiveButton("Send", null);
		builder.setNegativeButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "Cancelled!!!!!!!!!",
						Toast.LENGTH_SHORT).show();
			}
		});

		// 表示
		final AlertDialog diaLog = builder.show();
		Button buttonOK = diaLog.getButton(DialogInterface.BUTTON_POSITIVE);
		buttonOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send ボタンクリック処理
				EditText comment = (EditText) layout
						.findViewById(R.id.eval_comment);
				RatingBar rate = (RatingBar) layout
						.findViewById(R.id.eval_rate);
				String strComment = comment.getText().toString();
				int intRate = (int) rate.getRating();
				if (intRate == 0) {// Ratingが0：評価していない時
					Toast toast = Toast.makeText(context, "ちゃんと評価してや～！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, -100);
					toast.show();
					return;
				}
				Toast.makeText(context, strComment + ":" + intRate,
						Toast.LENGTH_SHORT).show();
				diaLog.dismiss();
			}
		});
	}
}