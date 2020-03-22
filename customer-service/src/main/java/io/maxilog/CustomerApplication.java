package io.maxilog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@EnableDiscoveryClient
@SpringBootApplication
public class CustomerApplication {

    private static final Logger log = LoggerFactory.getLogger(CustomerApplication.class);

    private final Environment env;

    public CustomerApplication(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

}
