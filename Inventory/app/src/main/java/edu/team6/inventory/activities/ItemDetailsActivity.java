package edu.team6.inventory.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.team6.inventory.R;
import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;

/**
 * This activity class displays all details or properties of a given item, including things like
 * name, value, condition, description, etc. as well as a button to enable the editing of these
 * properties and details.
 */
public class ItemDetailsActivity extends AppCompatActivity {

    /** The SQLite DB handler used to store items in the inventory. */
    private SQLiteDBHandler mDBhandler;
    /** The selected Item to display details of. */
    private Item selectedItem;
    /** The TextView to display the item name. */
    private TextView mNameDetail;
    /** The TextView to display the item value. */
    private TextView mValueDetail;
    /** The TextView to display the item condition. */
    private TextView mConditionDetail;
    /** The TextView to display the item's description. */
    private TextView mDescriptionDetail;
    /** The Button to edit this item's properties. */
    private Button mEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        mDBhandler = new SQLiteDBHandler(this);

        // Connecting UI components
        mNameDetail = (TextView)findViewById(R.id.details_item_name);
        mValueDetail = (TextView)findViewById(R.id.details_item_value);
        mConditionDetail = (TextView)findViewById(R.id.details_item_condition);
        mDescriptionDetail = (TextView)findViewById(R.id.details_item_description);
        mEditButton = (Button) findViewById(R.id.details_edit_button);

        // Gets the list of all items
        final ArrayList<Item> itemList = (ArrayList<Item>) getIntent().getSerializableExtra("ItemList");
        String selectedItemName = getIntent().getStringExtra("selected");
        selectedItem = new Item();
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

        // Sets edit button's onclick to move to editing that item.
        mEditButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailsActivity.this, EditItemActivity.class);
                intent.putExtra("ItemList", itemList);
                intent.putExtra("selectedItem", selectedItem);
                startActivity(intent);
            }
        });
    }

    /**
     * Deletes this item from the inventory.
     * @param view
     */
    public void deleteItem(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the item
                        mDBhandler.deleteItem(selectedItem);
                        // Make a toast to provide feedback to user
                        Toast.makeText(ItemDetailsActivity.this, "Item deleted! ", Toast.LENGTH_SHORT).show();
                        // Go back to viewing inventory
                        Intent intent = new Intent(ItemDetailsActivity.this, ViewItemsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

