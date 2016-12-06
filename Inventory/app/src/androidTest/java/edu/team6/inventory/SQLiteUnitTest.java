/////////////////////////////////////////////////////////////////////////
// NOTE: THESE TESTS MUST BE RUN ON AN EMULATOR AFTER APP INSTALLATION //
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
        Item item = new Item(4,"test1", 2.0, "Condition", "testDescription");
        database.addItem(item);
        assertEquals(database.getItem(4).getmName(), "test1");
    }

    @Test
    public void test2AddNameValue() {
        Item item = new Item(5,"test2", 2.0, "Condition", "testDescription");
        database.addItem(item);
        assertEquals(database.getItem(5).getmName(), "test2");
        assertEquals(database.getItem(5).getmValue(), 2.0, THRESHOLD);
    }

    @Test
    public void test3AddNameValueCondition() {
        Item item = new Item(5, "test3", 2.0, "Brand New", "testDescription");
        database.addItem(item);
        assertEquals("test2", database.getItem(5).getmName());
        assertEquals(database.getItem(5).getmValue(), 2.0, THRESHOLD);
        assertEquals("Brand New", database.getItem(5).getmCondition());
    }

    @Test
    public void testDelete() throws Exception {
        Item item = new Item(2,"test2", 2.0, "Condition", "testDescription");
        int base = database.getItemCount();
        database.addItem(item);
        database.deleteItem(item);
        assertEquals(database.getItemCount(), base);
    }

    @Test
    public void testUpdate() throws  Exception {
        database.addItem(new Item(3,"name", 2.0, "Condition", "testDescription"));
        database.updateItem(new Item(3,"nameUpdate", 2.0, "Condition_updated", "testDescription"));
        Item item = database.getItem(3);
        assertEquals(item.getmCondition(), "Condition_updated");
    }

}