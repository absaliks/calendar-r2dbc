package absaliks.calendar.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class EventLogRouter(private val eventLogHandler: EventLogHandler) {

    @Bean
    fun eventLogRoutes() = router {
        accept(MediaType.APPLICATION_JSON).nest {
            GET("/events/{eventId}/log", eventLogHandler::getEventLog)
            "/eventLog".nest {
                POST("/", eventLogHandler::createEntry)
                PATCH("/{id}", eventLogHandler::updateEntry)
                DELETE("/{id}", eventLogHandler::deleteEntry)
            }
        }
    }
}