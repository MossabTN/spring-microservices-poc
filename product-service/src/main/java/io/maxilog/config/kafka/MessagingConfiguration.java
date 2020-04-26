package io.maxilog.config.kafka;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(value = {ProductMessaging.class})
public class MessagingConfiguration {

}

