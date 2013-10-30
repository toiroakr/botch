package com.example.mapdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RestaurantFragment extends Fragment {

	ListView listView;
	static List<Restaurant> dataList = new ArrayList<Restaurant>();
	static RestaurantAdapter adapter;

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View a = inflater.inflate(R.layout.restaurant_view, container, false);
		findViews(a);
		setAdapters();

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
		dataList.add(
				new Restaurant("天下一品ラーメン", 4.5, "ラーメン"));
		adapter.notifyDataSetChanged();
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
				ratingBar.setNumStars(5);
				ratingBar.setRating((float) restaurant.getDifficalty());
			}
			return v;
		}

	}

}
