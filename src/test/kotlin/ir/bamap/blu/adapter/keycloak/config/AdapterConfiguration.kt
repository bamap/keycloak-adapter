package ir.bamap.blu.adapter.keycloak.config

import ir.bamap.blu.adapter.keycloak.adapter.KeycloakUserAdapter
import ir.bamap.blu.adapter.keycloak.adapter.TokenAdapter
import ir.bamap.blu.adapter.keycloak.provider.ClientTokenProvider
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient
import tools.jackson.databind.ObjectMapper


@SpringBootConfiguration
//@TestConfiguration(proxyBeanMethods = false)
open class AdapterConfiguration(
    private val kycInfo: KycInfo
) {

    @Bean
    open fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }

    @Bean
    open fun keycloakErrorHandler(): KeycloakErrorHandler {
        return KeycloakErrorHandler(ObjectMapper())
    }

    @Bean
    open fun adapterWebClient(builder: WebClient.Builder, errorHandler: KeycloakErrorHandler): WebClient {
        return builder
            .baseUrl(kycInfo.getRealmUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter(errorHandler.handleResponseError())
            .filter(errorHandler.handleConnectionError())
//            .defaultHeader("Authorization", "token " + properties.getToken())
            .build()
    }

    @Bean
    open fun tokenAdapter(adapterWebClient: WebClient): TokenAdapter {
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(adapterWebClient))
            .build()
            .createClient<TokenAdapter>()
    }

    @Bean
    open fun userAdapter(
        builder: WebClient.Builder,
        clientTokenProvider: ClientTokenProvider,
        errorHandler: KeycloakErrorHandler
    ): KeycloakUserAdapter {
        val adapterWebClient = builder
            .baseUrl(kycInfo.getAdminRealmUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter(oauthFilter(clientTokenProvider))
            .filter(errorHandler.handleResponseError())
            .filter(errorHandler.handleConnectionError())
            .build()
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(adapterWebClient))
            .build()
            .createClient<KeycloakUserAdapter>()
    }

    @Bean
    open fun clientTokenProvider(tokenAdapter: TokenAdapter): ClientTokenProvider {
        return ClientTokenProvider(tokenAdapter, kycInfo.clientId, kycInfo.clientSecret)
    }

    private fun oauthFilter(tokenProvider: ClientTokenProvider): ExchangeFilterFunction {
        return ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->

            val token = tokenProvider.getTokenOrNull()
            val filteredRequest = ClientRequest.from(request)
                .header("Authorization", token)
                .build()
            next.exchange(filteredRequest)
        }
    }
}