package com.example.mapdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.navdrawer.SimpleSideDrawer;

public class MyMapFragment extends SupportMapFragment implements
		OnMarkerClickListener {
	private GoogleMap mMap;
	private static final Map<Marker, Restaurant> mMarkers = new HashMap<Marker, Restaurant>();
	private SimpleSideDrawer drawer;

	private static final Object TAG_REQUEST_QUEUE = new Object();
	private RequestQueue requestQueue;
	private HashMap<String, String> params = new HashMap<String, String>();
	private int method;
	private String url;
	private HashMap<Integer, Restaurant> restaurants = new HashMap<Integer, Restaurant>();
	private boolean fetching = false;

	// setParamsに注意
	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public void setRequestQueue(RequestQueue requestQueue) {
		this.requestQueue = requestQueue;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams() {
		this.params.put("params", "hoge");
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private Restaurant fetchNearRsts() {
		fetching = true;
		Location loc = this.getLocation();
		// CameraPosition loc = this.getLocation();
		// lat = loc.target.lat;
		// zoom = loc.zoom;
		double lat = loc.getLatitude();
		double lng = loc.getLongitude();
		int zoom = 1;
		this.startRequest(lat, lng, zoom);
		fetching = false;
		return null;
	}

	private void startRequest(double lat, double lng, int zoom) {
		// マッピング用のRestaurantDetailを作成
		params.put("zoom", Integer.toString(zoom));
		params.put("lat", Double.toString(lat));
		params.put("lng", Double.toString(lng));
		params.put("limit", "100");
		url = "/near_rst";
		method = Method.POST;
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, url,
				JsonObject.class, params, new Listener<JsonObject>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(JsonObject rst) {
						// success
						JsonArray results = (JsonArray) rst.get("result");
						int rst_id;
						String restaurantName;
						double raw_difficulty;
						int difficulty;
						String category;
						JsonObject json_restaurant;
						Restaurant restaurant;
						for (int i = 0, length = results.size(); i < length; i++) {
							json_restaurant = results.get(i).getAsJsonObject();
							rst_id = Integer.parseInt(json_restaurant.get(
									"rst_id").toString());
							restaurantName = json_restaurant.get(
									"RestaurantName").toString();
							category = json_restaurant.get("Category")
									.toString();
							raw_difficulty = Double.parseDouble(json_restaurant
									.get("raw_difficulty").toString());
							difficulty = Integer.parseInt(json_restaurant.get(
									"difficulty").toString());
							restaurant = new Restaurant(rst_id, restaurantName,
									raw_difficulty, difficulty, category);
							restaurants.put(rst_id, restaurant);
						}
						Log.v("success:", restaurants.toString());
						Log.v("success:", "DONE!");
						Toast.makeText(getActivity(), rst.toString(),
								Toast.LENGTH_LONG).show();
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						Log.v("error:", error.toString() + "：再読み込みしてください");
						Toast.makeText(getActivity(), "onErrorResponse",
								Toast.LENGTH_LONG).show();
					}
				});
		// requestQueueに上で定義したreqをaddすることで、非同期通信が行われる
		// Queueなので、入れた順番に通信される
		// 通信が終われば、それぞれのreqで定義したコールバック関数が呼ばれる
		req.setTag(TAG_REQUEST_QUEUE);
		requestQueue.add(req);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FrameLayout mapView = (FrameLayout) super.onCreateView(inflater,
				container, savedInstanceState);
		setUpMapIfNeeded();

		if (drawer == null)
			drawer = ((MainActivity) getActivity()).getDrawer();
		mapView.addView(addToggleButton(inflater, container));

		renewRsts();

		return mapView;
	}

	private void renewRsts() {
		addMarkers();
		setRestaurantList();
	}

	private void setRestaurantList() {
		// ListViewに表示するデータを作成する
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			list.add("hoge" + i);
		}

		fetchNearRsts();

		while (fetching) {
			ListView listView = (ListView) drawer.findViewById(R.id.rst_list);
			// android.R.layout.simple_list_item_1はAndroidで既に定義されているリストアイテムのレイアウトです
			RstListItemAdapter adapter = (RstListItemAdapter) listView
					.getAdapter();
			if (adapter == null) {
				Toast.makeText(getActivity(), "adapter = null",
						Toast.LENGTH_LONG).show();
				adapter = new RstListItemAdapter(getActivity());
			}
			adapter.addAll(restaurants);

			listView.setAdapter(adapter);
			// タップした時の動作を定義する
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Restaurant tarR = (Restaurant) parent
							.getItemAtPosition(position);
					Marker tarM = null;
					for (Marker m : mMarkers.keySet())
						if (tarR.equals(mMarkers.get(m))) {
							tarM = m;
							break;
						}
					// onMarkerClickがtrueだと
					// Map画面じゃない
					if (onMarkerClick(tarM))
						return;
					tarM.showInfoWindow();
					drawer.toggleLeftDrawer();
					CameraPosition.Builder builder = new CameraPosition.Builder()
							.bearing(0).tilt(0).zoom(16)
							.target(tarM.getPosition());
					mMap.moveCamera(CameraUpdateFactory
							.newCameraPosition(builder.build()));
				}
			});
			try {
				Log.d("sleep", "sleep");
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private View addToggleButton(LayoutInflater inflater, ViewGroup container) {
		View layout;
		if ((layout = getActivity().findViewById(R.id.btn_frame)) == null) {
			layout = inflater.inflate(R.layout.drawer_toggle_btn, container,
					false);
			layout.findViewById(R.id.drawer_btn).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							drawer.toggleLeftDrawer();
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
			Location loc = getMyLocation();
			double lat = 35.;
			double lon = 135;
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
		addMarkers();

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

		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
				// LatLng point = position.target;
				// String text = "latitude=" + point.latitude + ", longitude="
				// + point.longitude;
				// CameraPosition camPos = mMap.getCameraPosition();
				renewRsts();
			}
		});

	}

	private Location getMyLocation() {
		LocationManager mgr = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE); // 位置マネージャ取得
		Location loc = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null)
			loc = mgr.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		return loc;
	}

	private void addMarkers() {
		Location loc = getMyLocation();
		double lat = loc.getLatitude();
		double lon = loc.getLongitude();

		Random rand = new Random();
		int numMarkersInRainbow = rand.nextInt(10) + 1;
		double r = 0.001;
		double angle = 2 * Math.PI;
		for (int i = 0; i < numMarkersInRainbow; i++) {
			Restaurant sampleRst = new Restaurant(mMarkers.size(), "天下一品"
					+ mMarkers.size(), rand.nextInt(6), rand.nextInt(6),
					"フレンチ",
					lon + r * Math.cos(i * angle / numMarkersInRainbow), lat
							- r * Math.sin(angle * i / numMarkersInRainbow));
			addMarker(sampleRst, i, numMarkersInRainbow);
		}
	}

	private Marker addMarker(Restaurant rst, int i, int n) {
		Marker m = mMap.addMarker(new MarkerOptions()
				.position(new LatLng(rst.getLat(), rst.getLon()))
				.title(rst.getRestaurantName())
				.icon(BitmapDescriptorFactory.defaultMarker(i * 360 / n)));
		mMarkers.put(m, rst);
		return m;
	}

	private Location getLocation() {
		LocationManager mgr = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE); // 位置マネージャ取得
		Location loc = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null)
			loc = mgr.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		return loc;
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
		requestQueue = Volley.newRequestQueue(this.getActivity());

	}

	@Override
	public void onStop() {
		super.onStop();
		requestQueue.cancelAll(TAG_REQUEST_QUEUE);
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
		if (((MainActivity) getActivity()).getCurrentTab() != 0)
			return true;
		((MainActivity) getActivity()).viewButtons();
		setBtns(marker);
		return false;
	}

	private void setBtns(final Marker marker) {
		TextView eBtn = (TextView) getActivity().findViewById(R.id.eat_btn);
		eBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showEvalDialog(marker);
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

	public static Restaurant getRestaurant(Marker m) {
		return mMarkers.get(m);
	}
}