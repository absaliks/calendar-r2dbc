package absaliks.calendar.router

import absaliks.calendar.model.EventLogEntry
import absaliks.calendar.repository.EventLogRepository
import absaliks.calendar.utils.Patcher
import absaliks.calendar.utils.typeReference
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class EventLogHandler(private val eventLogRepository: EventLogRepository) {

    fun getEventLog(request: ServerRequest): Mono<ServerResponse> {
        val eventId = request.pathVariable("eventId").toLong()
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(eventLogRepository.findByEventId(eventId), EventLogEntry::class.java)
    }

    fun createEntry(request: ServerRequest) =
        request.bodyToMono(EventLogEntry::class.java)
            .flatMap(::saveAndRespond)

    private fun saveAndRespond(event: EventLogEntry) = ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(eventLogRepository.save(event), EventLogEntry::class.java)

    fun updateEntry(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return request.bodyToMono(typeReference<JsonNode>())
            .zipWith(eventLogRepository.findById(id)) { patch, existing ->
                applyPatch(patch, existing)
            }
            .flatMap(::saveAndRespond)
    }

    fun deleteEntry(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return eventLogRepository.deleteById(id)
            .then(ServerResponse.noContent().build())
    }

    private fun applyPatch(patch: JsonNode, existing: EventLogEntry): EventLogEntry {
        val patcher = Patcher(patch)
        return EventLogEntry(
            id = existing.id,
            eventId = existing.eventId,
            date = patcher.patch("date", existing.date),
            done = patcher.patch("done", existing.done),
            comment = patcher.patchNullable("comment", existing.comment)
        )
    }
}
