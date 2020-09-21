package absaliks.gateway

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux

class DBInitializer

@Bean
internal fun init(repository: TaskRepository) = CommandLineRunner {
    Flux.just(
        Task(null, "Do the backend part", "Using Spring reactive stack"),
        Task(null, "Do the frontend part", "Using Angular"),
        Task(null, "Integrate", null)
    ).flatMap { repository.save(it) }
}