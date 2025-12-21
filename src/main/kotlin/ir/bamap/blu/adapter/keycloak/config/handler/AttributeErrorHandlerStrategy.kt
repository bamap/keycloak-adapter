package ir.bamap.blu.adapter.keycloak.config.error.handler

import ir.bamap.blu.adapter.keycloak.exception.AttributeRequiredException
import ir.bamap.blu.adapter.keycloak.exception.SecurityAdapterException
import ir.bamap.blu.exception.BluException
import org.springframework.web.reactive.function.client.ClientResponse

class AttributeErrorHandlerStrategy(
    private val errorMessageExceptionMap: Map<String, SecurityAdapterException> = HashMap()
) : ErrorHandlerStrategy {

    override fun getExceptionOrNull(errorBody: Map<String, Any?>, response: ClientResponse): BluException? {
        val errorMessage = errorBody["errorMessage"] as String?

        if (errorMessage === "error-user-attribute-required") {
            val field: Any = errorBody["field"]!!
            return AttributeRequiredException(field.toString())
        }

        return null
    }
}