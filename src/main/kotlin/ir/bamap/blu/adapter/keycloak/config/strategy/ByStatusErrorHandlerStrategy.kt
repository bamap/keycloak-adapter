package ir.bamap.blu.adapter.keycloak.config.strategy

import ir.bamap.blu.adapter.config.error.handler.ErrorDecoderStrategy
import ir.bamap.blu.adapter.config.model.JsonResponseModel
import ir.bamap.blu.exception.AccessDeniedException
import ir.bamap.blu.exception.BluException

class ByStatusErrorHandlerStrategy : ErrorDecoderStrategy {

    override fun getExceptionOrNull(response: JsonResponseModel): BluException? {
        if (response.statusCode.value() == 403)
            return AccessDeniedException("", "", "{blu.accessDenied}", mapOf())

        return null
    }
}