package ir.bamap.blu.adapter.keycloak.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder
import ir.bamap.blu.adapter.keycloak.config.error.handler.AttributeErrorHandlerStrategy
import ir.bamap.blu.adapter.keycloak.config.error.handler.ByStatusErrorHandlerStrategy
import ir.bamap.blu.adapter.keycloak.config.error.handler.ErrorHandlerStrategy
import ir.bamap.blu.adapter.keycloak.config.error.handler.GeneralErrorHandlerStrategy
import ir.bamap.blu.adapter.keycloak.exception.SecurityAdapterException
import ir.bamap.blu.adapter.keycloak.util.LogUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

open class KeycloakErrorDecoder(protected val objectMapper: ObjectMapper) : ErrorDecoder {
    protected val logger: Logger = LoggerFactory.getLogger(javaClass)
    protected val strategies = mutableListOf<ErrorHandlerStrategy>()

    init {
        initStrategies()
    }

    override fun decode(methodKey: String, response: Response): Exception {
        val body = getResponseBody(response)

        logError(methodKey, response, body)

        for (strategy in strategies) {
            val exception = strategy.getExceptionOrNull(body, response)
            if (exception != null) return exception
        }

        return SecurityAdapterException(500, "{blu.keycloak}", mutableMapOf<String, Any?>())
    }

    protected open fun logError(methodKey: String, response: Response, responseBody: Map<String, Any?>) {
        val responseArguments = HashMap<String, Any?>()
        responseArguments["body"] = responseBody
        responseArguments["status"] = response.status()

        val requestParams = HashMap<String?, String?>()
        val request = response.request()
        requestParams["url"] = request.url()
        requestParams["method"] = request.httpMethod().name

        val arguments = HashMap<String?, Any?>()
        arguments["methodKey"] = methodKey
        arguments["response"] = responseArguments
        arguments["request"] = requestParams

        LogUtil.log(logger.atError(), "Error in keycloak", arguments)
    }

    protected open fun getResponseBody(response: Response): MutableMap<String, Any?> {
        val body = response.body() ?: return mutableMapOf<String, Any?>()

        try {
            body.asInputStream().use { bodyIs ->
                return objectMapper.readValue(
                    bodyIs,
                    object : TypeReference<MutableMap<String, Any?>>() {
                    })
            }
        } catch (ignored: IOException) {
        }

        return mutableMapOf()
    }

    protected open fun initStrategies() {
        strategies.add(GeneralErrorHandlerStrategy())
        strategies.add(AttributeErrorHandlerStrategy())
        strategies.add(ByStatusErrorHandlerStrategy())
    }
}
