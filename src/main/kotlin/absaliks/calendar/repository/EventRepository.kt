package absaliks.calendar.repository

import absaliks.calendar.model.Event
import absaliks.calendar.model.EventLogEntry
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface EventRepository : ReactiveCrudRepository<Event, Long>

interface EventLogRepository : ReactiveCrudRepository<EventLogEntry, Long> {
    fun findByEventId(id: Long): Flux<EventLogEntry>
}