FROM nginx:alpine
EXPOSE 8080
COPY /dist /usr/share/nginx/html
COPY ./.nginx/nginx.conf /etc/nginx/nginx.conf

RUN chmod -R 777 /var/log/nginx /var/cache/nginx /var/run \
     && chgrp -R 0 /etc/nginx \
     && chmod -R g+rwX /etc/nginx \
     && rm /etc/nginx/conf.d/default.conf

ENV KEYCLOAK_URL=https://auth.maxilog.me/auth
ENV KEYCLOAK_REALM=poc

ENTRYPOINT ["/bin/sh", "-c", "sed -i \"s#KEYCLOAK_URL#$KEYCLOAK_URL#g\" /usr/share/nginx/html/main.*.js;sed -i \"s#KEYCLOAK_REALM#$KEYCLOAK_REALM#g\" /usr/share/nginx/html/main.*.js;nginx -g 'daemon off;'"]

