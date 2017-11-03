package io.github.tiagofrbarbosa.fleekard;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.tiagofrbarbosa.fleekard.activity.SignIn;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by tfbarbosa on 31/10/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StressUpdateProfileWithUserLoggedIn {

    private static int LOOP_STRESS = 5;

    @Rule
    public ActivityTestRule<SignIn> mActivityRule = new ActivityTestRule<>(SignIn.class);

    @Test
    public void updateProfile(){

        for(int i=0;i<=LOOP_STRESS;i++) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText("Profile settings")).perform(click());

            onView(withId(R.id.user_name)).perform(clearText(), typeText("teste" + i), closeSoftKeyboard());
            onView(withId(R.id.user_status)).perform(clearText(), typeText("teste" + i), closeSoftKeyboard());
            onView(withId(R.id.user_gender)).perform(click());
            onView(withText("Male")).perform(click());
            onView(withId(R.id.user_age)).perform(clearText(), typeText(String.valueOf(i)), closeSoftKeyboard());

            onView(withId(R.id.fab_save)).perform(click());
        }
    }
}
