package absaliks.calendar.repository

import absaliks.calendar.model.Recurrence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource


internal object R {
    /**
     * NOTE: I use map instead of "list + list.index" on purpose, so that it would be harder
     * to make mistake by fixing tests.
     * */
    val expectedRecurrenceOrdinals = mapOf(
        Recurrence.DAILY to 0,
        Recurrence.WEEKLY to 1,
        Recurrence.MONTHLY to 2,
        Recurrence.YEARLY to 3,
        Recurrence.FIXED_PERIOD to 4,
        Recurrence.WEEKDAYS to 5,
        Recurrence.DAY_OF_WEEK_SET to 6
    )
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RecurrenceTest {

    /** WARNING: Do not fix for existing enum items!
     *
     * Test below has one purpose - to make sure that enum items' ordinal is never changed
     * (at least after the moment when the app hits production environment for the first time).
     *
     * Rationale: Recurrence is stored in DB as integer, not as text; with value - enum ordinal.
     */
    @ParameterizedTest
    @EnumSource
    internal fun `item order not changed`(recurrence: Recurrence) {
        val expectedOrdinal = R.expectedRecurrenceOrdinals[recurrence]
        assertEquals(expectedOrdinal, recurrence.ordinal)
    }
}