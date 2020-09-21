package absaliks.gateway

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface TaskRepository : ReactiveCrudRepository<Task, Long>