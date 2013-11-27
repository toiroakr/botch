package com.example.mapdemo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;

public class TitleFragment extends Fragment {

	GridView gridView;
	//static DBAdapter dbAdapter;
	static List<Title> dataList = new ArrayList<Title>();
	static TitleAdapter adapter;
	static SQLiteDatabase db;
	String TAG = "tag";
	public static final String TABLE_NAME = "titles";
	public static final String COL_ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_RANK = "rank";
	public static final String COL_CONDITION = "condition";
	public static final String COL_LASTUPDATE = "lastupdate";
	private String acquired_titles;
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View a = inflater.inflate(R.layout.title_view, container, false);
		findViews(a);

		setAdapters();
		Log.v("tag", "start");
		DBHelper helper = new DBHelper(getActivity());
		try{
			helper.createEmptyDataBase();
			db = helper.openDataBase();
		}catch (IOException ioe) {
	        throw new Error("Unable to create database");
	    } catch(SQLException sqle){
	        throw sqle;
	    }

	    //db = helper.getReadableDatabase();
		//Title title = new Title(1,"ぼっち","プラチナ","ほげほげする","rank2");
		//helper.insertTitle(db,title);
		//dbAdapter = new DBAdapter(getActivity());

		loadTitles();
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Title title = adapter.getItem(position);
				View parentView = (View)parent.getParent();
				setTitleDetail(title,parentView);
				redrawGridView();

			}

		});
		return a;
	}

	protected void findViews(View a) {
		gridView = (GridView) a.findViewById(R.id.titleGrid);
		//Log.v("asa",Integer.toString(gridView.getColumnWidth()));;
		//gridView.setColumnWidth(columnWidth);
		//gridView.getColumnWidth();
	}

	protected void setAdapters() {
		/*adapter = new ArrayAdapter<Book>(
		  this,
		  android.R.layout.simple_list_item_1,
		  dataList);*/
		adapter = new TitleAdapter();
		//addItem();
		gridView.setAdapter(adapter);
	}

	protected void addTitle(Title title){
		dataList.add(title);
	}

	protected void addItem() {
		Title title = new Title(1,"孤高のぼっち","プラチナ","ほげほげする","rank3");
		addTitle(title);
		Title title2 = new Title(2,"至高のぼっち","プラチナ","ふがふがする");
		addTitle(title2);
		adapter.notifyDataSetChanged();
	}


	protected void loadTitles(){
		dataList.clear();

		// Read

		Cursor c = db.query("titles", null, null, null, null, null, null);
		boolean isEof = c.moveToFirst();
		while (isEof) {
			Title title = new Title(
					c.getInt(c.getColumnIndex(DBHelper.COL_ID)),
					c.getString(c.getColumnIndex(DBHelper.COL_TITLE)),
					c.getString(c.getColumnIndex(DBHelper.COL_RANK)),
					c.getString(c.getColumnIndex(DBHelper.COL_CONDITION)),
					c.getString(c.getColumnIndex(DBHelper.COL_IMG))
					);
			dataList.add(title);
			isEof = c.moveToNext();
		}
		c.close();

//		dbAdapter.open();
//		Cursor c = dbAdapter.getAllTitles();
//
//		getActivity().startManagingCursor(c);
//
//		if(c.moveToFirst()){
//			do {
//				Title title = new Title(
//						c.getInt(c.getColumnIndex(DBAdapter.COL_ID)),
//						c.getString(c.getColumnIndex(DBAdapter.COL_TITLE)),
//						c.getString(c.getColumnIndex(DBAdapter.COL_RANK)),
//						c.getString(c.getColumnIndex(DBAdapter.COL_CONDITION))
//						);
//				dataList.add(title);
//			} while(c.moveToNext());
//		}
//
//		getActivity().stopManagingCursor(c);
//		dbAdapter.close();
//		adapter.notifyDataSetChanged();
	}

	public void redrawGridView(){
		final UserSettings preference = new UserSettings(getActivity(),	"botch_user_setting");
		try {
			// sharedPreferenceから取得済みの称号を取得する
			acquired_titles = preference.ReadKeyValue("acquired_title");
			//Log.v("取得済みの称号！", acquired_titles);
			JSONObject o = new JSONObject(acquired_titles);
			JSONArray r = o.getJSONArray("acquired_titles");
			for(int i=0;i < r.length();i++){
				Title t = adapter.getItem(r.getInt(i));
				t.setGet(true);
			}
			adapter.notifyDataSetChanged();
			//Log.v("acquired_title => ",r.get(1).toString() + Integer.toString(r.length()));
		} catch (Exception e) {
			Log.v("acquired_title", e.toString());
			acquired_titles = "";
		};
	}
	private void setTitleDetail(Title title, View parentView){
		ImageView imgView = (ImageView)parentView.findViewById(R.id.titleImg);
		TextView titleText = (TextView)parentView.findViewById(R.id.titleText);
		TextView conditionText = (TextView)parentView.findViewById(R.id.conditionText);
		TextView rankText = (TextView)parentView.findViewById(R.id.rankText);
		imgView.setImageResource(getResources().getIdentifier(title.getImgStr(), "drawable", getActivity().getPackageName()));
		titleText.setText(title.getTitleName());
		conditionText.setText(title.getCondition());
		rankText.setText(title.getRank());
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

			Log.v("getview",Integer.toString(position));
			Title title = (Title) getItem(position);
			if (title != null){
				imgView = (ImageView) v.findViewById(R.id.img);
				Log.v("getview",title.getImgStr());
				int resourceId = getResources().getIdentifier(title.getImgStr(), "drawable", getActivity().getPackageName());
				imgView.setImageResource(resourceId);

				if(!title.isGet()){
					imgView.setAlpha(127);
				}
			}

			return v;
		}




	}

}
