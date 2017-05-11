package nl.gemeente.breda.bredaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.domain.Report;
import nl.gemeente.breda.bredaapp.domain.User;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "InfraDBHandler";

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "inframeld.db";

    private static final String USERS_TABLE_NAME = "users";

    private static final String USERS_COLUMN_MAILACCOUNT = "mailAccount";

    private static final String REPORTS_TABLE_NAME = "reports";

    private static final String REPORTS_COLUMN_ID = "_id";

    public DatabaseHandler(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + USERS_TABLE_NAME +
                "(" +
                USERS_COLUMN_MAILACCOUNT + " TEXT PRIMARY KEY" +
                ")";

        String CREATE_REPORTS_TABLE = "CREATE TABLE " + REPORTS_TABLE_NAME +
                "(" +
                REPORTS_COLUMN_ID + " INTEGER PRIMARY KEY" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_REPORTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REPORTS_TABLE_NAME);
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

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(REPORTS_TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList getAllReportIDs() {
        String query = "SELECT * FROM " + REPORTS_TABLE_NAME;
        ArrayList<Report> reports = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            Report report = new Report();

            report.setServiceRequestId(cursor.getString(cursor.getColumnIndex(REPORTS_COLUMN_ID)));

            reports.add(report);
        }

        db.close();
        return reports;
    }
}