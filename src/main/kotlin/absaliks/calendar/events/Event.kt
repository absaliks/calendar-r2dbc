package absaliks.calendar.events

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class Event (
    @Id val id: Long?,
    val name: String,
    val description: String?,
    val startDate: Instant,
    val endDate: Instant?,

    val recurrence: Recurrence?,
    val recurrenceExtra: Int?
)