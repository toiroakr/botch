package com.example.mapdemo;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class TitleFragment extends Fragment {

	GridView gridView;
	static List<Title> dataList = new ArrayList<Title>();
	static TitleAdapter adapter;
	String TAG = "tag";

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View a = inflater.inflate(R.layout.title_view, container, false);
		findViews(a);
		setAdapters();
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String message = adapter.getItem(position).getTitleName();
               Log.v("create:", message);
            }

		});
		Log.v("create:", "create");
		return a;
	}

	protected void findViews(View a) {
		gridView = (GridView) a.findViewById(R.id.titleGrid);
	}

	protected void setAdapters() {
		/*adapter = new ArrayAdapter<Book>(
		  this,
		  android.R.layout.simple_list_item_1,
		  dataList);*/
		adapter = new TitleAdapter();
		addItem();
		gridView.setAdapter(adapter);
	}

	protected void addItem() {
		dataList.add(
				new Title("孤高のぼっち","プラチナ","ほげほげする"));

		dataList.add(
				new Title("至高のぼっち","プラチナ","ふがふがする"));
		adapter.notifyDataSetChanged();
	}

	private class TitleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Title getItem(int position) {
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

			ImageView imgView;
			View v = convertView;

			if (v == null) {
				LayoutInflater inflater =
						(LayoutInflater)
						getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.box, null);
			}
			Title title = (Title) getItem(position);
			if (title != null) {
				imgView = (ImageView) v.findViewById(R.id.img);
				imgView.setImageResource(title.getImgId());
				if(!title.isGet()){
					imgView.setAlpha(127);
				}
			}
			return v;
		}

	}

}
