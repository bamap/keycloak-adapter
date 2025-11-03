package ir.bamap.blu.adapter.keycloak.exception

class InvalidClientCredentials() : SecurityAdapterException(401, "{blu.security.invalidClientCredentials}") {
}