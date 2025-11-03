package ir.bamap.blu.adapter.keycloak.exception

class AttributeRequiredException(
    val field: String,
) : SecurityAdapterException(
    400,
    "{blu.security.requiredAttribute}",
    mapOf("field" to field)
) {
}