package io.maxilog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@EnableDiscoveryClient
@SpringBootApplication
@EnableSchemaRegistryClient
public class OrderApplication {

    private static final Logger log = LoggerFactory.getLogger(OrderApplication.class);

    private final Environment env;

    public OrderApplication(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
