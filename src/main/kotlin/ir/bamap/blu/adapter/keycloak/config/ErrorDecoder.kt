package ir.bamap.blu.adapter.keycloak.config

import ir.bamap.blu.exception.BluException
import org.springframework.web.reactive.function.client.ClientResponse

interface ErrorDecoder {

    fun decode(response: ClientResponse): BluException
}