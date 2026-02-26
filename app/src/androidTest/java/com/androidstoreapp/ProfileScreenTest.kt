package com.androidstoreapp

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import com.androidstoreapp.core.ui.theme.StoreTheme
import com.androidstoreapp.feature.profile.FavoritesCountCard
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule val rule = createComposeRule()

    @Test
    fun favorites_count_card_shows_correct_number() {
        rule.setContent {
            StoreTheme {
                FavoritesCountCard(count = 5)
            }
        }

        rule.onNodeWithTag("FavoriteCount", useUnmergedTree = true)
        val expectedText = ApplicationProvider.getApplicationContext<android.content.Context>()
            .resources.getQuantityString(com.androidstoreapp.core.ui.R.plurals.product_count, 5, 5)

        rule.onNodeWithTag("FavoriteCount", useUnmergedTree = true)
            .assertTextContains(expectedText)
    }

    @Test
    fun singular_label_when_count_is_1() {
        rule.setContent { StoreTheme { FavoritesCountCard(count = 1) } }
        rule.onNodeWithTag("FavoriteCount", useUnmergedTree = true)
        val expectedText = ApplicationProvider.getApplicationContext<android.content.Context>()
            .resources.getQuantityString(com.androidstoreapp.core.ui.R.plurals.product_count, 1, 1)

        rule.onNodeWithTag("FavoriteCount", useUnmergedTree = true)
            .assertTextContains(expectedText)
    }

    @Test
    fun plural_label_when_count_is_0() {
        rule.setContent { StoreTheme { FavoritesCountCard(count = 0) } }
        rule.onNodeWithTag("FavoriteCount", useUnmergedTree = true)
        val expectedText = ApplicationProvider.getApplicationContext<android.content.Context>()
            .resources.getQuantityString(com.androidstoreapp.core.ui.R.plurals.product_count, 0, 0)

        rule.onNodeWithTag("FavoriteCount", useUnmergedTree = true)
            .assertTextContains(expectedText)
    }
}
