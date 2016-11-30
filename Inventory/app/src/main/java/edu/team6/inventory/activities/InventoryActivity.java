package edu.team6.inventory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;
import edu.team6.inventory.utils.GMailSender;
import edu.team6.inventory.fragments.ShareFragment;
/**
 * This activity class displays the users inventory which is obtained from the local
 * SQLite database. From this activity a user able to add a new item as well as view the details
 * of a previously added item.
 */
public class InventoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ShareFragment.OnCompleteListener {

    /** The SQLite DB handler used to store items in the inventory. */
    private SQLiteDBHandler mDBhandler;
    /** The ListView which displays the inventory. */
    private ListView mItemListView;
    /** The SearchView used to search the inventory. */
    private SearchView mSearchView;
    /** The inventory of items. */
    private List<Item> mInventory;
    /** The current sorting method. */
    private String mSort = "None";
    /** The current search text. */
    private String mSearchText = "";
    /** The master email. */
    private String baseEmail = "uwtsealteam6@gmail.com";
    private String pw = "letsandroid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBhandler = new SQLiteDBHandler(this);
        setContentView(R.layout.activity_view_items);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Connecting UI components
        mSearchView = (SearchView) findViewById(R.id.item_search);
        mSearchView.setOnQueryTextListener(new ItemSearchListener());

        // Create placeholder/test items
        if(getInventoryFromDB(mSort).isEmpty()) {
            runTestingCode();
        }

        // cast arraylist to pass to intent extras
        final ArrayList<Item> inventory = (ArrayList) getInventoryFromDB(mSort);
        mInventory = inventory;

        // array of string of items
        final List<String> itemList = getInventoryItemNamesFromDB();
        setItemListView(mSearchText, mSort); // Creates listview of items with all inventory

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

        FloatingActionButton share = (FloatingActionButton) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new ShareFragment();
                dialogFragment.show(getSupportFragmentManager(), "Dialog");
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.sort_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorting_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setItemListView(mSearchText, mSort);
    }

    /**
     * Creates a listview adapter for the inventory with items that contain the given search text.
     * @param searchText A string to look for matching items. If empty, include all items.
     */
    private void setItemListView(String searchText, String sortMethod) {
        final ArrayList<Item> inventory = (ArrayList) getInventoryFromDB(sortMethod);
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
        List<Item> allitems = mDBhandler.getAllItems(mSort);
        for (Item item : allitems) {
            inventory.add(item.getmName());
        }
        return inventory;
    }

    /**
     * Gets a list of all items in the inventory.
     * @return A list of Item objects of every item in the inventory.
     */
    private List<Item> getInventoryFromDB(String sortMethod){
        List<Item> inventory = mDBhandler.getAllItems(sortMethod);
        return inventory;
    }

    /**
     * This ItemSearchListener allows the user to be able to search for items by their name.
     */
    private class ItemSearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            setItemListView(query, mSort);
            mSearchText = query;
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            setItemListView(newText, mSort);
            mSearchText = newText;
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        mSort = parent.getItemAtPosition(pos).toString();
        setItemListView(mSearchText, mSort);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
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
        List<Item> allitems = mDBhandler.getAllItems(mSort);

        for (Item item : allitems) {
            String log = "Id: " + item.getmId() + ", Name: " + item.getmName() + ", Value: "
                    + item.getmValue() + ", Condition: " + item.getmCondition() + ", Description: " + item.getmDescription();
            // Writing items to log
            Log.d("Items: : ", log);

        }
    }

    public void onComplete(String email) {
        try {
            ArrayList<String> names = (ArrayList) getInventoryItemNamesFromDB();
            String body = "";
            for (String s:names) {
                body += s;
                body += "\n";
            }
            GMailSender sender = new GMailSender(baseEmail, pw);
            sender.sendMail("I - inv Notification",
                    body,
                    baseEmail,
                    email);
            Toast.makeText(getBaseContext(),"Inventory shared!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }

    }

}
