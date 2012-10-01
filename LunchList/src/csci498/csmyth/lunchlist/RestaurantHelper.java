package csci498.csmyth.lunchlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class RestaurantHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "lunchlist.db";
	private static final int SCHEMA_VERSION = 1; 
	
	public RestaurantHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE restaurants (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// no-op, since will not be called until 2nd schema version exists
	}
	
	public void insert(String name, String address, String type, String notes) {
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		
		getWritableDatabase().insert("restaurants", "name", cv);
	}

	public Cursor getAll() {
		return (getReadableDatabase().rawQuery(
				"SELECT_id, name, address, types, notes FROM restaurants ORDER BY name", null));
	}
	
	// Note! Column ints in below getters refer to (1) name, (2) address, (3) type, and (4) notes
	public String getName(Cursor c) {
		return (c.getString(1));
	}
	
	public String getAddress(Cursor c) {
		return (c.getString(2));
	}
	
	public String getType(Cursor c) {
		return (c.getString(3));
	}
	
	public String getNotes(Cursor c) {
		return (c.getString(4));
	}
}
