package ir.bamap.blu.adapter.keycloak.config

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.TestPropertySource

//@SpringBootConfiguration
@TestPropertySource(locations = ["classpath:application-test.properties"])
@TestConfiguration(proxyBeanMethods = false)
class ContainersConfiguration {

    @Bean
    open fun keycloakContainer(): KeycloakContainer {
        return KeycloakContainer("keycloak/keycloak:26.3")
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