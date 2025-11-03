package ir.bamap.blu.adapter.keycloak.adapter

import ir.bamap.blu.adapter.keycloak.model.UserRepresentation
import org.springframework.web.bind.annotation.*
import java.util.*

interface KeycloakUserAdapter {

    @DeleteMapping("/users/{userId}")
    fun delete(@PathVariable(name = "userId") userId: UUID)

    @GetMapping("/users")
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

    @PostMapping("/users")
    fun add(@RequestBody user: UserRepresentation)

    @PutMapping("/users/{userId}")
    fun update(@RequestBody user: UserRepresentation, @PathVariable(name = "userId") userId: UUID)

    fun findSummaryByUsernameOrNull(username: String?): UserRepresentation? {
        if (username == null) return null
        return find(0, 1, username, null, null, null, null, null, true, false)
            .firstOrNull()
    }

    @GetMapping("/users/{userId}?userProfileMetadata=true")
    fun findById(@PathVariable(name = "userId") userId: UUID): UserRepresentation? //    @LoggablePutMapping("/users/{userId}")
    //    void edit(UserDetailsModel user, @PathVariable UUID userId);
}