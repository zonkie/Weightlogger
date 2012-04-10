package de.ardunoid.weightlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	int id = 1;
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_VALUE = "value";
	public static final String KEY_BODYFAT = "bodyfat";
	public static final String KEY_BODYBONE = "bodybonemass";
	public static final String KEY_BODYWATER = "bodywater";
	
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "weightlogger";
	private static final String DATABASE_TABLE = "weight";
	private static final int DATABASE_VERSION = 4;
	

	private static final String DATABASE_CREATE = "CREATE TABLE "
		+ DATABASE_TABLE + "(" + KEY_ROWID + " INTEGER PRIMARY KEY ASC,"
		+ KEY_VALUE + " TEXT NOT NULL,"
		+ KEY_DATE + " TEXT NOT NULL,"
		+ KEY_BODYWATER + " TEXT NOT NULL,"
		+ KEY_BODYBONE + " TEXT NOT NULL,"
		+ KEY_BODYFAT + " TEXT NOT NULL" + ")";
	
	private static final String DATABASE_UPDATE_ONE_TO_TWO = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + KEY_BODYFAT + " TEXT" + "";
	private static final String DATABASE_UPDATE_TO_FOUR_pt1 = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + KEY_BODYWATER + " TEXT";
	private static final String DATABASE_UPDATE_TO_FOUR_pt2 = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + KEY_BODYBONE +" TEXT";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Creating Database");
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log.d(TAG, "Upgrading database from version " + oldVersion +
			// " to " + newVersion + ", which will destroy all old data");
			// db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			// onCreate(db);
			if(oldVersion <= 1){
				Log.d("test","Updating DB from version 1 to version 2");
				db.execSQL(DATABASE_UPDATE_ONE_TO_TWO);
				oldVersion=2;
			}
			if(oldVersion<=3){
				Log.d("test","Updating DB to version 4");
				db.execSQL(DATABASE_UPDATE_TO_FOUR_pt1);
				db.execSQL(DATABASE_UPDATE_TO_FOUR_pt2);
			}
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert an Entry into the database---
	public long insertWeight(String Date, String Value, String Bodyfat, String Bodywater, String Bodybone) {
		long retval;
		try {
			Log.d("Soenke", "insertWeight");
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_DATE, "" + Date + "");
			initialValues.put(KEY_VALUE, "" + Value + "");
			initialValues.put(KEY_BODYFAT, "" + Bodyfat + "");
			initialValues.put(KEY_BODYWATER, "" + Bodywater + "");
			initialValues.put(KEY_BODYBONE, "" + Bodybone + "");
			Log.d("Soenke", "Insert: " + Date + " - " + Value);
			retval = db.insert(DATABASE_TABLE, null, initialValues);
		} catch (Exception e) {
			retval = 0;
		}
		return retval;
	}

	public long updateWeight(String RowId, String Date, String Value, String Bodyfat, String Bodywater, String Bodybone) {
		long retval;
		try {
			Log.d("Soenke", "updateExpense");
			ContentValues updateValues = new ContentValues();
			updateValues.put(KEY_DATE, "" + Date + "");
			updateValues.put(KEY_VALUE, "" + Value + "");
			updateValues.put(KEY_BODYFAT, "" + Bodyfat + "");
			updateValues.put(KEY_BODYWATER, "" + Bodywater + "");
			updateValues.put(KEY_BODYBONE, "" + Bodybone + "");
			Log.d("Soenke", "Update: " + Date + " - " + Value);
			retval = db.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
					+ Integer.parseInt(RowId), null);
		} catch (Exception e) {
			retval = 0;
		}
		return retval;
	}

	public long deleteWeight(String RowId) {
		long retval;
		try {
			Log.d("Soenke", "updateExpense");
			Log.d("Soenke", "Delete: " + Integer.parseInt(RowId));
			retval = db.delete(DATABASE_TABLE,
					KEY_ROWID + "=" + Integer.parseInt(RowId), null);
		} catch (Exception e) {
			retval = 0;
		}
		return retval;
	}

	public int getEntryCount() {
		Log.d("test", "Reading Count");
		Cursor cursor = db.rawQuery("SELECT COUNT(" + KEY_ROWID + ") FROM "
				+ DATABASE_TABLE, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return cursor.getInt(0);

	}

	public int getMaxId() {
		Log.d("test", "Reading Maxid");
		Cursor cursor = db.rawQuery("SELECT MAX(" + KEY_ROWID + ") FROM "
				+ DATABASE_TABLE, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return cursor.getInt(0);

	}
	
	

	public Cursor getWeightValues() {
		Log.d("test", "Reading Data");
		return db.rawQuery("SELECT " + KEY_ROWID + "," + KEY_VALUE + " AS " + KEY_VALUE + " FROM " + DATABASE_TABLE, null);
	}
	public Cursor getBodyfatValues() {
		Log.d("test", "Reading Data");
		return db.rawQuery("SELECT " + KEY_ROWID + "," + KEY_BODYFAT + " AS " + KEY_BODYFAT + " FROM " + DATABASE_TABLE, null);
	}
	public Cursor getBodywaterValues() {
		Log.d("test", "Reading Data");
		return db.rawQuery("SELECT " + KEY_ROWID + "," + KEY_BODYWATER + " AS " + KEY_BODYWATER + " FROM " + DATABASE_TABLE, null);
	}
	public Cursor getBodybonemassValues() {
		Log.d("test", "Reading Data");
		return db.rawQuery("SELECT " + KEY_ROWID + "," + KEY_BODYBONE + " AS " + KEY_BODYBONE + " FROM " + DATABASE_TABLE, null);
	}
	
	
	
	
	
	
	public Cursor getList() {
		Log.d("test", "Reading Data");
		return db.rawQuery("SELECT " + KEY_ROWID + ","
				+ KEY_VALUE
				+ "|| '|' || 'Fat:' || " + KEY_BODYFAT
				+ "|| '|' || 'Water:' || " + KEY_BODYWATER
				+ "|| '|' || 'Bone:' || " + KEY_BODYBONE
				+ " AS " + KEY_VALUE + " FROM " + DATABASE_TABLE, null);
	}

	public Cursor fetchOne(long rowId) throws SQLException {

		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_VALUE }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
		return mCursor;
	}

}