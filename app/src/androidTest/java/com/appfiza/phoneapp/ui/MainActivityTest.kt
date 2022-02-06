package com.appfiza.phoneapp.ui

import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appfiza.phoneapp.R
import com.appfiza.phoneapp.test.EspressoIdlingResource
import com.appfiza.phoneapp.utils.FileReader
import com.appfiza.phoneapp.utils.shouldHaveTextAtPosition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by Fayçal KADDOURI 🐈
 */

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Suppress("IllegalIdentifier")
class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val mockWebServer = MockWebServer()

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource())
        mockWebServer.shutdown()
        activityScenario.close()
    }

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("DevicesResponse.json"))
            }
        }
        activityScenario = launchActivity()
        activityScenario.onActivity {
            IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())
        }
    }

    @Test
    fun `Ensure Drawer is closed click on it favorites go to favorites`() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.myFavouritesDevicesFragment))
    }

    @Test
    fun `Ensure click on search and type VALET display empty view`() {
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText("1VALET"))
        onView(withId(R.id.imgDevice)).check(matches(isDisplayed()))
    }

    @Test
    fun `Ensure click on search and type apple display only rows related`() {
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText("apple"))
        with(R.id.recyclerViewDevicesList) { shouldHaveTextAtPosition("Apple iPhone XS", 1) }
    }

    @Test
    fun `Ensure click on first element it send us to details fragment`() {
        onView(withId(R.id.recyclerViewDevicesList)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )
        onView(withId(R.id.txtModel)).check(matches(isDisplayed()))
        onView(withId(R.id.txtModel)).check(matches(withText("Google Pixel 3")))
    }

    @Test
    fun `Go to details and click on favorite verify it is in favorites`() {
        onView(withId(R.id.recyclerViewDevicesList)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )
        onView(withId(R.id.action_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.action_favorite)).perform(click())
        pressBack()
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.myFavouritesDevicesFragment))
        with(R.id.recyclerViewDevicesList) { shouldHaveTextAtPosition("Google Pixel 3", 1) }
    }

    @Test
    fun `Ensure list is displayed with the first element Google`() {
        onView(withId(R.id.recyclerViewDevicesList)).check(matches(isDisplayed()))
        with(R.id.recyclerViewDevicesList) { shouldHaveTextAtPosition("Google Pixel 3", 1) }
    }

}