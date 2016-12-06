/////////////////////////////////////////////////////////////////////////
// NOTE: THESE TESTS MUST BE RUN ON AN EMULATOR OR DEVICE              //
// THIS IS BECAUSE THESE UNIT TESTS REQUIRE THE APP CONTEXT FOR SQLite //
/////////////////////////////////////////////////////////////////////////

package edu.team6.inventory;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class SQLiteUnitTest {

    /** The tolerance threshold for testing doubles. */
    private static final double THRESHOLD = 0.000000001;

    private SQLiteDBHandler database;

    @Before
    public void setUp() {
        database = new SQLiteDBHandler(getTargetContext());
        database.deleteAllItems();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void test1AddName() {
        Item item = new Item(1,"test1", 2.0, "Condition", "testDescription");
        database.addItem(item);
        int ID = getItemKey(item);
        assertEquals(database.getItem(ID).getmName(), "test1");
    }

    @Test
    public void test2AddNameValue() {
        Item item = new Item(2,"test2", 2.0, "Condition", "testDescription");
        database.addItem(item);
        int ID = getItemKey(item);
        assertEquals(database.getItem(ID).getmName(), "test2");
        assertEquals(database.getItem(ID).getmValue(), 2.0, THRESHOLD);
    }

    @Test
    public void test3AddNameValueCondition() {
        Item item = new Item(3, "test3", 2.0, "Brand New", "testDescription");
        database.addItem(item);
        int ID = getItemKey(item);
        assertEquals("test3", database.getItem(ID).getmName());
        assertEquals(database.getItem(ID).getmValue(), 2.0, THRESHOLD);
        assertEquals("Brand New", database.getItem(ID).getmCondition());
    }

    @Test
    public void test4AddNameValueConditionDescription() {
        Item item = new Item(4, "test4", 44, "New", "testDescription4");
        database.addItem(item);
        int ID = getItemKey(item);
        assertEquals("test4", database.getItem(ID).getmName());
        assertEquals(database.getItem(ID).getmValue(), 44, THRESHOLD);
        assertEquals("New", database.getItem(ID).getmCondition());
        assertEquals("testDescription4", database.getItem(ID).getmDescription());
    }

    @Test
    public void test5UpdateName() throws  Exception {
        Item item = new Item(5,"test5", 2.0, "Condition", "testDescription");
        database.addItem(item);
        int ID = getItemKey(item);
        database.updateItem(new Item(ID,"test5UPDATED", 2.0, "Condition_updated", "testDescription"));
        assertEquals(database.getItem(ID).getmName(), "test5UPDATED");
    }

    @Test
    public void test6UpdateValue() throws  Exception {
        Item item = new Item(6,"test6", 2.0, "Condition", "testDescription");
        database.addItem(item);
        int ID = getItemKey(item);
        database.updateItem(new Item(ID,"nameUpdate", 666, "Condition_updated", "testDescription"));
        assertEquals(database.getItem(ID).getmValue(), 666, THRESHOLD);
    }

    @Test
    public void test7UpdateCondition() throws  Exception {
        Item item = new Item(7,"test7", 2.0, "Brand New", "testDescription");
        database.addItem(item);
        int ID = getItemKey(item);
        database.updateItem(new Item(ID, "test7nameUpdate", 666, "VERY OLD", "testDescription"));
        assertEquals("VERY OLD", database.getItem(ID).getmCondition());
    }

    @Test
    public void test8UpdateDescription() throws  Exception {
        Item item = new Item(8,"test8", 2.0, "Brand New", "OLD DESCRIPTION");
        database.addItem(item);
        int ID = getItemKey(item);
        database.updateItem(new Item(ID, "test8nameUpdate", 666, "VERY OLD", "UPDATED DESCRIPTION"));
        assertEquals("UPDATED DESCRIPTION", database.getItem(ID).getmDescription());
    }

    @Test
    public void test9Delete() throws Exception {
        Item item = new Item(99,"testDeletion9", 2.0, "Condition", "testDescription");
        // Get current (old) count of items
        int base = database.getItemCount();
        // Add an item
        database.addItem(item);
        // Updates the items id.
        int ID = getItemKey(item);
        item.setmId(ID);
        // Delete item.
        database.deleteItem(item);
        // Check that Item count is the same.
        assertEquals(base, database.getItemCount());
    }

    // TEST HELPER: Gets the item key for an item.
    private int getItemKey(Item item) {
        int id = -1;
        for (Item otherItem : database.getAllItems("")) {
            if (otherItem.getmName().equals(item.getmName())) {
                id = otherItem.getmId();
            }
        }
        return id;
    }
}