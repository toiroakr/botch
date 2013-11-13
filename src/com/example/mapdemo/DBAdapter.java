package com.example.mapdemo;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	static final String DATABASE_NAME = "mynote.db";
	static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "titles";
	public static final String COL_ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_RANK = "rank";
	public static final String COL_CONDITION = "condition";
	public static final String COL_LASTUPDATE = "lastupdate";

	protected final Context context;
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;

	public DBAdapter(Context context){
		this.context = context;
		dbHelper = new DatabaseHelper(this.context);
	}

	//
	// SQLiteOpenHelper
	//

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(
					"CREATE TABLE " + TABLE_NAME + " ("
							+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
							+ COL_TITLE + " TEXT NOT NULL,"
							+ COL_RANK + " TEXT NOT NULL,"
							+ COL_CONDITION + " TEXT NOT NULL,"
							+ COL_LASTUPDATE + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(
				SQLiteDatabase db,
				int oldVersion,
				int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	//
	// Adapter Methods
	//

	public DBAdapter open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}


	public void close(){
		dbHelper.close();
	}


	//
	// App Methods
	//


	public boolean deleteAllTitles(){
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	public boolean deleteNote(int id){
		return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
	}

	public Cursor getAllNotes(){
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}

	public void saveNote(String note){
		Date dateNow = new Date ();
		ContentValues values = new ContentValues();
		values.put(COL_TITLE, note);
		values.put(COL_LASTUPDATE, dateNow.toLocaleString());
		db.insertOrThrow(TABLE_NAME, null, values);
	}
}