package edu.team6.inventory;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import edu.team6.inventory.data.Item;

import static org.junit.Assert.*;

/**
 * JUnit Tests for Items.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemUnitTest {

    /** The threshold for testing doubles. */
    private static final double THRESHOLD = 0.000000001;

    /** A Test Item. */
    public Item test;

    @Before
    public void setUp() {
        test = new Item("Test", 42, "Brand New", "Test Description");
    }

    @Test
    public void test1getItemName() {
        assertEquals("Test", test.getmName());
    }

    @Test
    public void test2setItemName() {
        test.setmName("Test2SetName");
        assertEquals("Test2SetName", test.getmName());
    }

    @Test
    public void test3getItemValue() {
        assertEquals(42, test.getmValue(), THRESHOLD);
    }

    @Test
    public void test4setItemValue() {
        test.setmValue(123);
        assertEquals(123, test.getmValue(), THRESHOLD);
    }

    @Test
    public void test5getItemValue() {
        assertEquals("Brand New", test.getmCondition());
    }

    @Test
    public void test6setItemValue() {
        test.setmCondition("Old");
        assertEquals("Old", test.getmCondition());
    }

    @Test
    public void test7getItemDescription() {
        assertEquals("Test Description", test.getmDescription());
    }

    @Test
    public void test8setItemDescription() {
        test.setmDescription("Setting a new description");
        assertEquals("Setting a new description", test.getmDescription());
    }

    @Test
    public void test9itemConstructorNameValue() {
        Item testItem = new Item ("Name and Value", 0, null, null);
        assertEquals("Name and Value", testItem.getmName());
        assertEquals(0, testItem.getmValue(), THRESHOLD);
    }

    @Test
    public void tests11itemConstructorNameValueCondition() {
        Item testItem = new Item ("Name and Value", 0, "Very worn out.", null);
        assertEquals("Name and Value", testItem.getmName());
        assertEquals(0, testItem.getmValue(), THRESHOLD);
        assertEquals("Very worn out.", testItem.getmCondition());
    }

    @Test
    public void tests12itemConstructorNameValueConditionDescription() {
        Item testItem = new Item ("Name and Value", 0, "Very worn out.", "A very worn out item obtained from your local Walmart.");
        assertEquals("Name and Value", testItem.getmName());
        assertEquals(0, testItem.getmValue(), THRESHOLD);
        assertEquals("Very worn out.", testItem.getmCondition());
        assertEquals("A very worn out item obtained from your local Walmart.", testItem.getmDescription());
    }

}