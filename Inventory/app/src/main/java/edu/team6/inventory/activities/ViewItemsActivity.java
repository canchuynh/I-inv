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

public class ViewItemsActivity extends AppCompatActivity {
    private SQLiteDBHandler mDBhandler;
    private ListView mItemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBhandler = new SQLiteDBHandler(this);
        setContentView(R.layout.activity_view_items);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // cast arraylist to pass to intent extras
        final ArrayList<Item> inventory = (ArrayList) getInventoryFromDB(mDBhandler);

        //TODO DELETE THIS AND THE METHOD BEFORE ANY SUBMISSIONS
        if(inventory.isEmpty()) {
            runTestingCode();
        }

        // array of string of items
        final List<String> itemList = getInventoryArrayFromDB(mDBhandler);

        //setting up adapter for listvirw
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

    private List<String> getInventoryArrayFromDB(SQLiteDBHandler DBhandler){
        ArrayList<String> inventory = new ArrayList<>();
        List<Item> allitems = DBhandler.getAllItems();
        for (Item item : allitems) {
            inventory.add(item.getmName());
        }
        return inventory;
    }

    private List<Item> getInventoryFromDB(SQLiteDBHandler DBhandler){
        List<Item> inventory = DBhandler.getAllItems();
        return inventory;
    }

    // TODO: TO BE DELETED
    private void runTestingCode() {
        SQLiteDBHandler db = new SQLiteDBHandler(this);

        // Inserting Items/Rows
        Log.d("Insert: ", "Inserting ..");
        db.addItem(new Item("Apples", 1.52, "Fresh", "A basket of yummy apples. Restores 12hp when consumed."));
        db.addItem(new Item("Excalibur", 1337.00, "Durability 42/420", "The legendary sword belonging to King Arthuria."));
        db.addItem(new Item("Hardcore Corno", 69.69, "Brand New", "The hardest of all cornography."));

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
