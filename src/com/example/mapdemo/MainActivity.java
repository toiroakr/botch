package com.example.mapdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

public class MainActivity extends FragmentActivity {
	TabHost mTabHost;
	MyViewPager mViewPager;
	TabsAdapter mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs_pager);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mViewPager = (MyViewPager) findViewById(R.id.pager);
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		// タブの追加
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("test").setIndicator(getIndicator("MAP")),
				MyMapFragment.class, null, true);
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

	public void showEvalDialog(Restaurant rst, Marker marker) {
		// カスタムビューを設定
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.eval_dialog,
				(ViewGroup) findViewById(R.id.layout_root));

		final Context context = this;
		Toast.makeText(context, marker.getId() + " : " + marker.getTitle(),
				Toast.LENGTH_SHORT).show();
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