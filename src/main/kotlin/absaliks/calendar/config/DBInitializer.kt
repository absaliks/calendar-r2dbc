package absaliks.calendar.config

import absaliks.calendar.model.Event
import absaliks.calendar.model.EventLogEntry
import absaliks.calendar.model.Recurrence
import absaliks.calendar.repository.EventLogRepository
import absaliks.calendar.repository.EventRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.DatabaseClient
import reactor.core.publisher.Flux
import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS

@Configuration
class DBInitializer(
    private val eventRepository: EventRepository,
    private val eventLogRepository: EventLogRepository,
    private val db: DatabaseClient
) {

    @Bean
    fun initEvents() = ApplicationRunner {
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
                
                CREATE TABLE EVENT_LOG (
                    id IDENTITY NOT NULL PRIMARY KEY,
                    event_id BIGINT NOT NULL,
                    date TIMESTAMP NOT NULL,
                    done BOOL NOT NULL,
                    comment VARCHAR(255)
                );
                
                CREATE INDEX IDX_EVENT_LOG__EVENT_ID ON EVENT_LOG(event_id);
            """
        }

        val now = Instant.now()

        val populateEvents = eventRepository.saveAll(
            Flux.just(
                Event(null, "Do Yoga", "Gotta move", now, null, Recurrence.DAILY, null),
                Event(null, "Water Plants", "Everyone need to drink", now, null, Recurrence.WEEKLY, null),
                Event(null, "Watch The Walking Dead", null, now, now.plus(60, DAYS), Recurrence.WEEKLY, null),
                Event(null, "Celebrate birthday", null, now, null, Recurrence.YEARLY, null),
                Event(null, "Appulse", "Watch big parade of the planets every 170 years!", now, null, Recurrence.YEARLY, 170)
            )
        )

        val populateEventLog = eventLogRepository.saveAll(
            Flux.just(
                EventLogEntry(null, 1, now.minus(2, DAYS), true, null),
                EventLogEntry(null, 1, now.minus(1, DAYS), false, "Felt lazy"),
                EventLogEntry(null, 1, now, true, "Yoga with Tim"),
                EventLogEntry(null, 2, now.minus(7, DAYS), true, null),
                EventLogEntry(null, 2, now, true, null)
            )
        )

        initSchema
            .then()
            .thenMany(populateEvents)
            .thenMany(populateEventLog)
            .subscribe()
    }
}