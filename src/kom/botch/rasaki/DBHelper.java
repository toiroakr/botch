package kom.botch.rasaki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	static final String DATABASE_NAME = "mytitle.db";
	private static String DB_NAME_ASSET = "titles.db";
	static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "titles";
	public static final String COL_ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_RANK = "rank";
	public static final String COL_CONDITION = "condition";
	public static final String COL_IMG = "img";
	public static final String COL_ISGET = "isGet";
	public static final String COL_LASTUPDATE = "lastupdate";

	private SQLiteDatabase mDataBase;
	private final Context mContext;
	private final File mDatabasePath;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
        mDatabasePath = mContext.getDatabasePath(DATABASE_NAME);
	}


	 /**
     * asset に格納したデータベースをコピーするための空のデータベースを作成する
     */
    public void createEmptyDataBase() throws IOException {
        boolean dbExist = checkDataBaseExists();

        if (dbExist) {
            // すでにデータベースは作成されている
        } else {
            // このメソッドを呼ぶことで、空のデータベースがアプリのデフォルトシステムパスに作られる
            getReadableDatabase();

            try {
                // asset に格納したデータベースをコピーする
                copyDataBaseFromAsset();

                String dbPath = mDatabasePath.getAbsolutePath();
                SQLiteDatabase checkDb = null;
                try {
                    checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
                } catch (SQLiteException e) {
                }

                if (checkDb != null) {
                    checkDb.setVersion(DATABASE_VERSION);
                    checkDb.close();
                }

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * 再コピーを防止するために、すでにデータベースがあるかどうか判定する
     *
     * @return 存在している場合 {@code true}
     */
    private boolean checkDataBaseExists() {
        String dbPath = mDatabasePath.getAbsolutePath();

        SQLiteDatabase checkDb = null;
        try {
            checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // データベースはまだ存在していない
        }

        if (checkDb == null) {
            // データベースはまだ存在していない
            return false;
        }

        int oldVersion = checkDb.getVersion();
        int newVersion = DATABASE_VERSION;

        if (oldVersion == newVersion) {
            // データベースは存在していて最新
            checkDb.close();
            return true;
        }

        // データベースが存在していて最新ではないので削除
        File f = new File(dbPath);
        f.delete();
        return false;
    }

    /**
     * asset に格納したデーだベースをデフォルトのデータベースパスに作成したからのデータベースにコピーする
     */
    private void copyDataBaseFromAsset() throws IOException{

        // asset 内のデータベースファイルにアクセス
        InputStream mInput = mContext.getAssets().open(DB_NAME_ASSET);

        // デフォルトのデータベースパスに作成した空のDB
        OutputStream mOutput = new FileOutputStream(mDatabasePath);

        // コピー
        byte[] buffer = new byte[1024];
        int size;
        while ((size = mInput.read(buffer)) > 0) {
            mOutput.write(buffer, 0, size);
        }

        // Close the streams
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        return getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public synchronized void close() {
        if(mDataBase != null)
            mDataBase.close();

            super.close();
    }

//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		Log.v("a", "create!!!");
//		db.execSQL(
//				"CREATE TABLE " + TABLE_NAME + " ("
//						+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//						+ COL_TITLE + " TEXT NOT NULL,"
//						+ COL_RANK + " TEXT NOT NULL,"
//						+ COL_CONDITION + " TEXT NOT NULL,"
//						+ COL_IMG+ " TEXT NOT NULL,"
//						+ COL_ISGET+ " TEXT NOT NULL,"
//						+ COL_LASTUPDATE + " TEXT NOT NULL);");
//
//	}
//
//	@Override
//	public void onUpgrade(
//			SQLiteDatabase db,
//			int oldVersion,
//			int newVersion) {
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//		onCreate(db);
//	}

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