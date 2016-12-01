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
import static android.support.test.espresso.action.ViewActions.click;
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

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.team6.inventory", appContext.getPackageName());
    }

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


    @Test
    public void testAddItemFab() {
        onView(withId(R.id.fab))
                .perform(click());
        onView(allOf(withId(R.id.item_name_field)
                , withText("")))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAddingAnItem() {
        Random r = new Random();
        onView(withId(R.id.fab))
                .perform(click());
        onView(withId(R.id.item_name_field)).perform(typeText("Random Item Name " + r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));
        onView(withId(R.id.item_value_field)).perform(typeText(""+ r.nextInt(5) + r.nextInt(5)+ r.nextInt(9)));

        // press the add item button
        onView(withId(R.id.item_add_button))
                .perform(click());

        onView(withText("Item successfully added!"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
