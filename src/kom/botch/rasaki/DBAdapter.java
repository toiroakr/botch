package kom.botch.rasaki;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		// Log.v("tag", "context");
		dbHelper = new DatabaseHelper(this.context);
		// Log.v("tag", "dbhelper");
		saveTitle("a", "b", "c");
		// Log.v("tag", "savetitle");
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
			// Log.v("a", "create!!!");
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

	public boolean deleteTitle(int id){
		return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
	}

	public Cursor getAllTitles(){
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}

	public void saveTitle(String title,String rank,String condition){
		Date dateNow = new Date ();
		ContentValues values = new ContentValues();
		values.put(COL_TITLE, title);
		values.put(COL_RANK, rank);
		values.put(COL_CONDITION, condition);
		values.put(COL_LASTUPDATE, dateNow.toString());
		// Log.v("tag", "tostringisok");
		db.insert(TABLE_NAME, null, values);

		// Log.v("tag", "in");
	}
	public void saveTitle(Title title){
		Date dateNow = new Date ();
		ContentValues values = new ContentValues();
		values.put(COL_TITLE, title.getTitleName());
		values.put(COL_RANK, title.getRank());
		values.put(COL_CONDITION, title.getCondition());
		values.put(COL_LASTUPDATE, dateNow.toString());
		db.insertOrThrow(TABLE_NAME, null, values);
	}
}