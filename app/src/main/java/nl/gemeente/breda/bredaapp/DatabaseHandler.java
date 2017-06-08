package nl.gemeente.breda.bredaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.domain.User;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final String TAG = "InfraDBHandler";
	
	private static final int DB_VERSION = 6;
	private static final String DB_NAME = "inframeld.db";
	
	private static final String USERS_TABLE_NAME = "users";
	
	private static final String USERS_COLUMN_MAILACCOUNT = "mailAccount";
	
	private static final String REPORTS_TABLE_NAME = "reports";
	
	private static final String REPORTS_COLUMN_ID = "_id";
	
	private static final String REPORTS_COLUMN_IS_FAVORITE = "isFavorite";
	
	private static final String SETTINGS_TABLE_NAME = "settings";
	
	private static final String SETTINGSCOLUMNTYPE = "_type";
	
	private static final String SETTINGSCOLUMNVALUE = "value";
	
	private static final String CREATETABLE = "CREATE TABLE ";
	
	private static final String DROPTABLEIFEXISTS = "DROP TABLE IF EXISTS";
	
	public DatabaseHandler(Context context, String name,
	                       SQLiteDatabase.CursorFactory factory, int version) {
		super(context, DB_NAME, factory, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createuserstable = CREATETABLE + USERS_TABLE_NAME +
				"(" +
				USERS_COLUMN_MAILACCOUNT + " TEXT PRIMARY KEY" +
				")";
		
		String createreportstable = CREATETABLE + REPORTS_TABLE_NAME +
				"(" +
				REPORTS_COLUMN_ID + " TEXT PRIMARY KEY, " +
				REPORTS_COLUMN_IS_FAVORITE + " INTEGER" +
				")";
		
		String createsettingstable = CREATETABLE + SETTINGS_TABLE_NAME +
				"(" +
				SETTINGSCOLUMNTYPE + " TEXT PRIMARY KEY, " +
				SETTINGSCOLUMNVALUE + " TEXT" +
				")";
		
		db.execSQL(createuserstable);
		db.execSQL(createreportstable);
		db.execSQL(createsettingstable);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROPTABLEIFEXISTS + USERS_TABLE_NAME);
		db.execSQL(DROPTABLEIFEXISTS + REPORTS_TABLE_NAME);
		db.execSQL(DROPTABLEIFEXISTS + SETTINGS_TABLE_NAME);
		onCreate(db);
	}
	
	public void addUser(String mailAccount) {
		ContentValues values = new ContentValues();
		
		values.put(USERS_COLUMN_MAILACCOUNT, mailAccount);
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(USERS_TABLE_NAME, null, values);
		db.close();
	}
	
	public boolean checkUser() {
		boolean flag;
		String query = "SELECT COUNT(*) FROM " + USERS_TABLE_NAME;
		
		SQLiteDatabase db = this.getReadableDatabase();

        /*
        If User table is empty, method returns false. If table has entries,
        method returns true.
        */
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		int rowCount = cursor.getInt(0);
		
		if (rowCount > 0) {
			flag = true;
		} else {
			flag = false;
		}
		
		db.close();
		return flag;
	}
	
	public void addReport(Report report) {
		ContentValues values = new ContentValues();
		
		values.put(REPORTS_COLUMN_ID, report.getServiceRequestId());
		values.put(REPORTS_COLUMN_IS_FAVORITE, report.isFavorite());
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(REPORTS_TABLE_NAME, null, values);
		db.close();
	}
	
	public void deleteReport(Report report) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(REPORTS_TABLE_NAME, "_id= '" + report.getServiceRequestId() + "'", null);
		db.close();
	}
	
	public Boolean checkReport(Report report) {
		String query = "SELECT * FROM " + REPORTS_TABLE_NAME + " WHERE " + REPORTS_COLUMN_ID +
				" = '" + report.getServiceRequestId() + "'";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Boolean reportCheck = false;
		
		if(cursor.moveToFirst()){
			reportCheck = true;
		} else if (!cursor.moveToFirst()) {
			reportCheck = false;
		}
		
		return reportCheck;
	}
	
	public ArrayList getAllReports() {
		String query = "SELECT * FROM " + REPORTS_TABLE_NAME;
		ArrayList<Report> reports = new ArrayList<>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		while (cursor.moveToNext()) {
			Report report = new Report();
			
			report.setServiceRequestId(cursor.getString(cursor.getColumnIndex(REPORTS_COLUMN_ID)));
			report.setServiceName(cursor.getString(cursor.getColumnIndex(REPORTS_COLUMN_IS_FAVORITE)));

			reports.add(report);
		}
		
		db.close();
		return reports;
	}
		
	public User getUser() {
		String query = "SELECT " + USERS_COLUMN_MAILACCOUNT + " FROM " + USERS_TABLE_NAME;
		User user = new User();
		Log.i(TAG, "Query: " + query);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		cursor.moveToFirst();
		user.setMailAccount(cursor.getString(cursor.getColumnIndex(USERS_COLUMN_MAILACCOUNT)));
		
		db.close();
		return user;
	}
	
	public void updateUser(String newMailAccount) {
		ContentValues values = new ContentValues();
		values.put(USERS_COLUMN_MAILACCOUNT, newMailAccount);
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(USERS_TABLE_NAME, values, null, null);
		db.close();
	}
	
	public String getSettingsValue(String key) {
		String query = "SELECT " + SETTINGSCOLUMNVALUE + " FROM " + SETTINGS_TABLE_NAME
				+ " WHERE " + SETTINGSCOLUMNTYPE + "=\"" + key + "\";";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		cursor.moveToFirst();
		
		String result = cursor.getString(cursor.getColumnIndex(SETTINGSCOLUMNVALUE));
		db.close();
		
		return result;
	}
	
	public void updateSettingsValue(String key, String value) {
		ContentValues values = new ContentValues();
		values.put(key, value);
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(SETTINGS_TABLE_NAME, values, SETTINGSCOLUMNTYPE + "=" + key, null);
		db.close();
	}
}