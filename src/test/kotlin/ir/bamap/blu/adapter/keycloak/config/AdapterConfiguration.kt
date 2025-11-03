package ir.bamap.blu.adapter.keycloak.config

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Client
import feign.Contract
import feign.Feign
import feign.codec.Decoder
import feign.codec.Encoder
import feign.form.FormEncoder
import ir.bamap.blu.adapter.keycloak.adapter.KeycloakUserAdapter
import ir.bamap.blu.adapter.keycloak.adapter.TokenAdapter
import ir.bamap.blu.adapter.keycloak.provider.ClientTokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@SpringBootConfiguration
@Import(FeignClientsConfiguration::class)
open class AdapterConfiguration(
    private val kycInfo: KycInfo
) {

    @Bean
    open fun tokenAdapter(
        builder: Feign.Builder, client: Client, messageConverters: HttpMessageConverters,
        contract: Contract, encoder: Encoder, decoder: Decoder, errorDecoder: KeycloakErrorDecoder
    ): TokenAdapter {
        val end = FormEncoder()
        return builder.client(client)
            .contract(contract)
            .encoder(end)
            .decoder(decoder)
            .errorDecoder(errorDecoder)
            .target(TokenAdapter::class.java, kycInfo.getRealmUrl())
    }

    @Bean
    open fun keycloakUserAdapter(
        client: Client, contract: Contract, encoder: Encoder, tokenAdapter: TokenAdapter,
        decoder: Decoder, errorDecoder: KeycloakErrorDecoder
    ): KeycloakUserAdapter {
        val tokenProvider = ClientTokenProvider(tokenAdapter, kycInfo.clientId, kycInfo.clientSecret)
        val interceptor = TokenRequestInterceptor(tokenProvider)
        return Feign.builder()
            .client(client)
            .contract(contract)
            .encoder(encoder)
            .decoder(decoder)
            .errorDecoder(errorDecoder)
            .requestInterceptor(interceptor)
            .target(KeycloakUserAdapter::class.java, kycInfo.getAdminRealmUrl())
    }

    @Bean
    open fun keycloakErrorDecoder(): KeycloakErrorDecoder {
        return KeycloakErrorDecoder(ObjectMapper())
    }

    @Bean
    open fun getClient(): Client {
        return Client.Default(null, null, false)
    }

    @Bean
    open fun messageConverters(): HttpMessageConverters {
        return HttpMessageConverters()
    }
}