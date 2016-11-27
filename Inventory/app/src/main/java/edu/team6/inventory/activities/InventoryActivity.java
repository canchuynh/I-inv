package edu.team6.inventory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.team6.inventory.R;
import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;

/**
 * This activity class displays the users inventory which is obtained from the local
 * SQLite database. From this activity a user able to add a new item as well as view the details
 * of a previously added item.
 */
public class InventoryActivity extends AppCompatActivity {

    /** The SQLite DB handler used to store items in the inventory. */
    private SQLiteDBHandler mDBhandler;
    /** The ListView which displays the inventory. */
    private ListView mItemListView;
    /** The SearchView used to search the inventory. */
    private SearchView mSearchView;
    /** The inventory of items. */
    private List<Item> mInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBhandler = new SQLiteDBHandler(this);
        setContentView(R.layout.activity_inventory);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Connecting UI components
        mSearchView = (SearchView) findViewById(R.id.item_search);
        mSearchView.setOnQueryTextListener(new ItemSearchListener());

        // Create placeholder/test items
        if(getInventoryFromDB().isEmpty()) {
            runTestingCode();
        }

        // cast arraylist to pass to intent extras
        final ArrayList<Item> inventory = (ArrayList) getInventoryFromDB();
        mInventory = inventory;

        // array of string of items
        final List<String> itemList = getInventoryItemNamesFromDB();

        // setting up adapter for listviewu
        //ArrayAdapter<String> itemsAdapter =
        //        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);

        setItemListView(""); // Creates listview of items with all inventory

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItem = new Intent(InventoryActivity.this, AddItemActivity.class);
                // ADDS LIST OF ALL ITEMS TO EXTRAS TO SEND TO ADDITEM ACTIVITY
                addItem.putExtra("ItemList", inventory);
                startActivity(addItem);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        setItemListView("");
    }

    /**
     * Creates a listview adapter for the inventory with items that contain the given search text.
     * @param searchText A string to look for matching items. If empty, include all items.
     */
    private void setItemListView(String searchText) {
        final ArrayList<Item> inventory = (ArrayList) getInventoryFromDB();
        final ArrayList<Item> searchedInventory = new ArrayList<Item>();

        // Simple Adapter Implementation for sub items
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (Item item : inventory) {
            if (item.getmName().toLowerCase().contains(searchText.toLowerCase())) {
                Map<String, String> datum = new HashMap<String, String>(2);
                datum.put("name", item.getmName());
                String description = item.getmDescription();
                if (description.equals("")) {
                    description = "No description.";
                }
                datum.put("description", description);
                data.add(datum);
                searchedInventory.add(item);
            }
        }

        // Creates the simple adapter
        SimpleAdapter itemsAdapter = new SimpleAdapter(this, data,
                R.layout.custom_inventory_list_items,
                new String[] {"name", "description"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        // attach adapter to listview
        mItemListView = (ListView) findViewById(R.id.items_listview);
        mItemListView.setAdapter(itemsAdapter);
        mItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InventoryActivity.this, ItemDetailsActivity.class);
                // ADDS LIST OF ALL ITEMS TO EXTRAS TO SEND TO VIEW ITEM DETAILS
                intent.putExtra("ItemList", inventory);
                // ADDS THE SELECTED ITEM TO EXTRAS
                intent.putExtra("selected", searchedInventory.get(position).getmName());
                startActivity(intent);
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
     * This ItemSearchListener allows the user to be able to search for items by their name.
     */
    private class ItemSearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            setItemListView(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            setItemListView(newText);
            return true;
        }
    }



    /**
     * Creates initial placeholder items
     */
    private void runTestingCode() {

        // Inserting Items/Rows
        Log.d("Insert: ", "Inserting ..");
        mDBhandler.addItem(new Item("Apples", 1.52, "Fresh", "A basket of yummy apples. Restores 12hp when consumed."));
        mDBhandler.addItem(new Item("Excalibur", 1337.00, "Durability 42/420", "The legendary sword belonging to King Arthuria."));
        mDBhandler.addItem(new Item("Galaxy Note 7", 699.99, "Brand New", "Comes with a free explosion after only 15 minutes of use!"));

        // Reading all items
        Log.d("Reading: ", "Reading all items..");
        List<Item> allitems = mDBhandler.getAllItems();

        for (Item item : allitems) {
            String log = "Id: " + item.getmId() + ", Name: " + item.getmName() + ", Value: "
                    + item.getmValue() + ", Condition: " + item.getmCondition() + ", Description: " + item.getmDescription();
            // Writing items to log
            Log.d("Items: : ", log);

        }
    }
}
