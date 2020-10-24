package absaliks.calendar.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class Event(
    @Id val id: Long?,
    val name: String,
    val description: String?,
    val startDate: Instant,
    val endDate: Instant?,
    val recurrence: Recurrence?,
    val recurrenceExtra: Int?
)

@Table("EVENT_LOG")
data class EventLogEntry(
    @Id val id: Long?,
    val eventId: Long,
    val date: Instant,
    val done: Boolean,
    val comment: String?
)