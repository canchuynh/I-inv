package me.henrylai.inventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class AddItemActivity extends AppCompatActivity {

    private Button mAddItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mAddItemButton = (Button) findViewById(R.id.add_item_button);
        mAddItemButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Move back to viewing inventory after adding an item
                Intent backToViewInventory = new Intent(AddItemActivity.this, ViewItemsActivity.class);
                backToViewInventory.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(AddItemActivity.this, "Item successfully added! (not!)", Toast.LENGTH_SHORT).show();
                startActivity(backToViewInventory);

            }
        });
    }
}
