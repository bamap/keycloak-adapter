package ir.bamap.blu.adapter.keycloak.config.error.handler

import ir.bamap.blu.exception.AccessDeniedException
import ir.bamap.blu.exception.BluException
import org.springframework.web.reactive.function.client.ClientResponse

class ByStatusErrorHandlerStrategy : ErrorHandlerStrategy {

    override fun getExceptionOrNull(errorBody: Map<String, Any?>, response: ClientResponse): BluException? {
        if (response.statusCode().value() == 403)
            return AccessDeniedException("", "", "{blu.accessDenied}", mapOf())

        return null
    }
}