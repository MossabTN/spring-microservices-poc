package io.maxilog.config.kafka;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(value = {OrderMessaging.class})
public class MessagingConfiguration {

}

