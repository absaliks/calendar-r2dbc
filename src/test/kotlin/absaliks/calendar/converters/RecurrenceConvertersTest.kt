package absaliks.calendar.converters

import absaliks.calendar.model.Recurrence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class RecurrenceReadConverterTest {
    private val converter = RecurrenceReadConverter()

    @ParameterizedTest
    @EnumSource
    internal fun `should map Byte to Recurrence based on enum ordinal`(expectedRecurrence: Recurrence) {
        val givenByteValue = expectedRecurrence.ordinal.toByte()
        assertEquals(expectedRecurrence, converter.convert(givenByteValue))
    }
}

internal class RecurrenceWriteConverterTest {
    private val converter = RecurrenceWriteConverter()

    @ParameterizedTest
    @EnumSource
    internal fun `should map Recurrence to Byte based on enum ordinal`(giveRecurrence: Recurrence) {
        val expectedByteValue = giveRecurrence.ordinal.toByte()
        assertEquals(expectedByteValue, converter.convert(giveRecurrence))
    }
}