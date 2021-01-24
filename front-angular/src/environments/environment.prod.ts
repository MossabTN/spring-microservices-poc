import { KeycloakConfig } from 'keycloak-angular';

// Add here your keycloak setup infos
let keycloakConfig: KeycloakConfig = {
  url: 'https://auth.maxilog.tech/auth',
  realm: 'poc',
  clientId: 'front'
};

export const environment = {
  production: false,
  apis: {
    customer: 'https://poc-spring.maxilog.tech/api/customer',
    notification: 'https://poc-spring.maxilog.tech/api/notification',
    product: 'https://poc-spring.maxilog.tech/api/product',
    order: 'https://poc-spring.maxilog.tech/api/order'
  },
  keycloak: keycloakConfig
};
