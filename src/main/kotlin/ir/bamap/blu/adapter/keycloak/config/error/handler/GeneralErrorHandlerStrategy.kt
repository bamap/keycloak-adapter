package ir.bamap.blu.adapter.keycloak.config.error.handler

import feign.Response
import ir.bamap.blu.adapter.keycloak.config.Messages
import ir.bamap.blu.adapter.keycloak.exception.AccountNotFullySetupException
import ir.bamap.blu.adapter.keycloak.exception.DuplicateEmailException
import ir.bamap.blu.adapter.keycloak.exception.DuplicateUsernameException
import ir.bamap.blu.adapter.keycloak.exception.InvalidClientCredentials
import ir.bamap.blu.adapter.keycloak.exception.InvalidUserCredentialException
import ir.bamap.blu.adapter.keycloak.exception.SecurityAdapterException

open class GeneralErrorHandlerStrategy : ErrorHandlerStrategy {

    protected val errorDescriptionExceptionMap = mutableMapOf<String, SecurityAdapterException>()
    protected val errorMessageExceptionMap = mutableMapOf<String, SecurityAdapterException>()

    init {
        initErrorDescriptionExceptionMap()
        initErrorMessageExceptionMap()
    }

    override fun getExceptionOrNull(errorBody: Map<String, Any?>, response: Response): SecurityAdapterException? {
        val description = errorBody["error_description"] as String?
        val entry = errorDescriptionExceptionMap.get(description)
        if (entry != null) return entry

        val errorMessage = errorBody["errorMessage"] as String?
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