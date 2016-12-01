package edu.team6.inventory;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.team6.inventory.data.Item;
import edu.team6.inventory.data.SQLiteDBHandler;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;



@RunWith(AndroidJUnit4.class)
public class DbTest {
    private SQLiteDBHandler database;

    @Before
    public void setUp() throws Exception {
        database = new SQLiteDBHandler(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }



    @Test
    public void testAdd() throws Exception {
        Item item = new Item(11,"test1", 2.0, "Condition", "testDescription");
        database.addItem(item);
        assertEquals(database.getItem(4).getmCondition(), "Condition");

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
