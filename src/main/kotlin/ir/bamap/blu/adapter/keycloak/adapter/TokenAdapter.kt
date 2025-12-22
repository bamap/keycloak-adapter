package ir.bamap.blu.adapter.keycloak.adapter

import ir.bamap.blu.adapter.keycloak.model.OAuthToken
import ir.bamap.blu.adapter.keycloak.model.login.LoginModel
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange

interface TokenAdapter {
    @PostExchange(
        value = "/protocol/openid-connect/token",
        contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    fun getToken(@RequestBody form: MultiValueMap<String, String>): OAuthToken

    fun getToken(body: LoginModel): OAuthToken {
        val parameters = LinkedMultiValueMap<String, String>()
        body.getParameters().forEach { (key, value) -> parameters[key] = value.toString() }
        return getToken(parameters)
    }
}