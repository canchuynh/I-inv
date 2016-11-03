package me.henrylai.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import me.henrylai.inventory.data.Item;

public class ItemDetailsActivity extends AppCompatActivity {

    private TextView mNameDetail;
    private TextView mValueDetail;
    private TextView mConditionDetail;
    private TextView mDescriptionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        mNameDetail = (TextView)findViewById(R.id.details_item_name);
        mValueDetail = (TextView)findViewById(R.id.details_item_value);
        mConditionDetail = (TextView)findViewById(R.id.details_item_condition);
        mDescriptionDetail = (TextView)findViewById(R.id.details_item_description);


        // GETS THE LIST OF ALL ITEMS
        ArrayList<Item> itemList = (ArrayList<Item>) getIntent().getSerializableExtra("ItemList");
        String selectedItemName = getIntent().getStringExtra("selected");
        Item selectedItem = new Item();
        // Find the selected item from the list of items!
        for (Item item : itemList) {
            if (item.getmName().equals(selectedItemName)) {
                // found, assign the item as selectedItem.
                selectedItem = item;
            }
        }

        // populate UI with details about selectedItem.
        mNameDetail.setText(        "Name: " + selectedItem.getmName());
        mValueDetail.setText(       "Value: " + selectedItem.getmValue() + "");
        mConditionDetail.setText(   "Condition: " + selectedItem.getmCondition());
        mDescriptionDetail.setText( "Description: " + selectedItem.getmDescription());
    }

    public void deleteItem(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

