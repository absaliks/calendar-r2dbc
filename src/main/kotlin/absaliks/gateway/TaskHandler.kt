package absaliks.gateway

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class TaskHandler(private val taskRepository: TaskRepository) {
    fun getAllTasks(request: ServerRequest) = ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(taskRepository.findAll(), Task::class.java)

    fun getItem(request: ServerRequest) = ServerResponse.ok()
        .contentType(APPLICATION_JSON)
        .body(taskRepository.findById(request.pathVariable("id").toLong()), Task::class.java)
        .switchIfEmpty(ServerResponse.notFound().build())
}
