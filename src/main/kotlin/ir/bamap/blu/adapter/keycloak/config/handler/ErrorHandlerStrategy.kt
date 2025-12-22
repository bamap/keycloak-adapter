package ir.bamap.blu.adapter.keycloak.config.error.handler

import ir.bamap.blu.exception.BluException
import org.springframework.web.reactive.function.client.ClientResponse

interface ErrorHandlerStrategy {
    fun getExceptionOrNull(errorBody: Map<String, Any?>, response: ClientResponse): BluException?
}