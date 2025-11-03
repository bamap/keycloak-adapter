package ir.bamap.blu.adapter.keycloak.config.error.handler

import feign.Response
import ir.bamap.blu.exception.BluException

interface ErrorHandlerStrategy {
    fun getExceptionOrNull(errorBody: Map<String, Any?>, response: Response): BluException?
}