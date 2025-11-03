package ir.bamap.blu.adapter.keycloak.config.error.handler

import feign.Response
import ir.bamap.blu.exception.AccessDeniedException
import ir.bamap.blu.exception.BluException

class ByStatusErrorHandlerStrategy : ErrorHandlerStrategy {

    override fun getExceptionOrNull(errorBody: Map<String, Any?>, response: Response): BluException? {
        if (response.status() == 403)
            return AccessDeniedException("", "", "{blu.accessDenied}", mapOf())

        return null
    }
}