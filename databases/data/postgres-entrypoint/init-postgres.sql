CREATE DATABASE "product";
CREATE USER "maxilog-product-user" WITH PASSWORD 'maxilog-product-password';
GRANT ALL PRIVILEGES ON DATABASE "product" TO "maxilog-product-user";

CREATE DATABASE "order";
CREATE USER "maxilog-order-user" WITH PASSWORD 'maxilog-order-password';
GRANT ALL PRIVILEGES ON DATABASE "order" TO "maxilog-order-user";
