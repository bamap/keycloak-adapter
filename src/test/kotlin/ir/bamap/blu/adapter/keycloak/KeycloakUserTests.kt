package ir.bamap.blu.adapter.keycloak

import com.github.javafaker.Faker
import ir.bamap.blu.adapter.keycloak.adapter.KeycloakUserAdapter
import ir.bamap.blu.adapter.keycloak.config.AdapterConfiguration
import ir.bamap.blu.adapter.keycloak.config.ContainersConfiguration
import ir.bamap.blu.adapter.keycloak.model.UserRepresentation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@Import(value = [ContainersConfiguration::class, AdapterConfiguration::class])
//@TestPropertySource(locations = ["classpath:application-test.properties"])
class KeycloakUserTests @Autowired constructor(
    private val keycloakUserAdapter: KeycloakUserAdapter
) {

    private val logger = LoggerFactory.getLogger(KeycloakUserTests::class.java)
    private val faker = Faker()

    @Test
    fun `add and remove user`() {
        val nameFaker = faker.name()
        val user = UserRepresentation(
            username = nameFaker.username(),
            firstName = nameFaker.firstName(),
            lastName = nameFaker.lastName()
        )
        logger.info("Adding user(${user.username}) to keycloak")
        keycloakUserAdapter.add(user)
        val keycloakUser = keycloakUserAdapter.findSummaryByUsernameOrNull(user.username)
        assertNotNull(keycloakUser)

        val userId = keycloakUser.id ?: return
        logger.info("Deleting user(${user.username}:$userId) from keycloak")
        keycloakUserAdapter.delete(userId)

        val deletedUser = keycloakUserAdapter.findSummaryByUsernameOrNull(user.username)
        assertNull(deletedUser)
    }
}
