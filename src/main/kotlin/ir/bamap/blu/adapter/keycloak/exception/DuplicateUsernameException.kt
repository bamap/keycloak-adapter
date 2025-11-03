package ir.bamap.blu.adapter.keycloak.exception

class DuplicateUsernameException(
    val username: String
) : SecurityAdapterException(409, "{blu.security.duplicateUsername}", mapOf("username" to username)) {
}