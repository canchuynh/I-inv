package edu.team6.inventory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.team6.inventory.R;
import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;

/**
 * This activity class displays the users inventory which is obtained from the local
 * SQLite database. From this activity a user able to add a new item as well as view the details
 * of a previously added item.
 */
public class ViewItemsActivity extends AppCompatActivity {

    /** The SQLite DB handler used to store items in the inventory. */
    private SQLiteDBHandler mDBhandler;
    /** The ListView which displays the inventory. */
    private ListView mItemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBhandler = new SQLiteDBHandler(this);
        setContentView(R.layout.activity_view_items);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // cast arraylist to pass to intent extras
        final ArrayList<Item> inventory = (ArrayList) getInventoryFromDB();

        // Create placeholder/test items
        if(inventory.isEmpty()) {
            runTestingCode();
        }

        // array of string of items
        final List<String> itemList = getInventoryItemNamesFromDB();

        // setting up adapter for listview
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);

        // attach listview
        mItemListView = (ListView) findViewById(R.id.items_listview);
        mItemListView.setAdapter(itemsAdapter);
        mItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewItemsActivity.this, ItemDetailsActivity.class);
                // ADDS LIST OF ALL ITEMS TO EXTRAS TO SEND TO VIEW ITEM DETAILS
                intent.putExtra("ItemList", inventory);
                // ADDS THE SELECTED ITEM TO EXTRAS
                intent.putExtra("selected", itemList.get(position));
                startActivity(intent);
            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItem = new Intent(ViewItemsActivity.this, AddItemActivity.class);
                // ADDS LIST OF ALL ITEMS TO EXTRAS TO SEND TO ADDITEM ACTIVITY
                addItem.putExtra("ItemList", inventory);
                startActivity(addItem);
            }
        });


    }

    /**
     * Gets a list of the names of all the items in the inventory.
     * @return A list of strings of every item's name in the inventory.
     */
    private List<String> getInventoryItemNamesFromDB(){
        ArrayList<String> inventory = new ArrayList<>();
        List<Item> allitems = mDBhandler.getAllItems();
        for (Item item : allitems) {
            inventory.add(item.getmName());
        }
        return inventory;
    }

    /**
     * Gets a list of all items in the inventory.
     * @return A list of Item objects of every item in the inventory.
     */
    private List<Item> getInventoryFromDB(){
        List<Item> inventory = mDBhandler.getAllItems();
        return inventory;
    }

    /**
     * Creates initial placeholder items
     */
    private void runTestingCode() {
        SQLiteDBHandler db = new SQLiteDBHandler(this);

        // Inserting Items/Rows
        Log.d("Insert: ", "Inserting ..");
        db.addItem(new Item("Apples", 1.52, "Fresh", "A basket of yummy apples. Restores 12hp when consumed."));
        db.addItem(new Item("Excalibur", 1337.00, "Durability 42/420", "The legendary sword belonging to King Arthuria."));
        db.addItem(new Item("Galaxy Note 7", 699.99, "Brand New", "Comes with a free explosion after only 15 minutes of use!"));

        // Reading all items
        Log.d("Reading: ", "Reading all items..");
        List<Item> allitems = db.getAllItems();

        for (Item item : allitems) {
            String log = "Id: " + item.getmId() + ", Name: " + item.getmName() + ", Value: "
                    + item.getmValue() + ", Condition: " + item.getmCondition() + ", Description: " + item.getmDescription();
            // Writing items to log
            Log.d("Items: : ", log);

        }
    }
}
