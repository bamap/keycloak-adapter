package ir.bamap.blu.adapter.keycloak.exception


class DuplicateEmailException(
    val email: String
) : SecurityAdapterException(409, "{blu.security.duplicateEmail}", mapOf("email" to email)) {
}