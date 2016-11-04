package me.henrylai.inventory.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import me.henrylai.inventory.R;
import me.henrylai.inventory.data.Item;

public class EditItemActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mValueField;
    private EditText mConditionField;
    private EditText mDescriptionField;
    private Button mSaveEditsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        mNameField = (EditText) findViewById(R.id.edit_name_field);
        mValueField = (EditText) findViewById(R.id.edit_value_field);
        mConditionField = (EditText) findViewById(R.id.edit_condition_field);
        mDescriptionField = (EditText) findViewById(R.id.edit_description_field);
        mSaveEditsButton = (Button) findViewById(R.id.save_edits_button);
        Item theEditItem = (Item) getIntent().getSerializableExtra("selectedItem");

        mNameField.setText(theEditItem.getmName());
        mValueField.setText(theEditItem.getmValue() + "");
        mConditionField.setText(theEditItem.getmCondition());
        mDescriptionField.setText(theEditItem.getmDescription());
    }
}
