package absaliks.calendar

import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.Period

internal class GeneralTest {

    val instant = Instant.now()

    @Test
    internal fun addMonths() {
        println(instant.plus(Period.ofDays(90)))
    }
}