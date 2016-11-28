package edu.team6.inventory.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import edu.team6.inventory.R;
import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;

/**
 * This class handles the adding of items to the inventory, as well as
 * the validation of inputs for item properties when an item is added.
 */
public class AddItemActivity extends AppCompatActivity {

    /** Constant for adding images to items. */
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    /** An ArrayList of all items in the inventory.. */
    private ArrayList<Item> itemList;
    /** The EditText field for item name input. */
    private EditText mNameField;
    /** The EditText field for item value input. */
    private EditText mValueField;
    /** The EditText field for item condition input. */
    private EditText mConditionField;
    /** The EditText field for item description input. */
    private EditText mDescriptionField;
    /** The button used to add an image to the item. */
    private Button mAddImageButton;
    /** The button used to add an item to the inventory. */
    private Button mAddItemButton;
    /** The ImageView to display item image. */
    private ImageView mImageView;
    /** The Bitmap storing the image. */
    private Bitmap mImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setTitle("Add A New Item");

        // Connecting UI components
        mAddItemButton = (Button) findViewById(R.id.item_add_button);
        mAddImageButton = (Button) findViewById(R.id.item_add_image_button);
        mNameField = (EditText) findViewById(R.id.item_name_field);
        mValueField = (EditText) findViewById(R.id.item_value_field);
        mConditionField = (EditText) findViewById(R.id.item_condition_field);
        mDescriptionField = (EditText) findViewById(R.id.item_description_field);
        mImageView = (ImageView) findViewById(R.id.item_image_view);

        // Creates the database handler
        final SQLiteDBHandler DBhandler = new SQLiteDBHandler(this);
        // Gets the inventory from extras
        itemList = (ArrayList<Item>) getIntent().getSerializableExtra("ItemList");

        // Defining and setting the "Add Item" button's onclick
        mAddItemButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) { // Anonymous OnClickListener class
                if (validateFields()) {
                    // Check value of item, prevent numberformatexception when trying to parsedouble if empty.
                    Double itemValue = (mValueField.getText().toString().equals("")) ? 0 : Double.parseDouble(mValueField.getText().toString());
                    // Create an item to add
                    Item newItem = new Item(mNameField.getText().toString(),
                            itemValue,
                            mConditionField.getText().toString(), mDescriptionField.getText().toString());

                    // Adds the image if available
                    if (mImageBitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageData = baos.toByteArray();
                        newItem.setmImage(imageData);
                    }

                    // Add item to local SQLite DB
                    DBhandler.addItem(newItem);
                    DBhandler.close();

                    // Move back to viewing inventory after adding an item
                    Intent backToViewInventory = new Intent(AddItemActivity.this, InventoryActivity.class);
                    backToViewInventory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(AddItemActivity.this, "Item successfully added! ", Toast.LENGTH_SHORT).show();
                    startActivity(backToViewInventory);
                }
            }
        });

        mAddImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) { // Anon OnClickListener
                dispatchTakePictureIntent();

            }
        });
    }

    /**
     * Start an activity to take a picture to add an image to an item.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mImageBitmap);
        } else {
            // do nothing
        }
    }

    /**
     * Validates all the input and returns the validity.
     * @return True if all given input is valid, false otherwise.
     */
    private boolean validateFields() {
        boolean result = true;
        for (Item item : itemList) {
            if (item.getmName().equals(mNameField.getText().toString())) {
                result = false;
                Toast.makeText(AddItemActivity.this, "There already exists an item with this name!", Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }
}
