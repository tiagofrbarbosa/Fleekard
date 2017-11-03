package io.github.tiagofrbarbosa.fleekard;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import io.github.tiagofrbarbosa.fleekard.activity.SignIn;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by tfbarbosa on 29/10/17.
 */

public class LoginTestWithUserLoggedIn {

    private static int SLEEP_TIME = 4000;

    @Rule
    public ActivityTestRule<SignIn> mActivityRule = new ActivityTestRule<>(SignIn.class);

    @Test
    public void loginOnApp(){

        sleepApp(SLEEP_TIME);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Exit")).perform(click());

        onView(withId(R.id.email_button)).perform(click());
        onView(withId(R.id.email)).perform(typeText("teste@teste.com"), closeSoftKeyboard());
        onView(withId(R.id.button_next)).perform(click());

        sleepApp(SLEEP_TIME);

        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.button_done)).perform(click());

        sleepApp(SLEEP_TIME);
    }

    private void sleepApp(int sleepTime){

        try {
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
