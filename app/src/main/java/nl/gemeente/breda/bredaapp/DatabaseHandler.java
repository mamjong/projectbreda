package nl.gemeente.breda.bredaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "InfraDBHandler";

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "inframeld.db";

    private static final String USERS_TABLE_NAME = "users";

    private static final String USERS_COLUMN_MAILACCOUNT = "mailAccount";

    private static final String REPORTS_TABLE_NAME = "reports";

    private static final String REPORTS_COLUMN_ID = "_id";
    private static final String REPORTS_COLUMN_CATEGORY = "category";
    private static final String REPORTS_COLUMN_TYPE = "type";
    private static final String REPORTS_COLUMN_IMAGEURL = "image";

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
                REPORTS_COLUMN_ID + " INTEGER PRIMARY KEY," +
                REPORTS_COLUMN_CATEGORY + " TEXT," +
                REPORTS_COLUMN_TYPE + " TEXT," +
                REPORTS_COLUMN_IMAGEURL + " TEXT," +
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
}
