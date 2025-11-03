package ir.bamap.blu.adapter.keycloak.config

class Messages {
    companion object {
        val DUPLICATE_EMAIL: String = "DUPLICATE_EMAIL"
        val DUPLICATE_USERNAME: String = "DUPLICATE_USERNAME"
        val ATTRIBUTE_REQUIRED: String = "ATTRIBUTE_REQUIRED"
        val INVALID_USER_CREDENTIALS: String = "INVALID_USER_CREDENTIALS"
        val INVALID_CLIENT_CREDENTIALS: String = "INVALID_CLIENT_CREDENTIALS"
        val ACCOUNT_NOT_FULLY_SETUP: String = "ACCOUNT_NOT_FULLY_SETUP"
        val ACCESS_DENIED: String = "ACCESS_DENIED"
        val KEYCLOAK_INTERNAL_SERVER_ERROR: String = "KEYCLOAK_INTERNAL_SERVER_ERROR"
        val SSO_INTERNAL_SERVER_ERROR: String = "SSO_INTERNAL_SERVER_ERROR"
        val NOT_FOUND: String = "NOT_FOUND"
    }

    class Keycloak {
        companion object {
            val INVALID_CREDENTIALS: String = "Invalid user credentials"
            val INVALID_CLIENT_CREDENTIALS: String = "Invalid client or Invalid client credentials"
            val ACCOUNT_NOT_FULLY_SETUP: String = "Account is not fully set up"
        }
    }
}