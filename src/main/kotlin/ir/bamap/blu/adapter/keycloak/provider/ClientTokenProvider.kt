package ir.bamap.blu.adapter.keycloak.provider

import ir.bamap.blu.adapter.keycloak.adapter.TokenAdapter
import ir.bamap.blu.adapter.keycloak.model.OAuthToken
import ir.bamap.blu.adapter.keycloak.model.login.LoginByClientModel
import java.util.*

class ClientTokenProvider(
    private val repository: TokenAdapter,
    private val clientId: String,
    private val clientSecret: String
) : TokenProvider {


    private var token: OAuthToken? = null

    override fun getTokenOrNull(): String {
        val constToken = token ?: return tryGetToken().getAuthorizationToken()
        if(constToken.isValid())
            return constToken.getAuthorizationToken()

        return tryGetToken().getAuthorizationToken()
    }

    @Synchronized
    private  fun tryGetToken(): OAuthToken {
        val nowInMilli = Date().time / 1000
        val parameters = LoginByClientModel(clientId, clientSecret)
        val newToken = repository.getToken(parameters)

        token = OAuthToken(
            newToken.accessToken,
            newToken.refreshToken,
            newToken.scope,
            newToken.tokenType,
            newToken.expiresIn + nowInMilli,
            newToken.refreshExpiresIn + nowInMilli,
        )

        return token!!
    }
}