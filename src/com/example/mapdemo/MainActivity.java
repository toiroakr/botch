package com.example.mapdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.map);

		setContentView(R.layout.activity_main);

		MyFragmentTabHost host = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
		host.setup(this, getSupportFragmentManager(), R.id.content);
		hideButtons();

		TabSpec tabSpec2 = host.newTabSpec("Map");
		Button button2 = new Button(this);
		button2.setBackgroundResource(R.drawable.ic_launcher);
		tabSpec2.setIndicator(button2);
		Bundle bundle2 = new Bundle();
		bundle2.putString("name", "Map");
		host.addTab(tabSpec2, MyMapFragment.class, bundle2);

		TabSpec tabSpec1 = host.newTabSpec("tab1");
		Button button1 = new Button(this);
		button1.setBackgroundResource(R.drawable.ic_launcher);
		tabSpec1.setIndicator(button1);
		Bundle bundle1 = new Bundle();
		bundle1.putString("name", "Tab1");
		host.addTab(tabSpec1, RestaurantFragment.class, bundle1);

		TabSpec tabSpec3 = host.newTabSpec("tab3");
		Button button3 = new Button(this);
		button3.setBackgroundResource(R.drawable.ic_launcher);
		tabSpec3.setIndicator(button3);
		Bundle bundle3 = new Bundle();
		bundle3.putString("name", "Tab3");
		host.addTab(tabSpec3, RequestAndParser.class, bundle3);
	}

	public void hideButtons() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.container);
		LinearLayout ll = (LinearLayout) rl.findViewById(R.id.marker_btns);
		rl.removeView(ll);
		rl.addView(ll, 0);
	}

	public void viewButtons() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.container);
		LinearLayout ll = (LinearLayout) rl.findViewById(R.id.marker_btns);
		rl.removeView(ll);
		rl.addView(ll);
	}
}