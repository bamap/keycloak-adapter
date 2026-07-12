package ir.bamap.blu.adapter.keycloak.config.strategy

import ir.bamap.blu.adapter.config.error.handler.ErrorDecoderStrategy
import ir.bamap.blu.adapter.config.model.JsonResponseModel
import ir.bamap.blu.adapter.keycloak.config.Messages
import ir.bamap.blu.adapter.keycloak.exception.*
import ir.bamap.blu.exception.BluException

open class GeneralErrorHandlerStrategy : ErrorDecoderStrategy {

    protected val errorDescriptionExceptionMap = mutableMapOf<String, SecurityAdapterException>()
    protected val errorMessageExceptionMap = mutableMapOf<String, SecurityAdapterException>()

    init {
        initErrorDescriptionExceptionMap()
        initErrorMessageExceptionMap()
    }

    override fun getExceptionOrNull(response: JsonResponseModel): BluException? {
        val description = response.jsonBody["error_description"] as String?
        val entry = errorDescriptionExceptionMap.get(description)
        if (entry != null) return entry

        val errorMessage = response.jsonBody["errorMessage"] as String?
        return errorMessageExceptionMap[errorMessage]
    }

    protected open fun initErrorDescriptionExceptionMap() {
        errorDescriptionExceptionMap[Messages.INVALID_CLIENT_CREDENTIALS] = InvalidClientCredentials()
        errorDescriptionExceptionMap[Messages.Keycloak.INVALID_CREDENTIALS] = InvalidUserCredentialException()
        errorDescriptionExceptionMap[Messages.ACCOUNT_NOT_FULLY_SETUP] = AccountNotFullySetupException()
    }

    protected open fun initErrorMessageExceptionMap() {
        errorMessageExceptionMap["User exists with same email"] = DuplicateEmailException("")
        errorMessageExceptionMap["User exists with same username"] = DuplicateUsernameException("")
    }
}