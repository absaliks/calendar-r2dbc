package absaliks.calendar.router

import absaliks.calendar.model.EventLogEntry
import absaliks.calendar.service.EventLogService
import absaliks.calendar.utils.typeReference
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class EventLogHandler(private val eventLogService: EventLogService) {

    fun getEventLog(request: ServerRequest): Mono<ServerResponse> {
        val eventId = request.pathVariable("eventId").toLong()
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(eventLogService.getEventLog(eventId), EventLogEntry::class.java)
    }

    fun createEntry(request: ServerRequest) =
        request.bodyToMono(EventLogEntry::class.java)
            .flatMap(eventLogService::createEntry)
            .flatMap { toJsonResponse(it) }

    fun updateEntry(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return request.bodyToMono(typeReference<JsonNode>())
            .flatMap { eventLogService.updateEntry(id, it) }
            .flatMap { toJsonResponse(it) }
    }

    private fun toJsonResponse(it: EventLogEntry): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(it, EventLogEntry::class.java)
    }

    fun deleteEntry(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return eventLogService.deleteEntry(id)
            .then(ServerResponse.noContent().build())
    }
}
