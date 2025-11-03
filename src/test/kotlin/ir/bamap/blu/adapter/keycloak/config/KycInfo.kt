package ir.bamap.blu.adapter.keycloak.config

class KycInfo(
    val url: String,
    val realm: String,
    val clientId: String,
    val clientSecret: String
) {

    fun getRealmUrl(): String = "$url/realms/$realm"

    fun getAdminRealmUrl(): String = "$url/admin/realms/$realm"
}