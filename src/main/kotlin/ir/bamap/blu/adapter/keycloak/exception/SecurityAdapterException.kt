package ir.bamap.blu.adapter.keycloak.exception

import ir.bamap.blu.exception.BluException

open class SecurityAdapterException(
    statusCode: Int,
    message: String,
    args: Map<String, Any?> = emptyMap(),
): BluException(statusCode, message, args) {

}