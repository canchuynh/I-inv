package edu.team6.inventory.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import edu.team6.inventory.R;
import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;

public class EditItemActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mValueField;
    private EditText mConditionField;
    private EditText mDescriptionField;
    private Button mSaveEditsButton;
    private SQLiteDBHandler mDBhandler;
    private Item theEditItem;
    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Grabs list of items from bundle
        itemList = (ArrayList<Item>) getIntent().getSerializableExtra("ItemList");

        // Connects all UI elements
        mNameField = (EditText) findViewById(R.id.edit_name_field);
        mValueField = (EditText) findViewById(R.id.edit_value_field);
        mConditionField = (EditText) findViewById(R.id.edit_condition_field);
        mDescriptionField = (EditText) findViewById(R.id.edit_description_field);
        mSaveEditsButton = (Button) findViewById(R.id.save_edits_button);

        // Creates the database handler and grabs the item being edited
        mDBhandler = new SQLiteDBHandler(this);
        theEditItem = (Item) getIntent().getSerializableExtra("selectedItem");

        // Prefill UI fields with the item details
        mNameField.setText(theEditItem.getmName());
        mValueField.setText(theEditItem.getmValue() + "");
        mConditionField.setText(theEditItem.getmCondition());
        mDescriptionField.setText(theEditItem.getmDescription());

        // Sets the save button's onclick
        mSaveEditsButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (validateInput()) { // Call input validation method here later

                    // sets the fields to be updated for the item being edited
                    updateItem();

                    // Updates the SQLite DB with the updated item
                    mDBhandler.updateItem(theEditItem);
                    mDBhandler.close();

                    // Go back to viewing inventory
                    Intent backToViewInventory = new Intent(EditItemActivity.this, ViewItemsActivity.class);
                    backToViewInventory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(EditItemActivity.this, "Item saved successfully! ", Toast.LENGTH_SHORT).show();
                    startActivity(backToViewInventory);
                }
            }
        });
    }

    private void updateItem() {
        theEditItem.setmName(mNameField.getText().toString());
        theEditItem.setmValue(
                (mValueField.getText().toString().equals("")) ? 0 : Double.parseDouble(mValueField.getText().toString()));
        theEditItem.setmCondition(mConditionField.getText().toString());
        theEditItem.setmDescription(mDescriptionField.getText().toString());
    }

    private boolean validateInput() {
        boolean validity = true;
        // Check for duplicate item names in inventory
        for (Item item : itemList) {
            if (item.getmName().equals(mNameField.getText().toString()) && !item.getmName().equals(theEditItem.getmName())) {
                validity = false;
                Toast.makeText(EditItemActivity.this, "There already exists an item with this name!", Toast.LENGTH_SHORT).show();
            }
        }
        return validity;
    }
}
