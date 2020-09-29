package absaliks.gateway

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.DatabaseClient
import reactor.core.publisher.Flux


@Configuration
class DBInitializer(
    private val repository: TaskRepository,
    private val db: DatabaseClient
) {

    @Bean
    fun initTasks() = CommandLineRunner {
        val initSchema = db.execute {
            """ CREATE TABLE TASK (
                    id IDENTITY NOT NULL PRIMARY KEY,
                    name VARCHAR(127) NOT NULL,
                    description VARCHAR(255)
                );
            """
        }

        val populateTasks = repository.saveAll(
            Flux.just(
                Task(null, "Do the backend part", "Using Spring reactive stack"),
                Task(null, "Do the frontend part", "Using Angular"),
                Task(null, "Integrate", null)
            )
        )

        initSchema
            .then()
            .thenMany(populateTasks)
            .subscribe()
    }
}