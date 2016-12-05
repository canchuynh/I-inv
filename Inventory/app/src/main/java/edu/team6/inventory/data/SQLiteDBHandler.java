package edu.team6.inventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.team6.inventory.activities.R;

/**
 * This class handles all the SQLite actions performed by the Inventory application.
 * This handler provides the ability to add, view, edit, and delete items to/from the inventory.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper {

    /** The database version. */
    private static final int DATABASE_VERSION = 13;
    /** The database name. */
    private static final String DATABASE_NAME = "Inventory";
    /** The name of the table to store items. */
    private static final String TABLE_ITEMS = "items";

    // BEGIN COLUMN NAME DEFINITIONS FOR THE TABLE items
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String CONDITION = "condition";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    // END OF COLUMN NAME DEFINITIONS

    /**
     * Creates a SQLiteDBHandler.
     * @param context The context where this handler is ccreated.
     */
    public SQLiteDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT NOT NULL UNIQUE,"
                + VALUE + " REAL,"
                + CONDITION + " TEXT,"
                + DESCRIPTION + " TEXT,"
                + IMAGE + " BLOB"
                + ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        // Creating tables again
        onCreate(db);
    }

    /**
     * Adds a given item to the inventory.
     * @param item The item to add.
     */
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, item.getmName()); // Item Name
        values.put(VALUE, item.getmValue()); // Item Value
        values.put(CONDITION, item.getmCondition()); // Item Value
        values.put(DESCRIPTION, item.getmDescription()); // Item Value
        values.put(IMAGE, item.getmImage()); // Item Image Blob

        // Inserting Row
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Gets an item from the inventory with a given id.
     * @param id The id of the item to get.
     * @return Returns the item with the given id.
     */
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS, new String[]{KEY_ID,
                NAME, VALUE, CONDITION, DESCRIPTION, IMAGE}, KEY_ID + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Double.parseDouble(cursor.getString(2)), cursor.getString(3), cursor.getString(4));
        item.setmImage(cursor.getBlob(5));
        // return item
        return item;
    }

    /**
     * Gets the count of how many items in the inventory.
     * @return An integer representing the amount of items in the inventory.
     */
    public int getItemCount() {
        String countQuery = "SELECT * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    /**
     * Deletes a given item from the inventory.
     * @param deletedItem The item to be deleted.
     */
    public void deleteItem(Item deletedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?",
                new String[] { String.valueOf(deletedItem.getmId()) });
        db.close();
    }

    /**
     * Deletes all Items from the inventory.
     */
    public void deleteAllItems() {
        List<Item> inventory = getAllItems("None");
        for(Item i : inventory) {
            deleteItem(i);
        }
    }

    /**
     * Updates an item that is already in the inventory with given new information.
     * @param updatedItem The item to be updated.
     * @return The number of rows updated, 1 if successful.
     */
    public int updateItem(Item updatedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, updatedItem.getmName());
        values.put(VALUE, updatedItem.getmValue());
        values.put(CONDITION, updatedItem.getmCondition());
        values.put(DESCRIPTION, updatedItem.getmDescription());
        values.put(IMAGE, updatedItem.getmImage());
        // updating row
        return db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(updatedItem.getmId())});
    }

    /**
     * Gets a list of all items from the inventory.
     * @param sortMethod The method to sort by.
     * @return A list of Item objects, each Item object representing one item in the inventory.
     */
    public List<Item> getAllItems(String sortMethod) {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;
        if (sortMethod.equals("Name (A - Z)")) {
            selectQuery += " ORDER BY " + NAME + " COLLATE NOCASE ASC";
        } else if (sortMethod.equals("Name (Z - A)")) {
            selectQuery += " ORDER BY " + NAME + " COLLATE NOCASE DESC";
        } else if (sortMethod.equals("Value (Low - High)")) {
            selectQuery += " ORDER BY " + VALUE + " Asc";
        } else if (sortMethod.equals("Value (High - Low)")) {
            selectQuery += " ORDER BY " + VALUE + " DESC";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setmId(Integer.parseInt(cursor.getString(0)));
                item.setmName(cursor.getString(1));
                item.setmValue(Double.parseDouble(cursor.getString(2)));
                item.setmCondition(cursor.getString(3));
                item.setmDescription(cursor.getString(4));
                item.setmImage(cursor.getBlob(5));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        // return item list
        return itemList;
    }

}