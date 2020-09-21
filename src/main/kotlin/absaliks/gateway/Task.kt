package absaliks.gateway

import org.springframework.data.annotation.Id

data class Task (
    @Id val id: Long?,
    val name: String,
    val description: String?
)