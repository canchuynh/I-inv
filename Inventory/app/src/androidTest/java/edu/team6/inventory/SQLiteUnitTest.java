/////////////////////////////////////////////////////////////////////////
// NOTE: THESE TESTS MUST BE RUN ON AN EMULATOR OR DEVICE              //
// THIS IS BECAUSE THESE UNIT TESTS REQUIRE THE APP CONTEXT FOR SQLite //
//** (MAY ALSO REQUIRE MULTIPLE TEST RUNS BEFORE SOME TESTS PASS!)   **//
/////////////////////////////////////////////////////////////////////////

package edu.team6.inventory;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

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
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void test1AddName() {
        Item item = new Item(1,"test1", 2.0, "Condition", "testDescription");
        database.addItem(item);
        assertEquals(database.getItem(1).getmName(), "test1");
    }

    @Test
    public void test2AddNameValue() {
        Item item = new Item(2,"test2", 2.0, "Condition", "testDescription");
        database.addItem(item);
        assertEquals(database.getItem(2).getmName(), "test2");
        assertEquals(database.getItem(2).getmValue(), 2.0, THRESHOLD);
    }

    @Test
    public void test3AddNameValueCondition() {
        Item item = new Item(3, "test3", 2.0, "Brand New", "testDescription");
        database.addItem(item);
        assertEquals("test3", database.getItem(3).getmName());
        assertEquals(database.getItem(3).getmValue(), 2.0, THRESHOLD);
        assertEquals("Brand New", database.getItem(3).getmCondition());
    }

    @Test
    public void test4AddNameValueConditionDescription() {
        Item item = new Item(4, "test4", 44, "New", "testDescription4");
        database.addItem(item);
        assertEquals("test4", database.getItem(4).getmName());
        assertEquals(database.getItem(4).getmValue(), 44, THRESHOLD);
        assertEquals("New", database.getItem(4).getmCondition());
        assertEquals("testDescription4", database.getItem(4).getmDescription());
    }

    @Test
    public void test5UpdateName() throws  Exception {
        database.addItem(new Item(5,"test5", 2.0, "Condition", "testDescription"));
        database.updateItem(new Item(5,"test5UPDATED", 2.0, "Condition_updated", "testDescription"));
        Item item = database.getItem(5);
        assertEquals(item.getmName(), "test5UPDATED");
    }

    @Test
    public void test6UpdateValue() throws  Exception {
        database.addItem(new Item(6,"test6", 2.0, "Condition", "testDescription"));
        database.updateItem(new Item(6,"nameUpdate", 666, "Condition_updated", "testDescription"));
        Item item = database.getItem(6);
        assertEquals(666, item.getmValue(), THRESHOLD);
    }

    @Test
    public void test7UpdateCondition() throws  Exception {
        database.addItem(new Item(7,"test7", 2.0, "Brand New", "testDescription"));
        database.updateItem(new Item(7,"test7nameUpdate", 666, "VERY OLD", "testDescription"));
        Item item = database.getItem(7);
        assertEquals("VERY OLD", item.getmCondition());
    }

    @Test
    public void test8UpdateDescription() throws  Exception {
        database.addItem(new Item(8,"test8", 2.0, "Brand New", "OLD DESCRIPTION"));
        database.updateItem(new Item(8,"test8nameUpdate", 666, "VERY OLD", "UPDATED DESCRIPTION"));
        Item item = database.getItem(8);
        assertEquals("UPDATED DESCRIPTION", item.getmDescription());
    }

    @Test
    public void test9Delete() throws Exception {
        Item item = new Item(9,"test2", 2.0, "Condition", "testDescription");
        // Get current (old) count of items
        int base = database.getItemCount();
        // Add an item
        database.addItem(item);
        // Delete item.
        database.deleteItem(item);
        // Check that Item count is the same.
        assertEquals(database.getItemCount(), base);
    }


}