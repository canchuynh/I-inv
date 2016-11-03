package me.henrylai.inventory.data;

/**
 * Created by Starwater on 11/3/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Inventory";
    // Inventory table name
    private static final String TABLE_ITEMS = "items";
    // Items Table Columns names
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String CONDITION = "condition";
    private static final String DESCRIPTION = "description";

    public SQLiteDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE" + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + NAME + " TEXT,"
                + VALUE + "REAL"
                + CONDITION + "TEXT"
                + DESCRIPTION + "TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ITEMS);
        // Creating tables again
        onCreate(db);
    }
}