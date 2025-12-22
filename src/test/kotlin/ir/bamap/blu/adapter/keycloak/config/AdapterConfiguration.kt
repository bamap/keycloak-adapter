package ir.bamap.blu.adapter.keycloak.config

import ir.bamap.blu.adapter.keycloak.adapter.KeycloakUserAdapter
import ir.bamap.blu.adapter.keycloak.adapter.TokenAdapter
import ir.bamap.blu.adapter.keycloak.provider.ClientTokenProvider
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient
import reactor.core.publisher.Mono
import tools.jackson.databind.ObjectMapper


//@SpringBootConfiguration
@TestConfiguration(proxyBeanMethods = false)
open class AdapterConfiguration(
    private val kycInfo: KycInfo
) {

    @Bean
    fun adapterWebClient(builder: WebClient.Builder): WebClient {
        return builder
            .baseUrl(kycInfo.getRealmUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//            .defaultHeader("Authorization", "token " + properties.getToken())
//            .filter(errorHandler())
//            .filter(connectionErrorHandler())
//            .filter(rateLimitHandler())
            .build()
    }

    @Bean
    fun tokenAdapter(adapterWebClient: WebClient): TokenAdapter {
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(adapterWebClient))
            .build()
            .createClient<TokenAdapter>()
    }

    @Bean
    fun userAdapter(builder: WebClient.Builder, clientTokenProvider: ClientTokenProvider): KeycloakUserAdapter {
        val adapterWebClient = builder
            .baseUrl(kycInfo.getAdminRealmUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter(oauthFilter(clientTokenProvider))
//            .defaultHeader("Authorization", "token " + properties.getToken())
            .filter(errorHandler())
//            .filter(connectionErrorHandler())
//            .filter(rateLimitHandler())
            .build()
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(adapterWebClient))
            .build()
            .createClient<KeycloakUserAdapter>()
    }

    @Bean
    fun clientTokenProvider(tokenAdapter: TokenAdapter): ClientTokenProvider {
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

    @Bean
    open fun keycloakErrorDecoder(): KeycloakErrorDecoder {
        return KeycloakErrorDecoder(ObjectMapper())
    }

    private fun errorHandler(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { response: ClientResponse ->
            if (response.statusCode().isError) {
                println("Error Status Code: ${response.statusCode().value()}")
                return@ofResponseProcessor response.bodyToMono<String>()
                    .flatMap { errorBody: String? ->
                        println("Error: $errorBody")
                        Mono.error(RuntimeException())
                    }
            }
            Mono.just(response)
        }
    }
}