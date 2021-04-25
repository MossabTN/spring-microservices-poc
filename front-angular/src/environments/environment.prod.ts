import { KeycloakConfig } from 'keycloak-angular';

// Add here your keycloak setup infos
let keycloakConfig: KeycloakConfig = {
  url: 'https://keycloak.maxilog.tech/auth',
  realm: 'poc',
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
