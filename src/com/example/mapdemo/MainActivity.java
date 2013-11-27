package com.example.mapdemo;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.JsonObject;
import com.navdrawer.SimpleSideDrawer;

public class MainActivity extends FragmentActivity {
	TabHost mTabHost;
	MyViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	private SimpleSideDrawer drawer;

	// /////////////////
	// /////通信用///////
	// /////////////////
	RequestQueue requestQueue;
	HashMap<String, String> params = new HashMap<String, String>();
	int method;
	String url;
	static final Object TAG_REQUEST_QUEUE = new Object();

	// ///////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs_pager);


		// //通信用/////
		requestQueue = Volley.newRequestQueue(this);
		// ///////////

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
						getIndicator("Resta")), TitleFragment.class, null);
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

	// ////////////////////////////////
	// ///////////通信用////////////////
	private void startRequest(int method, String url,
			HashMap<String, String> params) {
		// マッピング用のRestaurantDetailを作成
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, url,
				JsonObject.class, params, new Listener<JsonObject>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(JsonObject result) {
						// success
						Log.v("success:", result.toString());
						Log.v("success:", "DONE!");
						Toast.makeText(getApplicationContext(),
								result.toString(), Toast.LENGTH_LONG).show();
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						Log.e("error:", error.toString() + "：再読み込みしてください");
						Toast.makeText(getApplicationContext(),
								"onErrorResponse", Toast.LENGTH_LONG).show();
					}
				});
		// requestQueueに上で定義したreqをaddすることで、非同期通信が行われる
		// Queueなので、入れた順番に通信される
		// 通信が終われば、それぞれのreqで定義したコールバック関数が呼ばれる
		req.setTag(TAG_REQUEST_QUEUE);
		requestQueue.add(req);
	}

	public void showEvalDialog(Marker marker) {
		// ここに店の情報が入ってるはず
		// 情報が足りないならRestaurantクラスを拡張する必要あり（rst_idとか）
		Restaurant rst = MyMapFragment.getRestaurant(marker);

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

				// ////////////////////
				// /////通信用//////////
				method = Method.POST;
				url = "/post";
				params.clear();
				params.put("rst_id", "26001581");
				params.put("user_id", "19");
				params.put("difficulty", Integer.toString(intRate));
				params.put("comment", strComment);
				startRequest(method, url, params);
				// ////////////////////

				diaLog.dismiss();
			}
		});
	}

	public SimpleSideDrawer getDrawer() {
		if (drawer == null)
			return initDrawer();
		return drawer;
	}

	public SimpleSideDrawer initDrawer() {
		drawer = new SimpleSideDrawer(this);
		// drawer.setLeftBehindContentView(R.layout.side_list_contents);
		drawer.setLeftBehindContentView(R.layout.side_list);
		return drawer;
	}

	public int getCurrentTab() {
		Log.d("Marker", "current : " + mTabHost.getCurrentTab());
		return mTabHost.getCurrentTab();
	}
}