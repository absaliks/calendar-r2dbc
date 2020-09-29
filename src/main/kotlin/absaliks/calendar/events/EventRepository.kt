package absaliks.calendar.events

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface EventRepository : ReactiveCrudRepository<Event, Long>