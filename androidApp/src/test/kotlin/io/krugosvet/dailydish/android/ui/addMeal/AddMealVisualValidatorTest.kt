package io.krugosvet.dailydish.android.ui.addMeal

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class AddMealVisualValidatorTest {

    private lateinit var validator: AddMealVisualValidator

    @Before
    fun setup() {
        validator = AddMealVisualValidator()
    }

    @Test
    fun `test valid title`() {
        // given

        val title = "Valid title"

        // then

        assertTrue(validator.isTitleValid(title))
    }

    @Test
    fun `test invalid title`() {
        // given

        val title = ""

        // then

        assertFalse(validator.isTitleValid(title))
    }
}
