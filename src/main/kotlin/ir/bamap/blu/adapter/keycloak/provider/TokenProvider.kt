package ir.bamap.blu.adapter.keycloak.provider

interface TokenProvider {
    fun getTokenOrNull(): String?
}