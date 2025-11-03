package ir.bamap.blu.adapter.keycloak.exception

class InvalidUserCredentialException() : SecurityAdapterException(401, "{blu.security.invalidUserCredentials}") {
}