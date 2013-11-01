package com.example.mapdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.navdrawer.SimpleSideDrawer;

public class MyMapFragment extends SupportMapFragment implements
		OnMarkerClickListener/* , OnInfoWindowClickListener */{
	private GoogleMap mMap;
	private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
	private SimpleSideDrawer drawer;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// FrameLayout f = (FrameLayout) addDrawer(inflater, container);
		FrameLayout mapView = (FrameLayout) super.onCreateView(inflater,
				container, savedInstanceState);
		setUpMapIfNeeded();

		if (drawer == null) {
			addDrawer();
			drawer.setBehindContentView(R.layout.custom_info_contents2);
			ImageView img = (ImageView) drawer.findViewById(R.id.backImg);
			img.setScaleType(ImageView.ScaleType.FIT_CENTER);
			img.setImageResource(R.drawable.ic_launcher);
			Button b = (Button) drawer.findViewById(R.id.detail);
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Marker tarM = mMarkerRainbow.get(0);
					tarM.showInfoWindow();
					drawer.toggleDrawer();
					CameraPosition.Builder builder = new CameraPosition.Builder()
							.bearing(0).tilt(0).zoom(16)
							.target(tarM.getPosition());
					mMap.moveCamera(CameraUpdateFactory
							.newCameraPosition(builder.build()));
					((MainActivity) getActivity()).viewButtons();
					setBtns(tarM);
				}
			});
		}
		mapView.addView(addToggleButton(inflater, container));

		return mapView;
	}

	private void addDrawer() {
		drawer = new SimpleSideDrawer(getActivity());
	}

	@SuppressWarnings("deprecation")
	private View addToggleButton(LayoutInflater inflater, ViewGroup container) {
		View layout;
		if ((layout = getActivity().findViewById(R.id.btn_frame)) == null) {
			layout = inflater.inflate(R.layout.fragment2, container, false);
			layout.findViewById(R.id.btn).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							drawer.toggleDrawer();
						}
					});
		}
		return layout;
	}

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
			mMap.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public void onMapClick(LatLng arg0) {
					((MainActivity) getActivity()).hideButtons();
				}
			});

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
		// mMap.setOnInfoWindowClickListener(this);
	}

	private void addMarkersToMap() {
		int numMarkersInRainbow = 6;
		for (int i = 0; i < numMarkersInRainbow; i++) {
			mMarkerRainbow
					.add(mMap.addMarker(new MarkerOptions()
							.position(
									new LatLng(35 - 0.2 * Math.sin(2 * i
											* Math.PI / numMarkersInRainbow),
											135.7 + 0.2 * Math.cos(2 * i
													* Math.PI
													/ numMarkersInRainbow)))
							.title("Marker " + i)
							.icon(BitmapDescriptorFactory.defaultMarker(i * 360
									/ numMarkersInRainbow))));
		}
	}

	public static MyMapFragment newInstance() {
		MyMapFragment fragment = new MyMapFragment();
		fragment.setRetainInstance(true);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Nothing to see here.
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// Nothing to see here.
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Nothing to see here.
		return false;
	}

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
					R.layout.custom_info_contents, null);
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
			TextView titleUi = ((TextView) view.findViewById(R.id.url));
			titleUi.setText("" + marker.getPosition().longitude);
			RatingBar rate = ((RatingBar) view.findViewById(R.id.rate));
			rate.setRating(new Random().nextInt(5) + 1);
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

	@Override
	public boolean onMarkerClick(final Marker marker) {
		((MainActivity) getActivity()).viewButtons();
		setBtns(marker);
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

	private void setBtns(final Marker marker) {
		Button mBtn = (Button) getActivity().findViewById(R.id.eat);
		mBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(),
						marker.getPosition().longitude + "", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
	// @Override
	// public void onInfoWindowClick(Marker marker) {
	// Toast.makeText(getActivity(), "Click Info Window", Toast.LENGTH_SHORT)
	// .show();
	// }
}