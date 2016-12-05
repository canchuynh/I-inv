package edu.team6.inventory;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Random;

import edu.team6.inventory.activities.InventoryActivity;
import edu.team6.inventory.activities.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

///////
// IMPORTANT NOTE: THESE TESTS REQUIRE DISABLING ANIMATIONS UNDER DEVELOPER SETTINGS ON DEVICE
///////

/**
 * These tests will test the activity used to add an item.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditItemActivityInstrumentedTest {

    @RunWith(AndroidJUnit4.class)
    @LargeTest
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    public class SignActivityLoginTest {

        @Rule
        public ActivityTestRule<InventoryActivity> mActivityRule = new ActivityTestRule<>(
                InventoryActivity.class);

    }

    @Rule
    public ActivityTestRule<InventoryActivity> mActivityRule = new ActivityTestRule<>(
            InventoryActivity.class);


    /**
     * Guarentees atleast one item added.
     * Add a testing item for editing/deleting.
     */
    @Test
    public void test1AddingAnItem() {
        Random r = new Random();
        onView(withId(R.id.fab))
                .perform(click());
        onView(withId(R.id.item_name_field)).perform(typeText("Test Item " + r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));

        // pressing back button to remove keyboard.
        pressBack();

        // scroll down to adding button and click
        onView(withId(R.id.item_add_button))
                .perform(scrollTo(), click());

        onView(withText("Item successfully added!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     * Test editing an item with changes to name.
     */
    @Test
    public void test2EditingName() {
        Random r = new Random();

        // click on first item
        onData(anything()).inAdapterView(withId(R.id.items_listview)).atPosition(0).perform(click());

        // scroll down to editing an item button and click
        onView(withId(R.id.details_edit_button))
                .perform(scrollTo(), click());

        // Make some changesto that item.
        onView(withId(R.id.edit_name_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));

        // scroll down to save edits button and click
        onView(withId(R.id.save_edits_button))
                .perform(scrollTo(), click());

        onView(withText("Item saved successfully!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     * Test editing an item with changes to name and value.
     */
    @Test
    public void test3EditingNameValue() {
        Random r = new Random();

        // click on first item
        onData(anything()).inAdapterView(withId(R.id.items_listview)).atPosition(0).perform(click());

        // scroll down to editing an item button and click
        onView(withId(R.id.details_edit_button))
                .perform(scrollTo(), click());

        // Make some changesto that item.
        onView(withId(R.id.edit_name_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.edit_value_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));

        // scroll down to save edits button and click
        onView(withId(R.id.save_edits_button))
                .perform(scrollTo(), click());

        onView(withText("Item saved successfully!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     * Test editing an item with changes to name, value, and condition.
     */
    @Test
    public void test4EditingNameValueCondition() {
        Random r = new Random();

        // click on first item
        onData(anything()).inAdapterView(withId(R.id.items_listview)).atPosition(0).perform(click());

        // scroll down to editing an item button and click
        onView(withId(R.id.details_edit_button))
                .perform(scrollTo(), click());

        // Make some changesto that item.
        onView(withId(R.id.edit_name_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.edit_value_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.edit_condition_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));

        // scroll down to save edits button and click
        onView(withId(R.id.save_edits_button))
                .perform(scrollTo(), click());

        onView(withText("Item saved successfully!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     * Test editing an item with changes to name, value, condition, and description.
     */
    @Test
    public void test5EditingNameValueConditionDescription() {
        Random r = new Random();

        // click on first item
        onData(anything()).inAdapterView(withId(R.id.items_listview)).atPosition(0).perform(click());

        // scroll down to editing an item button and click
        onView(withId(R.id.details_edit_button))
                .perform(scrollTo(), click());

        // Make some changesto that item.
        onView(withId(R.id.edit_name_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.edit_value_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.edit_condition_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.edit_description_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));

        // scroll down to save edits button and click
        onView(withId(R.id.save_edits_button))
                .perform(scrollTo(), click());

        onView(withText("Item saved successfully!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     * Test deleting an item.
     */
    @Test
    public void test6DeletingAnItem() {

        // click on first item
        onData(anything()).inAdapterView(withId(R.id.items_listview)).atPosition(0).perform(click());

        // scroll down to deleting an item button and click
        onView(withId(R.id.deleteItemButton))
                .perform(scrollTo(), click());

        onView(withText("OK")).perform(click());


        // CHeck to see the delete message occured.
        onView(withText("Item deleted!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

}