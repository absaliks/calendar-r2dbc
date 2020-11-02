package absaliks.calendar.repository

import absaliks.calendar.exceptions.EntityNotFoundException
import absaliks.calendar.model.Event
import absaliks.calendar.model.EventLogEntry
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EventRepository : ReactiveCrudRepository<Event, Long>

interface EventLogRepository : ReactiveCrudRepository<EventLogEntry, Long> {
    fun findByEventId(id: Long): Flux<EventLogEntry>

    fun requireOne(id: Long): Mono<EventLogEntry> {
        return findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException(id, EventLogEntry::class)))
    }
}