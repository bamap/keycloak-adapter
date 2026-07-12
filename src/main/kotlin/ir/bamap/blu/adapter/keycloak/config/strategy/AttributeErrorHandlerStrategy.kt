package ir.bamap.blu.adapter.keycloak.config.strategy

import ir.bamap.blu.adapter.config.error.handler.ErrorDecoderStrategy
import ir.bamap.blu.adapter.config.model.JsonResponseModel
import ir.bamap.blu.adapter.keycloak.exception.AttributeRequiredException
import ir.bamap.blu.adapter.keycloak.exception.SecurityAdapterException
import ir.bamap.blu.exception.BluException

class AttributeErrorHandlerStrategy(
    private val errorMessageExceptionMap: Map<String, SecurityAdapterException> = HashMap()
) : ErrorDecoderStrategy {

    override fun getExceptionOrNull(response: JsonResponseModel): BluException? {
        val errorMessage = response.jsonBody["errorMessage"] as String?

        if (errorMessage === "error-user-attribute-required") {
            val field: Any = response.jsonBody["field"]!!
            return AttributeRequiredException(field.toString())
        }

        return null
    }
}