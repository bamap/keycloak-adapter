package ir.bamap.blu.adapter.keycloak.config

import feign.RequestInterceptor
import feign.RequestTemplate
import ir.bamap.blu.adapter.keycloak.provider.TokenProvider

class TokenRequestInterceptor(private val tokenProvider: TokenProvider) : RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        tokenProvider.getTokenOrNull()?.let { token ->
            template.header("Authorization", token)
        }
    }
}