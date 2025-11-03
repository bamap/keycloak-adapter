package ir.bamap.blu.adapter.keycloak.exception

class AccountNotFullySetupException : SecurityAdapterException(400, "{blu.security.accountNotFullySetup}") {
}