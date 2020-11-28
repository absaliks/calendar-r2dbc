package absaliks.calendar.router

import absaliks.calendar.TestData.EVENT
import absaliks.calendar.any
import absaliks.calendar.model.Event
import absaliks.calendar.repository.EventRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


internal class EventRouterTest {
    private val eventRepository: EventRepository = Mockito.mock(EventRepository::class.java)

    private val eventHandler = EventHandler(eventRepository)

    private val client = WebTestClient
        .bindToRouterFunction(EventRouter(eventHandler).eventRoutes())
        .build()

    @Test
    internal fun getAllEvents() {
        Mockito.`when`(eventRepository.findAll())
            .thenReturn(Flux.just(EVENT))

        client.get().uri("/events")
            .exchange()
            .expectStatus().isOk
            .expectBody<Array<Event>>().isEqualTo(arrayOf(EVENT))
    }

    @Test
    internal fun getEventById() {
        Mockito.`when`(eventRepository.findById(1))
            .thenReturn(Mono.just(EVENT))

        client.get().uri("/events/1")
            .exchange()
            .expectStatus().isOk
            .expectBody<Event>().isEqualTo(EVENT)
    }

    @Test
    internal fun createEvent() {
        val givenEvent = EVENT.copy(id = null)
        Mockito.`when`(eventRepository.save(givenEvent))
            .thenReturn(Mono.just(EVENT))

        client.post().uri("/events")
            .bodyValue(givenEvent)
            .exchange()
            .expectStatus().isOk
            .expectBody<Event>().isEqualTo(EVENT)

    }

    @Test
    internal fun patchEvent() {
        val newName = "Test name for patchEvent test"
        val givenPatch = "{\"name\": \"${newName}\"}"
        Mockito.`when`(eventRepository.findById(1))
            .thenReturn(Mono.just(EVENT))
        Mockito.`when`(eventRepository.save(any()))
            .thenAnswer { Mono.just(it.arguments[0]) }

        val expectedBody = EVENT.copy(name = newName)
        client.patch().uri("/events/1")
            .bodyValue(givenPatch)
            .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk
            .expectBody<Event>().isEqualTo(expectedBody)

    }
}