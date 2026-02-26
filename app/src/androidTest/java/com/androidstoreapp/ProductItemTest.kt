package com.androidstoreapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.androidstoreapp.core.ui.components.ProductItem
import com.androidstoreapp.core.ui.theme.StoreTheme
import com.androidstoreapp.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProductItemTest {

    @get:Rule val rule = createComposeRule()

    @Test
    fun clicking_add_favorite_button_triggers_onToggle_with_isFavorite_true() {
        var toggledId = -1
        var toggledFav = false

        rule.setContent {
            StoreTheme {
                ProductItem(
                    product = Product(1, "Shirt", 19.99, "", isFavorite = false),
                    onToggle = { id, fav -> toggledId = id; toggledFav = fav }
                )
            }
        }

        rule.onNodeWithContentDescription("Add to favorites").performClick()
        assertEquals(1, toggledId)
        assertTrue(toggledFav)
    }

    @Test
    fun clicking_remove_favorite_button_triggers_onToggle_with_isFavorite_false() {
        var toggledFav = true

        rule.setContent {
            StoreTheme {
                ProductItem(
                    product = Product(1, "Shirt", 19.99, "", isFavorite = true),
                    onToggle = { _, fav -> toggledFav = fav }
                )
            }
        }

        rule.onNodeWithContentDescription("Remove from favorites").performClick()
        assertFalse(toggledFav)
    }

    @Test
    fun non_favorite_product_shows_add_favorite_icon() {
        rule.setContent {
            StoreTheme {
                ProductItem(
                    Product(1, "Shirt", 19.99, "", isFavorite = false),
                    onToggle = { _, _ -> }
                )
            }
        }

        rule.onNodeWithContentDescription("Add to favorites").assertIsDisplayed()
    }

    @Test
    fun favorite_product_shows_remove_favorite_icon() {
        rule.setContent {
            StoreTheme {
                ProductItem(
                    Product(1, "Shirt", 19.99, "", isFavorite = true),
                    onToggle = { _, _ -> }
                )
            }
        }

        rule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
    }
}
