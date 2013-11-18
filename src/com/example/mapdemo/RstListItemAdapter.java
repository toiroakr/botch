package com.example.mapdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class RstListItemAdapter extends ArrayAdapter<Restaurant> {

	private LayoutInflater mLayoutInflater;

	public RstListItemAdapter(Context context, List<Restaurant> objects) {
		super(context, 0, objects);
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		if (convertView == null) {
			// レイアウトファイルからViewを生成する
			view = mLayoutInflater.inflate(R.layout.side_list_content, parent,
					false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			// レイアウトが存在する場合は再利用する
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		// リストアイテムに対応するデータを取得する
		Restaurant item = getItem(position);

		// 各Viewに表示する情報を設定
		holder.title.setText(item.getRestaurantName());
		holder.budget.setText("予算：" + item.getDifficalty());
		 holder.rate.setRating(position / 6);
		return view;
	}
}