package absaliks.calendar.service

import absaliks.calendar.exceptions.EntityNotFoundException
import absaliks.calendar.model.EventLogEntry
import absaliks.calendar.repository.EventLogRepository
import absaliks.calendar.repository.EventRepository
import absaliks.calendar.utils.Patcher
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.SynchronousSink

@Service
class EventLogService(
    private val eventRepository: EventRepository,
    private val eventLogRepository: EventLogRepository
) {

    fun getEventLog(eventId: Long): Flux<EventLogEntry> =
        eventLogRepository.findByEventId(eventId)

    fun createEntry(logEntry: EventLogEntry): Mono<EventLogEntry> {
        return validateEntry(logEntry)
            .flatMap { eventLogRepository.save(it) }
    }

    private fun validateEntry(logEntry: EventLogEntry): Mono<EventLogEntry> {
        return if (logEntry.eventId < 1L)
            Mono.error(IllegalArgumentException("eventId is invalid"))
        else
            eventRepository.existsById(logEntry.eventId)
                .handle { exist, sink: SynchronousSink<EventLogEntry> ->
                    if (exist) sink.next(logEntry)
                    else sink.error(IllegalArgumentException("Event #${logEntry.eventId} does not exist"))
                }
    }

    fun updateEntry(id: Long, patch: JsonNode): Mono<EventLogEntry> {
        val patcher = Patcher(patch)
        return eventLogRepository.findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException(id, EventLogEntry::class)))
            .map { existing ->
                EventLogEntry(
                    id = existing.id,
                    eventId = existing.eventId,
                    date = patcher.patch("date", existing.date),
                    done = patcher.patch("done", existing.done),
                    comment = patcher.patchNullable("comment", existing.comment)
                )
            }
            .flatMap { eventLogRepository.save(it) }
    }

    fun deleteEntry(id: Long): Mono<Void> = eventLogRepository.deleteById(id)

}