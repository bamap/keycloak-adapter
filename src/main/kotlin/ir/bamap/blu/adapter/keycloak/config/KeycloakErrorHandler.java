package ir.bamap.blu.adapter.keycloak.config;

import ir.bamap.blu.adapter.config.error.AdapterErrorHandler;
import tools.jackson.databind.ObjectMapper;

public class KeycloakErrorHandler extends AdapterErrorHandler {

    public KeycloakErrorHandler(ObjectMapper objectMapper) {
        super("Keycloak", objectMapper);
    }

    @Override
    protected void initStrategies() {
        super.initStrategies();
    }
}
