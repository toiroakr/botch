package com.example.mapdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.navdrawer.SimpleSideDrawer;

@SuppressWarnings("deprecation")
public class Fragment1 extends SupportMapFragment implements
		OnMarkerClickListener, OnInfoWindowClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment2, container, false);

		setUpMapIfNeeded();

		mSlidingMenu = new SimpleSideDrawer(getActivity());
		mSlidingMenu.setBehindContentView(R.layout.custom_info_contents2);
		v.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSlidingMenu.toggleDrawer();
			}
		});

		return v;
	}

	private SimpleSideDrawer mSlidingMenu;

	/** Demonstrates customizing the info window and/or its contents. */
	class CustomInfoWindowAdapter implements InfoWindowAdapter {
		// private final RadioGroup mOptions;

		// These a both viewgroups containing an ImageView with id "badge" and
		// two TextViews with id
		// "title" and "snippet".
		// private final View mWindow;
		private final View mContents;

		CustomInfoWindowAdapter() {
			// mWindow =
			// getLayoutInflater().inflate(R.layout.custom_info_window,
			// null);
			mContents = getActivity().getLayoutInflater().inflate(
					R.layout.custom_info_contents2, null);
			// mOptions = (RadioGroup)
			// findViewById(R.id.custom_info_window_options);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			// if (mOptions.getCheckedRadioButtonId() !=
			// R.id.custom_info_window) {

			// // This means that getInfoContents will be called.
			return null;
			// }

			// render(marker, mWindow);
			// return mWindow;
		}

		@Override
		public View getInfoContents(Marker marker) {
			// if (mOptions.getCheckedRadioButtonId() !=
			// R.id.custom_info_contents) {
			// // This means that the default info contents will be used.
			// return null;
			// }
			render(marker, mContents);
			return mContents;
		}

		private void render(Marker marker, View view) {
			// 画像のアドレス
			// int badge = 0;
			// Use the equals() method on a Marker to check for equals. Do not
			// use ==.
			// if (marker.equals(mBrisbane)) {
			// badge = R.drawable.badge_qld;
			// } else if (marker.equals(mAdelaide)) {
			// badge = R.drawable.badge_sa;
			// } else if (marker.equals(mSydney)) {
			// badge = R.drawable.badge_nsw;
			// } else if (marker.equals(mMelbourne)) {
			// badge = R.drawable.badge_victoria;
			// } else if (marker.equals(mPerth)) {
			// badge = R.drawable.badge_wa;
			// } else {
			// // Passing 0 to setImageResource will clear the image view.
			// badge = 0;
			// }

			// ((ImageView)
			// view.findViewById(R.id.badge)).setImageResource(badge);

			// String title = marker.getTitle();
			// TextView titleUi = ((TextView) view.findViewById(R.id.title));
			// if (title != null) {
			// // Spannable string allows us to edit the formatting of the
			// // text.
			// SpannableString titleText = new SpannableString(title);
			// titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
			// titleText.length(), 0);
			// titleUi.setText(titleText);
			// } else {
			// titleUi.setText("");
			// }

			// String snippet = marker.getSnippet();
			// TextView snippetUi = ((TextView)
			// view.findViewById(R.id.snippet));
			// if (snippet != null && snippet.length() > 12) {
			// SpannableString snippetText = new SpannableString(snippet);
			// snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0,
			// 10, 0);
			// snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12,
			// snippet.length(), 0);
			// snippetUi.setText(snippetText);
			// } else {
			// snippetUi.setText("");
			// }
		}
	}

	private GoogleMap mMap;

	// private Marker mPerth;
	// private Marker mSydney;
	// private Marker mBrisbane;
	// private Marker mAdelaide;
	// private Marker mMelbourne;

	private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();

	// private TextView mTopText;
	// private SeekBar mRotationBar;
	private CheckBox mFlatBox;

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		if (mMap != null) {
			// LocationMangerに最終位置を問い合わせて初期位置を確定する
			double lat = 34.65;
			double lon = 135;
			LocationManager mgr = (LocationManager) getActivity()
					.getSystemService(Context.LOCATION_SERVICE); // 位置マネージャ取得
			Location loc = mgr
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (loc == null)
				loc = mgr
						.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			if (loc != null) {
				lat = loc.getLatitude();
				lon = loc.getLongitude();
			}
			// カメラの初期位置や各種コントロール、リスナー等をセット
			CameraPosition.Builder builder = new CameraPosition.Builder()
					.bearing(0).tilt(0).zoom(16).target(new LatLng(lat, lon));
			mMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder
					.build()));
			mMap.getUiSettings().setCompassEnabled(true);
			mMap.getUiSettings().setZoomControlsEnabled(true);
			mMap.getUiSettings().setMyLocationButtonEnabled(true);
			mMap.setMyLocationEnabled(true);
		}

		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(false);

		// Add lots of markers to the map.
		addMarkersToMap();

		// Setting an info window adapter allows us to change the both the
		// contents and look of the
		// info window.
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

		// Set listeners for marker events. See the bottom of this class for
		// their behavior.
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);

	}

	private void addMarkersToMap() {
		int numMarkersInRainbow = 12;
		for (int i = 0; i < numMarkersInRainbow; i++) {
			mMarkerRainbow.add(mMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(-30
									+ 10
									* Math.sin(i * Math.PI
											/ (numMarkersInRainbow - 1)),
									135 - 10 * Math.cos(i * Math.PI
											/ (numMarkersInRainbow - 1))))
					.title("Marker " + i)
					.icon(BitmapDescriptorFactory.defaultMarker(i * 360
							/ numMarkersInRainbow))));
		}
	}

	private boolean checkReady() {
		if (mMap == null) {
			Toast.makeText(getActivity(), "mapが準備出来ていません", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	/** Called when the Clear button is clicked. */
	public void onClearMap(View view) {
		if (!checkReady()) {
			return;
		}
		mMap.clear();
	}

	/** Called when the Reset button is clicked. */
	public void onResetMap(View view) {
		if (!checkReady()) {
			return;
		}
		// Clear the map because we don't want duplicates of the markers.
		mMap.clear();
		addMarkersToMap();
	}

	/** Called when the Reset button is clicked. */
	public void onToggleFlat(View view) {
		if (!checkReady()) {
			return;
		}
		boolean flat = mFlatBox.isChecked();
		for (Marker marker : mMarkerRainbow) {
			marker.setFlat(flat);
		}
	}

	//
	// Marker related listeners.
	//
	@Override
	public boolean onMarkerClick(final Marker marker) {
		// if (marker.equals(mPerth)) {
		// // This causes the marker at Perth to bounce into position when it
		// // is clicked.
		// final Handler handler = new Handler();
		// final long start = SystemClock.uptimeMillis();
		// final long duration = 1500;
		//
		// final Interpolator interpolator = new BounceInterpolator();
		//
		// handler.post(new Runnable() {
		// @Override
		// public void run() {
		// long elapsed = SystemClock.uptimeMillis() - start;
		// float t = Math.max(
		// 1 - interpolator.getInterpolation((float) elapsed
		// / duration), 0);
		// marker.setAnchor(0.5f, 1.0f + 2 * t);
		//
		// if (t > 0.0) {
		// // Post again 16ms later.
		// handler.postDelayed(this, 16);
		// }
		// }
		// });
		// } else if (marker.equals(mAdelaide)) {
		// // This causes the marker at Adelaide to change color.
		// marker.setIcon(BitmapDescriptorFactory.defaultMarker(new Random()
		// .nextFloat() * 360));
		// }
		// We return false to indicate that we have not consumed the event and
		// that we wish
		// for the default behavior to occur (which is for the camera to move
		// such that the
		// marker is centered and for the marker's info window to open, if it
		// has one).
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Toast.makeText(getActivity(), "Click Info Window", Toast.LENGTH_SHORT)
				.show();
	}
}