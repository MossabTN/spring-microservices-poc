import { KeycloakConfig } from 'keycloak-angular';

// Add here your keycloak setup infos
let keycloakConfig: KeycloakConfig = {
  url: 'KEYCLOAK_URL',
  realm: 'KEYCLOAK_REALM',
  clientId: 'front'
};

export const environment = {
  production: false,
  apis: {
    customer: '/api/customer',
    notification: '/api/notification',
    product: '/api/product',
    order: '/api/order',
    websocket: 'https://poc-spring.maxilog.tech/websocket'
  },
  keycloak: keycloakConfig
};
