package ir.bamap.blu.adapter.keycloak.adapter

import ir.bamap.blu.adapter.keycloak.model.OAuthToken
import ir.bamap.blu.adapter.keycloak.model.login.LoginModel
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@JvmDefaultWithCompatibility
interface TokenAdapter {

    @PostMapping(value = ["/protocol/openid-connect/token"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getToken(@RequestBody form: MultiValueMap<String, Any>): OAuthToken

    fun getToken(body: LoginModel): OAuthToken {
        val parameters = LinkedMultiValueMap<String, Any>()
        body.getParameters().forEach { (key, value) -> parameters[key] = value }
        return getToken(parameters)
    }
}
