import { KeycloakConfig } from 'keycloak-angular';

// Add here your keycloak setup infos
let keycloakConfig: KeycloakConfig = {
  url: 'KEYCLOAK_URL/auth',
  realm: 'KEYCLOAK_REALM',
  clientId: 'front'
};

export const environment = {
  production: true,
  apis: {
    customer: '/api/customer',
    notification: '/api/notification',
    product: '/api/product',
    order: '/api/order',
    websocket: '/websocket'
  },
  keycloak: keycloakConfig
};
