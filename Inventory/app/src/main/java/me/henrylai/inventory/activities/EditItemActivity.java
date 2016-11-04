package me.henrylai.inventory.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.henrylai.inventory.R;
import me.henrylai.inventory.data.Item;
import me.henrylai.inventory.data.SQLiteDBHandler;

public class EditItemActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mValueField;
    private EditText mConditionField;
    private EditText mDescriptionField;
    private Button mSaveEditsButton;
    private SQLiteDBHandler mDBhandler;
    private Item theEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

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
                if (true) { // Call input validation method here later

                    // sets the fields to be updated for the item being edited
                    theEditItem.setmName(mNameField.getText().toString());
                    theEditItem.setmValue(Double.parseDouble(mValueField.getText().toString()));
                    theEditItem.setmCondition(mConditionField.getText().toString());
                    theEditItem.setmDescription(mDescriptionField.getText().toString());

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
}
