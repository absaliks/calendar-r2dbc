package absaliks.calendar

import absaliks.calendar.model.Event
import absaliks.calendar.model.Recurrence
import java.time.Instant

object TestData {
    val EVENT = Event(
        id = 1,
        name = "event-1",
        description = "description-1",
        startDate = Instant.MIN,
        endDate = Instant.MAX,
        recurrence = Recurrence.DAILY,
        recurrenceExtra = 1
    )
}