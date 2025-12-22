package ir.bamap.blu.adapter.keycloak.adapter

import ir.bamap.blu.adapter.keycloak.model.UserRepresentation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.annotation.PutExchange
import java.util.UUID

interface KeycloakUserAdapter {
    @DeleteExchange("/users/{userId}")
    fun delete(@PathVariable(name = "userId") userId: UUID)

    @GetExchange("/users")
    fun find(
        @RequestParam(required = false, name = "first") first: Int = 0,
        @RequestParam(required = false, name = "max") max: Int = 100,
        @RequestParam(required = false, name = "username") username: String? = null,
        @RequestParam(required = false, name = "email") email: String? = null,
        @RequestParam(required = false, name = "firstName") firstName: String? = null,
        @RequestParam(required = false, name = "lastName") lastName: String? = null,
        @RequestParam(required = false, name = "search") search: String? = null,
        @RequestParam(required = false, name = "q") q: String? = null,
        @RequestParam(required = false, name = "exact") exact: Boolean = true,
        @RequestParam(name = "briefRepresentation") briefRepresentation: Boolean = false
    ): List<UserRepresentation>

    @PostExchange("/users")
    fun add(@RequestBody user: UserRepresentation)

    @PutExchange("/users/{userId}")
    fun update(@RequestBody user: UserRepresentation, @PathVariable(name = "userId") userId: UUID)

    fun findSummaryByUsernameOrNull(username: String?): UserRepresentation? {
        if (username == null) return null
        return find(0, 1, username, null, null, null, null, null, true, false)
            .firstOrNull()
    }

    @GetExchange("/users/{userId}?userProfileMetadata=true")
    fun findById(@PathVariable(name = "userId") userId: UUID): UserRepresentation? //    @LoggablePutMapping("/users/{userId}")
}