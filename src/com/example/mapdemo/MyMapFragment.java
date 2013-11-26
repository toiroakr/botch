package com.example.mapdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
	String LIMIT = "15";
	private static final Object TAG_REQUEST_QUEUE = new Object();
	private RequestQueue requestQueue;
	private HashMap<String, String> params = new HashMap<String, String>();
	private int method;
	private String url;
	private HashMap<Integer, Restaurant> restaurants = new HashMap<Integer, Restaurant>();
	private ArrayList<Integer> puted_rstids = new ArrayList<Integer>();

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
		LatLng loc = this.getLocation();
		double lat = loc.latitude;
		double lng = loc.longitude;
		int zoom = 1;
		String lonly = "1"; // 1で「一人で」、それ以外で「全て」
		this.startRequest(lat, lng, zoom, lonly);
		return null;
	}

	private void startRequest(double lat, double lng, int zoom, String lonly) {
		// マッピング用のRestaurantDetailを作成
		params.put("zoom", Integer.toString(zoom));
		params.put("lat", Double.toString(lat));
		params.put("lng", Double.toString(lng));
		params.put("limit", LIMIT);
		params.put("lonly", lonly);
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
						double lat;
						double lng;
						int difficulty;
						String category;
						JsonObject json_restaurant;
						Restaurant restaurant;

						if (puted_rstids.size() >= 150) {
							for (int i = 0; i < Integer.parseInt(LIMIT); i++) {
								int remove_rstid = puted_rstids.get(0);
								restaurants.remove(remove_rstid);
								puted_rstids.remove(0);
							}
							Log.v("size", Integer.toString(puted_rstids.size()));
						}

						// restaurants.clear();

						for (int i = 0, length = results.size(); i < length; i++) {
							json_restaurant = results.get(i).getAsJsonObject();
							rst_id = Integer.parseInt(json_restaurant.get(
									"rst_id").toString());
							puted_rstids.add(rst_id);
							restaurantName = json_restaurant
									.get("RestaurantName").toString()
									.replace("\"", "");
							category = json_restaurant.get("Category")
									.toString();
							raw_difficulty = Double.parseDouble(json_restaurant
									.get("raw_difficulty").toString());
							difficulty = Integer.parseInt(json_restaurant.get(
									"difficulty").toString());
							lat = Double.parseDouble(json_restaurant.get("lat")
									.toString());
							lng = Double.parseDouble(json_restaurant.get("lng")
									.toString());
							restaurant = new Restaurant(rst_id, restaurantName,
									raw_difficulty, difficulty, category, lat,
									lng);
							restaurants.put(rst_id, restaurant);
						}
						// Log.v("success:", restaurants.toString());
						Log.v("success:", "DONE!");
						addMarkers(true);
						setRestaurantList(true);
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						Log.v("error:", error.toString() + "：再読み込みしてください");
						Toast.makeText(getActivity(), "Error: この辺りにお店がありません",
								Toast.LENGTH_SHORT).show();
						requestQueue.cancelAll(TAG_REQUEST_QUEUE);
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
		checkUserSetting();
		setUpMapIfNeeded();

		if (drawer == null)
			drawer = ((MainActivity) getActivity()).getDrawer();
		mapView.addView(addToggleButton(inflater, container));

		renewRsts();

		return mapView;
	}

	private void renewRsts() {
		fetchNearRsts();
		// デバッグ用マーカー
		Restaurant tst = new Restaurant(1, "testRst", 3, 3, "aaa", 135.763, 35);
		restaurants.put(1, tst);
		addMarkers(false);
		setRestaurantList(false);
	}

	private void setRestaurantList(boolean clear) {
		// ListViewに表示するデータを作成する
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			list.add("hoge" + i);
		}

		ListView listView = (ListView) drawer.findViewById(R.id.rst_list);
		// android.R.layout.simple_list_item_1はAndroidで既に定義されているリストアイテムのレイアウトです
		RstListItemAdapter adapter = (RstListItemAdapter) listView.getAdapter();
		if (adapter == null)
			adapter = new RstListItemAdapter(getActivity());
		else if (clear)
			adapter.clear();
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
						.bearing(0).tilt(0).zoom(16).target(tarM.getPosition());
				mMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder
						.build()));
			}
		});
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
							drawer.openLeftSide();
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
				Restaurant rst = getRestaurant(arg0);
				showDetail(Integer.toString(rst.getRst_id()));
			}
		});

		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
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

	private void addMarkers(boolean clear) {
		if (clear) {
			clear();
		}
		for (Restaurant rst : restaurants.values()) {
			addMarker(rst);
		}
	}

	private void clear() {
		for (Iterator<Entry<Marker, Restaurant>> it = mMarkers.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<Marker, Restaurant> entry = (Map.Entry<Marker, Restaurant>) it
					.next();
			Marker r = entry.getKey();
			if(!r.isInfoWindowShown())
				r.remove();
		}
	}

	private Marker addMarker(Restaurant rst) {
		Marker m = mMap.addMarker(new MarkerOptions()
				.position(new LatLng(rst.getLat(), rst.getLon()))
				.title(rst.getRestaurantName())
				.icon(BitmapDescriptorFactory.defaultMarker()));
		mMarkers.put(m, rst);
		return m;
	}

	private LatLng getLocation() {
		CameraPosition camPos = mMap.getCameraPosition();
		return camPos.target;
	}

	public static MyMapFragment newInstance() {
		MyMapFragment fragment = new MyMapFragment();
		fragment.setRetainInstance(true);
		return fragment;
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
			Restaurant rst = getRestaurant(marker);
			TextView title = ((TextView) view.findViewById(R.id.title));
			title.setText(rst.getRestaurantName());
			RatingBar rate = ((RatingBar) view.findViewById(R.id.rate));
			rate.setRating((float) rst.getDifficulty());
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
		LinearLayout eBtn = (LinearLayout) getActivity().findViewById(
				R.id.eat_btn);
		final Restaurant rst = getRestaurant(marker);
		eBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DistanceCalculator.distace(getMyLocation(), rst) <= 100)
					((MainActivity) getActivity()).showEvalDialog(rst);
				else
					Toast.makeText(getActivity(), "お店から100m以内で評価してください。",
							Toast.LENGTH_LONG).show();

			}
		});
		LinearLayout dBtn = (LinearLayout) getActivity().findViewById(
				R.id.detail_btn);
		dBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDetail(Integer.toString(rst.getRst_id()));
			}
		});
	}

	private void showDetail(String rst_id) {
		// 詳細ページへ
		Intent intent = new Intent(getActivity(), RstDetail.class);
		intent.putExtra("rst_id", rst_id);
		startActivity(intent);
	}

	public Restaurant getRestaurant(Marker m) {
		return mMarkers.get(m);
	}

	// sharedPreferenceを使ってユーザー情報を管理
	String user_id;

	public void checkUserSetting() {
		Log.v("checkUserSetting", "start");
		// preference.WriteKeyValue("key","value");
		final UserSettings preference = new UserSettings(this.getActivity(),
				"botch_user_setting");
		String _user_id = preference.ReadKeyValue("user_id");

		Log.v("checkUserSetting", "user_id:" + _user_id);
		if (_user_id == "") {
			// テキスト入力を受け付けるビューを作成します。
			final EditText editView = new EditText(this.getActivity());
			new AlertDialog.Builder(this.getActivity())
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("名前を入力してください")
					// setViewにてビューを設定します。
					.setView(editView)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 入力した文字をトースト出力する
									String user_name = editView.getText()
											.toString();
									if (user_name.length() < 1)
										user_name = "名無しの権兵衛";
									sendUserName(user_name);
									preference.WriteKeyValue("user_name",
											user_name);
									Toast.makeText(getActivity(),
											"Hello, " + user_name,
											Toast.LENGTH_LONG).show();
								}
							})
					.setNegativeButton("キャンセル",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String user_name = "名無しの権兵衛";
									sendUserName(user_name);
									preference.WriteKeyValue("user_name",
											user_name);
								}
							}).show();
		} else {
			String user_name = preference.ReadKeyValue("user_name");
			Toast.makeText(getActivity(), "Hello, " + user_name,
					Toast.LENGTH_SHORT).show();
		}
		user_id = _user_id;
	}

	private void sendUserName(String user_name) {
		// checkUserSettingで呼び出す。user_nameでcreate_userしてuser_idを与える
		String home = "";
		params.put("name", user_name);
		params.put("home", home);
		url = "/create_user";
		method = Method.POST;
		final UserSettings preference = new UserSettings(this.getActivity(),
				"botch_user_setting");
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, url,
				JsonObject.class, params, new Listener<JsonObject>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(JsonObject response) {
						// success
						String _user_id = response.get("user_id").toString();
						Log.v("success sendUserName:", _user_id);
						preference.WriteKeyValue("user_id", _user_id);
						user_id = _user_id;
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						Log.v("error:", error.toString() + "：通信に失敗しました");
						Toast.makeText(getActivity(), "通信に失敗しました",
								Toast.LENGTH_SHORT).show();
					}
				});
		// requestQueueに上で定義したreqをaddすることで、非同期通信が行われる
		// Queueなので、入れた順番に通信される
		// 通信が終われば、それぞれのreqで定義したコールバック関数が呼ばれる
		req.setTag(TAG_REQUEST_QUEUE);
		requestQueue.add(req);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMap.clear();
		mMarkers.clear();
		restaurants.clear();
	}
}