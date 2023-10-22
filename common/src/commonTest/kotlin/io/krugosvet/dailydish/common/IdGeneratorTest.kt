package io.krugosvet.dailydish.common

import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertTrue

class IdGeneratorTest {

    private lateinit var idGenerator: IdGenerator

    @Test
    fun whenGenerateId_thenIdIsValid() {
        // when

        val id = idGenerator.generate()

        // then

        assertTrue(id.isNotBlank())
    }

    @BeforeTest
    fun setUp() {
        idGenerator = IdGenerator()
    }
}
