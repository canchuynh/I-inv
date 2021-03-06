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
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddItemActivityInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.team6.inventory", appContext.getPackageName());
    }

    @RunWith(AndroidJUnit4.class)
    public class SignActivityLoginTest {

        @Rule
        public ActivityTestRule<InventoryActivity> mActivityRule = new ActivityTestRule<>(
                InventoryActivity.class);

    }

    @Rule
    public ActivityTestRule<InventoryActivity> mActivityRule = new ActivityTestRule<>(
            InventoryActivity.class);


    @Test
    public void testAddItemFab() {
        onView(withId(R.id.fab))
                .perform(click());
        onView(allOf(withId(R.id.item_name_field)
                , withText("")))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests adding an item with only a name.
     */
    @Test
    public void testAddingAnItemName() {
        Random r = new Random();
        onView(withId(R.id.fab))
                .perform(click());
        onView(withId(R.id.item_name_field)).perform(typeText("Random Item Name " + r.nextInt(9) + r.nextInt(9)+ r.nextInt(9)));

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
     * Tests adding an item with only a name and value.
     */
    @Test
    public void testAddingAnItemNameValue() {
        Random r = new Random();
        onView(withId(R.id.fab))
                .perform(click());
        onView(withId(R.id.item_name_field)).perform(typeText("Random Item Name " + r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.item_value_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));

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
     * Tests adding an item with a name, value, and condition.
     */
    @Test
    public void testAddingAnItemNameValueCondition() {
        Random r = new Random();
        onView(withId(R.id.fab))
                .perform(click());
        onView(withId(R.id.item_name_field)).perform(typeText("Random Item Name " + r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.item_value_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.item_condition_field)).perform(typeText("New"));

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
     * Tests adding an item with a name, value, condition, and description.
     */
    @Test
    public void testAddingAnItemNameValueConditionDescription() {
        Random r = new Random();
        onView(withId(R.id.fab))
                .perform(click());
        onView(withId(R.id.item_name_field)).perform(typeText("Random Item Name " + r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.item_value_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.item_condition_field)).perform(typeText("New"));
        onView(withId(R.id.item_description_field)).perform(typeText("Random Test Description " + r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));

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


}
