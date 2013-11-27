package com.example.mapdemo;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	static final String DATABASE_NAME = "mytitle.db";
	static final int DATABASE_VERSION = 6;
	public static final String TABLE_NAME = "titles";
	public static final String COL_ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_RANK = "rank";
	public static final String COL_CONDITION = "condition";
	public static final String COL_IMG = "img";
	public static final String COL_LASTUPDATE = "lastupdate";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("a", "create!!!");
		db.execSQL(
				"CREATE TABLE " + TABLE_NAME + " ("
						+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
						+ COL_TITLE + " TEXT NOT NULL,"
						+ COL_RANK + " TEXT NOT NULL,"
						+ COL_CONDITION + " TEXT NOT NULL,"
						+ COL_IMG+ " TEXT NOT NULL,"
						+ COL_LASTUPDATE + " TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(
			SQLiteDatabase db,
			int oldVersion,
			int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		Title title = new Title(1,"孤高のぼっち","プラチナ","ほげほげする");
		insertTitle(db,title);
		Title title2 = new Title(2,"至高のぼっち","プラチナ","ふがふがする");
		//insertTitle(db,title2);
	}

	public long insertTitle(SQLiteDatabase db, Title title){
		Date dateNow = new Date ();
		ContentValues values = new ContentValues();
		values.put(COL_TITLE, title.getTitleName());
		values.put(COL_RANK, title.getRank());
		values.put(COL_CONDITION, title.getCondition());
		values.put(COL_IMG, title.getImgStr());
		values.put(COL_LASTUPDATE, dateNow.toString());

		long rowId = db.insert(TABLE_NAME, null, values);
		Log.v("insert", Long.toString(rowId));
		return rowId;
	}
}