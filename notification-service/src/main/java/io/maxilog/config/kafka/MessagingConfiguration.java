package io.maxilog.config.kafka;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(value = {NotificationMessaging.class})
public class MessagingConfiguration {

}

