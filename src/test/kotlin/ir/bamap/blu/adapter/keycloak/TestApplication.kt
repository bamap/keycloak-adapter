package ir.bamap.blu.adapter.keycloak

import ir.bamap.blu.adapter.keycloak.config.ContainersConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.fromApplication
import org.springframework.boot.with

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	fromApplication<Application>().with(ContainersConfiguration::class).run(*args)
}
