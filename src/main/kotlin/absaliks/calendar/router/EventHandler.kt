package absaliks.calendar.router

import absaliks.calendar.model.Event
import absaliks.calendar.model.Recurrence
import absaliks.calendar.repository.EventRepository
import absaliks.calendar.utils.Patcher
import absaliks.calendar.utils.typeReference
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class EventHandler(private val eventRepository: EventRepository) {
    fun getAllEvents(request: ServerRequest) = ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(eventRepository.findAll(), Event::class.java)

    fun getEvent(request: ServerRequest) = ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(eventRepository.findById(request.pathVariable("id").toLong()), Event::class.java)
        .switchIfEmpty(ServerResponse.notFound().build())

    fun createEvent(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Event::class.java)
            .flatMap(::saveAndRespond)
            .switchIfEmpty(ServerResponse.notFound().build())

    private fun saveAndRespond(event: Event) = ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(eventRepository.save(event), Event::class.java)

    fun updateEvent(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return request.bodyToMono(typeReference<JsonNode>())
            .zipWith(eventRepository.findById(id)) { patch, existing -> applyPatch(patch, existing) }
            .flatMap(::saveAndRespond)
    }

    private fun applyPatch(patch: JsonNode, existing: Event): Event {
        val patcher = Patcher(patch)
        return Event(
            id = existing.id,
            name = patcher.patch("name", existing.name),
            description = patcher.patchNullable("description", existing.description),
            startDate = patcher.patch("startDate", existing.startDate),
            endDate = patcher.patchNullable("endDate", existing.endDate),
            recurrence = patcher.patch("recurrence", existing.recurrence, this::enumConverter),
            recurrenceExtra = patcher.patchNullable("recurrenceExtra", existing.recurrenceExtra)
        )
    }

    private fun enumConverter(node: JsonNode) = Recurrence.valueOf(node.textValue())
}
