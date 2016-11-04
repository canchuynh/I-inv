package me.henrylai.inventory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.henrylai.inventory.R;
import me.henrylai.inventory.data.Item;
import me.henrylai.inventory.data.SQLiteDBHandler;

public class ViewItemsActivity extends AppCompatActivity {
    private SQLiteDBHandler mDBhandler;
    private ListView mItemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBhandler = new SQLiteDBHandler(this);
        setContentView(R.layout.activity_view_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // cast arraylist to pass to intent extras
        final ArrayList<Item> inventory = (ArrayList) getInventoryFromDB(mDBhandler);
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

        //TODO DELETE THIS AND THE METHOD BEFORE ANY SUBMISSIONS
        //runTestingCode();
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
//    private void runTestingCode() {
//        SQLiteDBHandler db = new SQLiteDBHandler(this);
//
//        // Inserting Items/Rows
//        Log.d("Insert: ", "Inserting ..");
//        db.addItem(new Item("Dockers", 3.52,null,null));
//        db.addItem(new Item("Dunkin Donuts", 4.51, "good condition", "a healthy donut description"));
//        db.addItem(new Item("Pizza Porlar",5.5,null,null));
//        db.addItem(new Item("Computer",7.8,null,null));
//
//        // Reading all items
//        Log.d("Reading: ", "Reading all items..");
//        List<Item> allitems = db.getAllItems();
//
//        for (Item item : allitems) {
//            String log = "Id: " + item.getmId() + ", Name: " + item.getmName() + ", Value: "
//                    + item.getmValue() + ", Condition: " + item.getmCondition() + ", Description: " + item.getmDescription();
//            // Writing items to log
//            Log.d("Items: : ", log);
//
//        }
//    }
}
