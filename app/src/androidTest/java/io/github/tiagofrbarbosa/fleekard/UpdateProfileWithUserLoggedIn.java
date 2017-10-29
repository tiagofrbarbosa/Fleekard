package io.github.tiagofrbarbosa.fleekard;

/**
 * Created by tfbarbosa on 29/10/17.
 */

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
 * Created by tfbarbosa on 29/10/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpdateProfileWithUserLoggedIn {

    @Rule
    public ActivityTestRule<SignIn> mActivityRule = new ActivityTestRule<>(SignIn.class);

    @Test
    public void updateProfile(){

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Profile settings")).perform(click());

        onView(withId(R.id.user_name)).perform(clearText(), typeText("teste"), closeSoftKeyboard());
        onView(withId(R.id.user_status)).perform(clearText(), typeText("teste"), closeSoftKeyboard());
        onView(withId(R.id.user_gender)).perform(click());
        onView(withText("Male")).perform(click());
        onView(withId(R.id.user_age)).perform(clearText(), typeText("20"), closeSoftKeyboard());

        onView(withId(R.id.fab_save)).perform(click());
    }
}
