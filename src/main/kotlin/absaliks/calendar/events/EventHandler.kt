package absaliks.calendar.events

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class EventHandler(private val repository: EventRepository) {
    fun getAllEvents(request: ServerRequest) = ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(repository.findAll(), Event::class.java)

    fun getItem(request: ServerRequest) = ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(repository.findById(request.pathVariable("id").toLong()), Event::class.java)
        .switchIfEmpty(ServerResponse.notFound().build())
}
