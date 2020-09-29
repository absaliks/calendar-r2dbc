package absaliks.gateway

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class TaskRouter(private val taskHandler: TaskHandler) {

    @Bean
    fun route() = router {
        accept(MediaType.APPLICATION_JSON).nest {
            "/tasks".nest {
                GET("/", taskHandler::getAllTasks)
                GET("/{id}", taskHandler::getItem)
            }
        }
    }
}