package absaliks.calendar.config

import absaliks.calendar.events.Event
import absaliks.calendar.events.EventRepository
import absaliks.calendar.events.Recurrence
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.DatabaseClient
import reactor.core.publisher.Flux
import java.time.Instant


@Configuration
class DBInitializer(
    private val repository: EventRepository,
    private val db: DatabaseClient
) {

    @Bean
    fun initEvents() = CommandLineRunner {
        val initSchema = db.execute {
            """ CREATE TABLE EVENT (
                    id IDENTITY NOT NULL PRIMARY KEY,
                    name VARCHAR(127) NOT NULL,
                    description VARCHAR(255),
                    start_date TIMESTAMP NOT NULL,
                    end_date TIMESTAMP,
                    recurrence TINYINT,
                    recurrence_extra SMALLINT
                );
            """
        }

        val now = Instant.now()
        val populateEvents = repository.saveAll(
            Flux.just(
                Event(null, "Do the backend part", "Using Spring reactive stack", now, null, Recurrence.DAILY, null),
                Event(null, "Do the frontend part", "Using Angular", now, null, Recurrence.DAILY, 1),
                Event(null, "Integrate", null, now, null, Recurrence.WEEKLY, null)
            )
        )

        initSchema
            .then()
            .thenMany(populateEvents)
            .subscribe()
    }
}