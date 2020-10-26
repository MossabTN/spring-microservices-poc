package io.maxilog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.core.MessageSendingOperations;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@EnableDiscoveryClient
@EnableSchemaRegistryClient
@SpringBootApplication
public class NotificationApplication {

    private static final Logger log = LoggerFactory.getLogger(NotificationApplication.class);

    private final Environment env;

    public NotificationApplication(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Autowired
    MessageSendingOperations messagingTemplate;
    Map<String, Integer> progress = new HashMap<>();

    //@Bean
    public CommandLineRunner websocketDemo() {
        return (args) -> {
            while (true) {
                try {
                    Thread.sleep(3*1000); // Each 3 sec.
                    progress.put("num1", 100);
                    progress.put("num1", 100);
                    messagingTemplate.convertAndSend("/notifications", this.progress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
