package com.example.mapdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
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
		OnMarkerClickListener {
	private GoogleMap mMap;
	private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
	private SimpleSideDrawer drawer;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FrameLayout mapView = (FrameLayout) super.onCreateView(inflater,
				container, savedInstanceState);
		setUpMapIfNeeded();

		if (drawer == null) {
			drawer = new SimpleSideDrawer(getActivity());
			// 一時的なコード
			drawer.setBehindContentView(R.layout.side_list_contents);
			ImageView img = (ImageView) drawer.findViewById(R.id.backImg);
			img.setScaleType(ImageView.ScaleType.FIT_CENTER);
			img.setImageResource(R.drawable.ic_launcher);
			Button b = (Button) drawer.findViewById(R.id.detail);
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int rand = new Random().nextInt(mMarkerRainbow.size() - 1);
					Marker tarM = mMarkerRainbow.get(rand);
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

	@SuppressWarnings("deprecation")
	private View addToggleButton(LayoutInflater inflater, ViewGroup container) {
		View layout;
		if ((layout = getActivity().findViewById(R.id.btn_frame)) == null) {
			layout = inflater.inflate(R.layout.drawer_toggle_btn, container,
					false);
			layout.findViewById(R.id.drawer_btn).setOnClickListener(
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
		// Do a null check to confirm
		// that we have not already instantiated the map.
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

			// 各種コントロール、リスナー等をセット
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

			// カメラの初期位置をセット
			double lat = 35.;
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
			CameraPosition.Builder builder = new CameraPosition.Builder()
					.bearing(0).tilt(0).zoom(16).target(new LatLng(lat, lon));
			mMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder
					.build()));
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
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker arg0) {
				showDetail();
			}
		});
	}

	private void addMarkersToMap() {
		double lat = 35.;
		double lon = 135;
		LocationManager mgr = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE); // 位置マネージャ取得
		Location loc = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null)
			loc = mgr.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if (loc != null) {
			lat = loc.getLatitude();
			lon = loc.getLongitude();
		}

		int numMarkersInRainbow = 8;
		double r = 0.001;
		double angle = 2 * Math.PI;
		for (int i = 0; i < numMarkersInRainbow; i++) {
			double posLat = lat - r * Math.sin(angle * i / numMarkersInRainbow);
			double posLon = lon + r * Math.cos(i * angle / numMarkersInRainbow);
			mMarkerRainbow.add(mMap.addMarker(new MarkerOptions()
					.position(new LatLng(posLat, posLon))
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
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
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
		private final View mContents;

		CustomInfoWindowAdapter() {
			mContents = getActivity().getLayoutInflater().inflate(
					R.layout.info_contents, null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}

		@Override
		public View getInfoContents(Marker marker) {
			render(marker, mContents);
			return mContents;
		}

		private void render(Marker marker, View view) {
			TextView titleUi = ((TextView) view.findViewById(R.id.url));
			titleUi.setText("" + marker.getPosition().longitude);
			RatingBar rate = ((RatingBar) view.findViewById(R.id.rate));
			rate.setRating(new Random().nextInt(5) + 1);
		}
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		((MainActivity) getActivity()).viewButtons();
		setBtns(marker);
		return false;
	}

	private void setBtns(final Marker marker) {
		TextView eBtn = (TextView) getActivity().findViewById(R.id.eat_btn);
		eBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//((MainActivity) getActivity()).showEvalDialog(new Restaurant(1,"天下一品", 0, "フレンチ"), marker);
			}
		});
		TextView dBtn = (TextView) getActivity().findViewById(R.id.detail_btn);
		dBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDetail();
			}
		});
	}

	private void showDetail() {
		// 詳細ページへ
		Intent intent = new Intent(getActivity(), RstDetail.class);
		startActivity(intent);
	}
}