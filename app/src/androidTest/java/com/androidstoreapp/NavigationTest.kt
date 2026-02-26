package com.androidstoreapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun bottom_nav_shows_products_screen_by_default() {
        val productsText = rule.activity.getString(com.androidstoreapp.core.ui.R.string.title_products)
        rule.onNodeWithText(productsText).assertIsSelected()
    }

    @Test
    fun tapping_favorites_tab_navigates_to_favorites_screen() {
        val favoritesText = rule.activity.getString(com.androidstoreapp.core.ui.R.string.title_favorites)
        rule.onNodeWithText(favoritesText).performClick()
        rule.onNodeWithTag("FavoritesScreen").assertIsDisplayed()
    }

    @Test
    fun tapping_profile_tab_navigates_to_profile_screen() {
        val profileText = rule.activity.getString(com.androidstoreapp.core.ui.R.string.title_profile)
        rule.onNodeWithText(profileText).performClick()
        rule.onNodeWithTag("ProfileScreen").assertIsDisplayed()
    }

    @Test
    fun navigating_back_to_products_from_favorites_shows_product_list() {
        val favoritesText = rule.activity.getString(com.androidstoreapp.core.ui.R.string.title_favorites)
        val productsText = rule.activity.getString(com.androidstoreapp.core.ui.R.string.title_products)
        rule.onNodeWithText(favoritesText).performClick()
        rule.onNodeWithText(productsText).performClick()
        rule.onNodeWithText(productsText).assertIsSelected()
    }
}
