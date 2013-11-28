package kom.botch.rasaki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.mapdemo.R;
import com.google.gson.JsonObject;

public class RestaurantFragment extends Fragment {

	ListView listView;
	static List<Restaurant> dataList = new ArrayList<Restaurant>();
	static RestaurantAdapter adapter;
	private RequestQueue requestQueue;
	String TAG = "tag";

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View a = inflater.inflate(R.layout.restaurant_view, container, false);
		findViews(a);
		requestQueue = Volley.newRequestQueue(getActivity()
				.getApplicationContext());
		setAdapters();
		Log.v("create:", "create");
		return a;
	}


	protected void findViews(View a) {
		listView = (ListView) a.findViewById(R.id.restaurantList);
	}

	protected void setAdapters() {
		/*adapter = new ArrayAdapter<Book>(
		  this,
		  android.R.layout.simple_list_item_1,
		  dataList);*/
		adapter = new RestaurantAdapter();
		addItem();
		listView.setAdapter(adapter);
	}

	protected void addItem() {
		Random rnd = new Random();
		int ran = rnd.nextInt(100);
		int method = Method.POST;
		String url = "/read_rst";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("rst_id", Integer.toString(ran));
		Log.v("success:", Integer.toString(ran));
		/*
		 * public GsonRequest(int method, String url, Class<T> clazz,
		 * Map<String, String> params, Listener<T> listener, ErrorListener
		 * errorListener) {
		 */
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, url,
				JsonObject.class, params, new Listener<JsonObject>() {
			@Override
			public void onResponse(JsonObject rst) {
				// success
				String result = rst.toString(); // user.get("result").toString();

				Log.v("success:", result);
				dataList.add(
						new Restaurant(1,"天下一品ラーメン", 4.5, 5, "ラーメン"));
				adapter.notifyDataSetChanged();

			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
				Log.v("error:", error.toString());
			}
		});
		Log.v("check:", "check");
		requestQueue.add(req);
	}

	private class RestaurantAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(
				int position,
				View convertView,
				ViewGroup parent) {

			TextView textView1;
			TextView textView2;
			RatingBar ratingBar;
			View v = convertView;

			if (v == null) {
				LayoutInflater inflater =
						(LayoutInflater)
						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.row, null);
			}
			Restaurant restaurant = (Restaurant) getItem(position);
			if (restaurant != null) {
				textView1 = (TextView) v.findViewById(R.id.textView1);
				textView2 = (TextView) v.findViewById(R.id.textView2);
				ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);

				textView1.setText(restaurant.getRestaurantName());
				textView2.setText("---" + restaurant.getCategory());
				ratingBar.setNumStars(3);
				ratingBar.setRating((float) restaurant.getDifficulty());
			}
			return v;
		}

	}

}
