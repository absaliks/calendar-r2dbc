package absaliks.calendar.events

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class EventRouter(private val eventHandler: EventHandler) {

    @Bean
    fun route() = router {
        accept(MediaType.APPLICATION_JSON).nest {
            "/events".nest {
                GET("/", eventHandler::getAllEvents)
                GET("/{id}", eventHandler::getItem)
            }
        }
    }
}