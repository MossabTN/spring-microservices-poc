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
public class NotificationApplication {

    private static final Logger log = LoggerFactory.getLogger(NotificationApplication.class);

    private final Environment env;

    public NotificationApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @PostConstruct
    public void initApplication() {
    }

}
