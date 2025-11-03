package ir.bamap.blu.adapter.keycloak.config

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistrar
import org.springframework.test.context.DynamicPropertyRegistry

@SpringBootConfiguration
open class ContainerConfiguration {

    @Bean
    open fun keycloakContainer(): KeycloakContainer {
        return KeycloakContainer("keycloak/keycloak:26.1")
            .withAdminUsername("admin")
            .withAdminPassword("admin")
            .withRealmImportFile("realm-card.json")
    }

    @Bean
    open fun kycInfo(keycloakContainer: KeycloakContainer): KycInfo {
        return KycInfo(
            keycloakContainer.authServerUrl, "Card", "prepaid", "fckXQKZf9f9FINKa2XUw1DyEUt8HtHlx"
        )

    }
}