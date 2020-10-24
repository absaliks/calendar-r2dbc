package absaliks.calendar.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import java.time.Instant

@JsonTest
@TestInstance(PER_CLASS)
internal class PatcherTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper
    private val instant = Instant.now()
    private lateinit var instantStr: String

    @BeforeAll
    fun init() {
        instantStr = objectMapper.writeValueAsString(instant)
    }


    @Nested
    inner class Nonnull {
        private val patch = """{ "int": 23,  "string": "Hello World!",  "instant": $instantStr }"""
        private val patcher: Patcher = Patcher(objectMapper.readTree(patch))

        @Test
        fun `patch int`() = assertEquals(23, patcher.patch("int", 42))

        @Test
        fun `patch string`() =
            assertEquals("Hello World!", patcher.patch("string", "ABC"))

        @Test
        fun `patch instant`() =
            assertEquals(instant, patcher.patch("instant", Instant.MIN))


        @Test
        fun `patchNullable int`() = assertEquals(23, patcher.patchNullable("int", 42))

        @Test
        fun `patchNullable string`() =
            assertEquals("Hello World!", patcher.patchNullable("string", "ABC"))

        @Test
        fun `patchNullable instant`() =
            assertEquals(instant, patcher.patchNullable("instant", Instant.MIN))
    }


    @Nested
    inner class PatchFieldValueWithNull {
        private val patch = """{ "field": null }"""
        private val patcher: Patcher = Patcher(objectMapper.readTree(patch))

        @Test
        fun `patch int - failure`() {
            assertThrows<IllegalArgumentException> { patcher.patch("field", 42) }
        }

        @Test
        fun `patch string - failure`() {
            assertThrows<IllegalArgumentException> { patcher.patch("field", "ABC") }
        }

        @Test
        fun `patch instant - failure`() {
            assertThrows<IllegalArgumentException> { patcher.patch("field", Instant.MIN) }
        }


        @Test
        fun `patchNullable int - ok`() = assertEquals(null, patcher.patchNullable("field", 42))

        @Test
        fun `patchNullable string - ok`() =
            assertEquals(null, patcher.patchNullable("field", "ABC"))

        @Test
        fun `patchNullable instant - ok`() =
            assertEquals(null, patcher.patchNullable("field", Instant.MIN))
    }


    @Nested
    inner class FiledIsNotPatched {
        private val patch = "{ \"foo\": 512 }"
        private val patcher: Patcher = Patcher(objectMapper.valueToTree(patch))

        @Test
        fun `patch int`() = assertEquals(42, patcher.patch("int", 42))

        @Test
        fun `patch string`() =
            assertEquals("ABC", patcher.patch("string", "ABC"))

        @Test
        fun `patch instant`() =
            assertEquals(Instant.MIN, patcher.patch("instant", Instant.MIN))


        @Test
        fun `patchNullable int`() = assertEquals(42, patcher.patchNullable("int", 42))

        @Test
        fun `patchNullable string`() =
            assertEquals("ABC", patcher.patchNullable("string", "ABC"))

        @Test
        fun `patchNullable instant`() =
            assertEquals(Instant.MIN, patcher.patchNullable("instant", Instant.MIN))
    }
}